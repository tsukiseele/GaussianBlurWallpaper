package com.tsukiseele.fastblurwallpaper.utils;

/**
 * Created in 2018.12.26 by TsukiSeele
 *
 * Last modified in 2018.12.26
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class IOUtil {
	public interface OnFileScanCallback {
		boolean onScan(File file);
	}
	
	public static final int Byte = 1;
	public static final int KB = 1024;
	public static final int MB = 1048576;
	public static final int GB = 1073741824;
	public static final long TB = 1099511627776L;
	
	public enum FileSize {
		Byte(IOUtil.Byte),
		KB(IOUtil.KB),
		MB(IOUtil.MB),
		GB(IOUtil.GB),
		TB(IOUtil.TB);
		private long size = -1;
		
		FileSize(long size) {
			this.size = size;
		}
		
		public long size() {
			return size;
		}
		
		@Override
		public String toString() {
			return this.name();
		}
	}
	
	/**
	 * 格式化数据大小
	 * @param size 以字节为单位的大小
	 * @return 字符串表示形式
	 */
	public static String formatDataSize(double size){
		DecimalFormat format = new DecimalFormat("####.00");
		if (size < 0)
			return String.valueOf(size);
		if (size < KB)
			return size + FileSize.Byte.name();
		else if (size < MB)
			return format.format(size / KB) + FileSize.KB.name();
		else if (size < GB)
			return format.format(size / MB) + FileSize.MB.name();
		else if (size < TB)
			return format.format(size / GB) + FileSize.GB.name();
		else
			return format.format(size / TB) + FileSize.TB.name();
	}
	
	/**
	 * 在指定的文件内打印文本
	 * @param filePath
	 * @param text
	 * @param charset
	 * @param autoFlush
	 * @param append
	 * @return 成功写入则返回true
	 */
	public static boolean printText(String filePath, String text, String charset, boolean autoFlush, boolean append) {
		PrintWriter pw = null;
		boolean isFinish = true;
		try {
			pw = new PrintWriter(
					new OutputStreamWriter(
							new FileOutputStream(filePath, append), charset), autoFlush);
			pw.print(text);
		} catch (Exception e) {
			isFinish = false;
		} finally {
			close(pw);
		}
		return isFinish;
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @param charset
	 * @param append
	 * @return 成功写入则返回true
	 */
	public static boolean printText(String filePath, String text, String charset, boolean append) {
		return printText(filePath, text, charset, false, append);
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @param charset
	 * @return 成功写入则返回true
	 */
	public static boolean printText(String filePath, String text, String charset) {
		return printText(filePath, text, charset, false);
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @param autoFlush
	 * @return 成功写入则返回true
	 */
	public static boolean printText(String filePath, String text, boolean autoFlush) {
		return printText(filePath, text, "UTF-8", true, false);
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @param charset
	 * @param isAppend
	 * @throws IOException
	 */
	public static void writeText(String filePath, String text, String charset, boolean isAppend) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(filePath, isAppend), charset));
			writer.write(text);
		} finally {
			close(writer);
		}
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @param charset
	 * @throws IOException
	 */
	public static void writeText(String filePath, String text, String charset) throws IOException {
		writeText(filePath, text, charset, false);
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @param isAppend
	 * @throws IOException
	 */
	public static void writeText(String filePath, String text, boolean isAppend) throws IOException {
		writeText(filePath, text, "UTF-8", isAppend);
	}
	
	/**
	 *
	 * @param filePath
	 * @param text
	 * @throws IOException
	 */
	public static void writeText(String filePath, String text) throws IOException {
		writeText(filePath, text, "UTF-8", false);
	}
	
	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readText(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		try {
			String line;
			while ((line = reader.readLine()) != null)
				stringBuilder.append(line);
		} finally {
			close(reader);
		}
		return stringBuilder.toString();
	}
	
	/**
	 *
	 * @param filePath
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readText(String filePath, String charset) throws IOException {
		BufferedReader reader = null;
		StringBuilder text = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
			String line = null;
			while ((line = reader.readLine()) != null)
				text.append(line).append("\n");
		} finally {
			close(reader);
		}
		return text.toString();
	}
	
	/**
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readText(String filePath) throws IOException {
		return readText(filePath, "UTF-8");
	}
	
	/**
	 *
	 * @param filePath
	 * @param is
	 * @throws IOException
	 */
	public static void writeByteArray(String filePath, InputStream is) throws IOException {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			int len;
			byte[] buff = new byte[8192];
			while ((len = is.read(buff)) != -1)
				bos.write(buff, 0, len);
		} finally {
			close(bos);
			close(bis);
		}
	}
	
	/**
	 *
	 * @param filePath
	 * @param datas
	 * @throws IOException
	 */
	public static void writeByteArray(String filePath, byte[] datas) throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			bos.write(datas);
		} finally {
			close(bos);
		}
	}
	
	/**
	 *
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] readByteArray(InputStream is) throws IOException {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			bis = new BufferedInputStream(is);
			baos = new ByteArrayOutputStream();
			
			byte[] buff = new byte[8192];
			int length = 0;
			while ((length = bis.read(buff)) != -1)
				baos.write(buff, 0, length);
		} finally {
			close(bis);
			close(baos);
		}
		return baos.toByteArray();
	}
	
	/**
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readByteArray(String filePath) throws IOException {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		byte[] buff = new byte[8192];
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			baos = new ByteArrayOutputStream();
			
			int length = 0;
			while ((length = bis.read(buff)) != -1)
				baos.write(buff, 0, length);
		} finally {
			close(bis);
			close(baos);
		}
		return baos.toByteArray();
	}
	
	/**
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readSerializable(String filePath) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(filePath));
			obj = ois.readObject();
		} finally {
			close(ois);
		}
		return obj;
	}
	
	/**
	 *
	 * @param filePath
	 * @param object
	 * @param <T>
	 * @throws IOException
	 */
	public static <T extends Serializable> void writeSerializable(String filePath, T object) throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(object);
		} finally {
			close(oos);
		}
	}
	
	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean mkdirs(File file) {
		if (file == null)
			return false;
		if (!file.exists())
			return file.mkdirs();
		return false;
	}
	
	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean mkdirsParent(File file) {
		if (file == null)
			return false;
		if (file.getParentFile() != null && !file.getParentFile().exists())
			return file.getParentFile().mkdirs();
		return false;
	}
	
	/**
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean mkdirs(String filePath) {
		if (TextUtil.isEmpty(filePath))
			return false;
		return mkdirs(new File(filePath));
	}
	
	/**
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean mkdirsParent(String filePath) {
		if (TextUtil.isEmpty(filePath))
			return false;
		return mkdirsParent(new File(filePath));
	}
	
	/**
	 * 获取指定文件夹内所有文件大小的和
	 *
	 * @param file file
	 * @return size
	 * @throws Exception
	 */
	public static long getDirectoryAllFileSize(File file) {
		long size = 0;
		File[] fileList = file.listFiles();
		if (fileList == null)
			return 0;
		for (File fileItem : fileList) {
			if (fileItem.isFile()) {
				size += fileItem.length();
			} else {
				size += getDirectoryAllFileSize(fileItem);
			}
		}
		return size;
	}
	
	/**
	 *
	 * @param dirPath
	 * @return
	 */
	public static long getDirectoryAllFileSize(String dirPath) {
		return getDirectoryAllFileSize(new File(dirPath));
	}
	
	/**
	 *
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		if (TextUtil.isEmpty(path))
			return false;
		return deleteFile(new File(path));
	}
	
	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		if (file == null)
			return false;
		return file.delete();
	}
	
	/**
	 * 递归删除指定目录下的文件
	 *
	 * @param dir filePath
	 *
	 */
	public static void deleteDirectoryAllFile(File dir) {
		if (dir == null)
			return ;
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (File file : files)
					if (file.isFile())
						file.delete();
					else
						deleteDirectoryAllFile(file);
				dir.delete();
			} else {
				dir.delete();
			}
		}
	}
	
	/**
	 *
	 * @param dirPath
	 */
	public static void deleteDirectoryAllFile(String dirPath) {
		deleteDirectoryAllFile(new File(dirPath));
	}
	
	/**
	 *
	 * @param fromPath
	 * @param toPath
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String fromPath, String toPath)throws IOException {
		if (TextUtil.isEmpty(fromPath) || TextUtil.isEmpty(toPath))
			return false;
		mkdirsParent(toPath);
		writeByteArray(toPath, readByteArray(fromPath));
		return true;
	}
	
	/**
	 *
	 * @param fromPath
	 * @param toPath
	 * @return
	 */
	public static int copyDirectory(String fromPath, String toPath) {
		int counter = 0;
		List<File> files = scanDirectory(new File(fromPath));
		for (File file : files) {
			try {
				copyFile(file.getAbsolutePath(), file.getAbsolutePath().replaceAll(new File(fromPath).getParentFile().getAbsolutePath(), toPath));
			} catch (IOException e) {
				counter++;
				continue;
			}
		}
		return counter;
	}
	
	/**
	 *
	 * @param fromPath
	 * @param toPath
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(String fromPath, String toPath) throws IOException {
		if (TextUtil.isEmpty(fromPath) || TextUtil.isEmpty(toPath))
			return false;
		mkdirsParent(toPath);
		writeByteArray(fromPath, readByteArray(toPath));
		deleteFile(fromPath);
		return true;
	}
	
	/**
	 *
	 * @param fromPath
	 * @param toPath
	 * @return
	 */
	public static boolean moveDirectory(String fromPath, String toPath) {
		if (TextUtil.isEmpty(fromPath) || TextUtil.isEmpty(toPath))
			return false;
		copyDirectory(fromPath, toPath);
		deleteDirectoryAllFile(fromPath);
		return true;
	}
	
	/**
	 *
	 * @param path
	 * @return
	 */
	public static boolean exists(String path) {
		if (!TextUtil.isEmpty(path))
			return exists(new File(path));
		return false;
	}
	
	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean exists(File file) {
		if (file != null && file.exists())
			return true;
		return false;
	}
	
	/**
	 *
	 * @param closeable
	 * @return
	 */
	public static boolean close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
				return true;
			}
		} catch (IOException e) {
			// 这里不作处理
		}
		return false;
	}
	
	/**
	 *
	 * @param directory
	 * @param suffixs
	 * @return
	 */
	// 获取目录内随机文件
	public static File getRandomFile(final File directory, final String[] suffixs) {
		if (directory == null || !directory.isDirectory())
			return null;
		File[] images = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File parent, String name) {
				for (String suffix : suffixs)
					if (name.endsWith(suffix))
						return true;
				return false;
			}
		});
		File image = null;
		if (images != null && images.length > 0) {
			image = images[(int) (Math.random() * images.length)];
			// 大于2MB则重随机
			for (int i = 0; i < 5; i++)
				if (image.length() > 2 * IOUtil.MB)
					image = images[(int) (Math.random() * images.length)];
		}
		return image;
	}
	
	
	/**
	 * 扫描目录
	 * @param rootPath
	 * @param suffixs
	 * @return
	 */
	public static List<File> scanDirectory(final String rootPath, final String... suffixs) {
		return scanDirectory(new File(rootPath), suffixs);
	}
	
	/**
	 *
	 * @param rootPath
	 * @param suffixs
	 * @return
	 */
	public static List<File> scanDirectory(final File rootPath, final String... suffixs) {
		return scanDirectory(rootPath, null, false, suffixs);
	}
	
	/**
	 *
	 * @param rootPath
	 * @param containDirectory
	 * @param suffixs
	 * @return
	 */
	public static List<File> scanDirectory(final File rootPath, final boolean containDirectory,  final String... suffixs) {
		return scanDirectory(rootPath, null, containDirectory, suffixs);
	}
	
	/**
	 *
	 * @param rootPath
	 * @param callback
	 * @param containDirectory
	 * @param suffixs
	 * @return
	 */
	public static List<File> scanDirectory(final File rootPath, final OnFileScanCallback callback, final boolean containDirectory, final String... suffixs) {
		if (!rootPath.exists()) {
			return null;
		}
		File[] files = null;
		if (suffixs == null || suffixs.length == 0)
			files = rootPath.listFiles();
		else
			files = rootPath.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.isDirectory())
						return true;
					else
						for (String suffix : suffixs)
							if (file.getName().endsWith(suffix))
								return true;
					return false;
				}
			});
		List<File> fileList = new ArrayList<>(files.length);
		for (File file : files) {
			if (file.isDirectory()) {
				List<File> fs = scanDirectory(file, callback, containDirectory, suffixs);
				if (fs != null)
					fileList.addAll(fs);
				if (containDirectory)
					fileList.add(file);
			} else {
				if (callback != null) {
					if (callback.onScan(file))
						fileList.add(file);
				} else {
					fileList.add(file);
				}
			}
		}
		return fileList;
	}
	
	/**
	 *
	 * @param url
	 * @return
	 */
	public static String getUrlFilename(String url) {
		String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
		int endIndex = filename.indexOf("?");
		if (endIndex != -1)
			filename = filename.substring(0, endIndex);
		return filename;
	}
	
	/**
	 *
	 * @param file
	 * @param append
	 * @return
	 */
	public static File appendFilename(File file, String append) {
		int end = file.getName().lastIndexOf(".");
		String oldName = file.getName();
		String newName;
		if (end != -1) {
			newName = oldName.substring(0, end) + append + oldName.substring(end);
		} else {
			newName = file.getName() + append;
		}
		return new File(file.getParent(), newName);
	}
	
	/**
	 *
	 * @param filename
	 * @return
	 */
	public static String getWindowsFilename(String filename) {
		return filename.replaceAll(":", "%3A")
				.replaceAll("\\*", "%2A")
				.replaceAll("\\?", "%3F")
				.replaceAll("\"", "%22")
				.replaceAll("<", "%3C")
				.replaceAll(">", "%3E")
				.replaceAll("\\|", "%7C");
	}
	
	/**
	 *
	 * @param filename
	 * @return
	 */
	public static String getWindowsPath(String filename) {
		return filename.replaceAll("\\*", "%2A")
				.replaceAll("\\?", "%3F")
				.replaceAll("\"", "%22")
				.replaceAll("<", "%3C")
				.replaceAll(">", "%3E")
				.replaceAll("\\|", "%7C");
	}
	
	/**
	 *
	 * @param filename
	 * @return
	 */
	public static String getPureFilename(String filename) {
		return filename.replaceAll("\\.\\.\\\\|\\\\|/|../", "-");
	}
	
	/**
	 * 获取字符串形式的文件名后缀
	 * @param file
	 * @return
	 */
	public static String getFileSuffix(File file) {
		return getFileSuffix(file.getAbsolutePath());
	}
	
	/**
	 * 获取字符串形式的文件名后缀
	 * @param filename 文件名
	 * @return 文件名后缀
	 */
	public static String getFileSuffix(String filename) {
		if (TextUtil.isEmpty(filename)) {
			return "";
		} else {
			int index = filename.lastIndexOf('.');
			return (index != -1 && ++index < filename.length()) ? filename.substring(index, filename.length()) : "";
		}
	}
	
	/**
	 * 修改文件名后缀
	 * @param file 文件名
	 * @param suffix 修改后的后缀
	 * @return 修改后的文件名
	 */
	public static File replaceFileSuffix(File file, String suffix) {
		return new File(file.getParent(), getFilenameNoSuffix(file.getName()) + '.' + suffix);
	}
	
	/**
	 * 获取不包含后缀的文件名
	 * @param filename 原文件名
	 * @return 新文件名
	 */
	public static String getFilenameNoSuffix(String filename) {
		if (TextUtil.isEmpty(filename)) {
			return filename;
		} else {
			int index = filename.lastIndexOf('.');
			return  (index != -1) ? filename.substring(0, index) : filename;
		}
	}
	
	public static class ZipUtil {
		public interface OnCompressProgress {
			boolean onProgress(File file, long length);
		}
		
		/**
		 *
		 * @param directory
		 * @return
		 * @throws IOException
		 */
		public static File createZip(File directory) throws IOException {
			return createZip(directory, null);
		}
		
		/**
		 *
		 * @param directory
		 * @param onCompressProgress
		 * @return
		 * @throws IOException
		 */
		public static File createZip(File directory, OnCompressProgress onCompressProgress) throws IOException {
			ZipOutputStream zos = null;
			BufferedInputStream bis = null;
			File zip = new File(directory + ".zip");
			try {
				zos = new ZipOutputStream(new FileOutputStream(zip));
				List<File> fileList = scanDirectory(directory);
				
				for (File f : fileList) {
					bis = new BufferedInputStream(new FileInputStream(f));
					ZipEntry entry = new ZipEntry(f.getAbsolutePath().replace(directory.getAbsolutePath(), ""));
					zos.putNextEntry(entry);
					byte[] buff = new byte[8192];
					long length = 0;
					int pos = 0;
					while ((pos = bis.read(buff)) > 0) {
						zos.write(buff, 0, pos);
						length += pos;
						if (onCompressProgress != null)
							onCompressProgress.onProgress(f, length);
					}
					bis.close();
					zos.closeEntry();
				}
			} finally {
				if (bis != null) bis.close();
				if (zos != null) zos.close();
			}
			return zip;
		}
	}
}
