package com.tsukiseele.fastblurwallpaper.app;

/**
 * Created by TsukiSeele in 2018.12.26
 *
 * Last modified in 2018.12.26
 */
import com.tsukiseele.fastblurwallpaper.utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FastBlurWallpaper {

	public static BufferedImage createBlurWallpaper(BufferedImage image, BufferedImage background, int wallpaperWidth, int wallpaperHeight, int radius) {
		BufferedImage blurBackground = centerFitImage(ImageUtil.doBlur(background, radius),
				wallpaperWidth, wallpaperHeight);
		BufferedImage content = scaledImage(image, wallpaperWidth, wallpaperHeight, true);
		
		Graphics graphics = blurBackground.getGraphics();
		if (blurBackground.getWidth() == content.getWidth())
			graphics.drawImage(content, 0, (blurBackground.getHeight() - content.getHeight()) / 2, null);
		else
			graphics.drawImage(content, (blurBackground.getWidth() - content.getWidth()) / 2, 0, null);
		graphics.dispose();
		return blurBackground;
	}

	/**
	 * 居中裁剪图像
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage centerFitImage(BufferedImage image, int width, int height) {
		// 使用长宽比进行裁剪
		float ratioW = (float) image.getWidth() / width;
		float ratioH = (float) image.getHeight() / height;
		int newWidth = width;
		int newHeight = height;
		if (ratioW > ratioH)
			newWidth = Math.round(image.getWidth() / ratioH);
		else
			newHeight = Math.round(image.getHeight() / ratioW);
		// 居中裁剪的X轴坐标
		BufferedImage scaledImage = scaledImage(image, newWidth, newHeight, false);

		if (scaledImage.getWidth() == width) {
			return scaledImage.getSubimage(0, (scaledImage.getHeight() - height) / 2, width, height);
		} else {
			return scaledImage.getSubimage((scaledImage.getWidth() - width) / 2, 0, width, height);
		}
	}

	/**
	 * 图像缩放
	 * @param image 源图片
	 * @param width 指定宽度
	 * @param height 指定高度
	 * @param proportion 是否等比缩放
	 * @return 新图片
	 */
	public static BufferedImage scaledImage(BufferedImage image, int width, int height, boolean proportion) {
		// 优先使用 Image.SCALE_SMOOTH 缩略算法,生成缩略图片的平滑度的,优先级比速度高,生成的图片质量更好,但速度更慢
		return scaledImage(image, width, height, proportion, Image.SCALE_SMOOTH);
	}

	/**
	 * 图像缩放
	 * @param image 源图片
	 * @param width 指定宽度
	 * @param height 指定高度
	 * @param proportion 是否等比缩放
	 * @param scaleType 缩放算法类型
	 * @return 新图片
	 */
	public static BufferedImage scaledImage(BufferedImage image, int width, int height, boolean proportion, int scaleType) {
		int newWidth;
		int newHeight;
		// 判断是否是等比缩放
		if (proportion) {
			// 为等比缩放计算输出的图片宽度及高度
			double rate1 = ((double) image.getWidth()) / width;
			double rate2 = ((double) image.getHeight()) / height;
			// 根据缩放比率大的进行缩放控制
			double rate = rate1 > rate2 ? rate1 : rate2;
			newWidth = (int) (((double) image.getWidth()) / rate);
			newHeight = (int) (((double) image.getHeight()) / rate);
		} else {
			newWidth = width; // 输出的图片宽度
			newHeight = height; // 输出的图片高度
		}
		// 如果图片小于目标图片的宽和高则不进行转换
//		if (image.getWidth(null) < width && image.getHeight() < height) {
//			newWidth = image.getWidth();
//			newHeight = image.getHeight();
//		}
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = newImage.getGraphics();
		graphics.drawImage(image.getScaledInstance(newWidth, newHeight, scaleType), 0, 0, null);
		graphics.dispose();
		return newImage;
	}
}
