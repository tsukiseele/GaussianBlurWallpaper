package com.tsukiseele.fastblurwallpaper.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by TsukiSeele on 2018.12.22
 *
 * Last modified in 2018.12.22
 */
public class ImageUtil {

	/**
	 * 使用高斯模糊处理图像
	 * @param path 图像的路径
	 * @param radius 模糊半径
	 * @return 处理后的图像
	 * @throws IOException
	 */
	public static BufferedImage doBlur(File path, int radius) throws IOException {
		return doBlur(ImageIO.read(path), radius);
	}

	/**
	 * 使用高斯模糊处理图像
	 * @param image 待处理的图像
	 * @param radius 模糊半径
	 * @return 处理后的图像
	 * @throws IOException
	 */
	public static BufferedImage doBlur(BufferedImage image, int radius) {

		int width = image.getWidth();
		int height = image.getHeight();

		int[] blurPixels = FastBlurUtil.getBlurTemplate(getImagePixel(image), width, height, radius);
		BufferedImage blurImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		blurImage.setRGB(0, 0, width, height, blurPixels, 0, width);
		return blurImage;
	}

	/**
	 * 获取图像的像素数据
	 * @param image 需要处理的图像
	 * @return 图像像素数据
	 */
	public static int[] getImagePixel(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int minx = image.getMinX();
		int miny = image.getMinY();
		int[] pixels = new int[width * height];
		// 逐行扫描
		for (int x = miny; x < height; x++)
			for (int y = minx; y < width; y++)
				pixels[x * width + y] = image.getRGB(y, x);
		return pixels;
	}
	
	/**
	 * 获取图像的字节数据
	 * @param image 需要处理的图像
	 * @param quality 品质(0 ~ 1.0)
	 * @return 图像字节数据
	 */
	public static byte[] toBytes(BufferedImage image, float quality) {
		if (image == null)
			throw new NullPointerException("指定的图片为 null");
		
		if (quality < 0 || quality > 1)
			quality = 0.9F;
		Iterator iterator = ImageIO.getImageWritersByFormatName("JPEG");
		
		ImageWriter imageWriter = (ImageWriter) iterator.next();
		ImageWriteParam param = imageWriter.getDefaultWriteParam();
		param.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			imageWriter.setOutput(ImageIO.createImageOutputStream(baos));
			imageWriter.write(null, new IIOImage(image, null, null), param);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
