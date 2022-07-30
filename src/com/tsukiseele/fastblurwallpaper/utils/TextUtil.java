package com.tsukiseele.fastblurwallpaper.utils;

/**
 * Created by TsukiSeele in 2018.12.26
 *
 * Last modified in 2018.12.26
 */
public class TextUtil {
	public static boolean isEmpty(String text) {
		return null == text || text.trim().isEmpty();
	}

	public static boolean isEmptyAll(String... texts) {
		for (String text : texts)
			if (!isEmpty(text))
				return false;
		return true;
	}
	public static boolean nonEmpty(String text) {
		return null != text && !text.trim().isEmpty();
	}

	public static boolean nonEmptyAll(String... texts) {
		return !isEmptyAll(texts);
	}
	public static int toInt(String text) {
		int num = 0;
		try {
			num = Integer.parseInt(text);
		} catch (Exception e) {}
		return num;
	}
} 
