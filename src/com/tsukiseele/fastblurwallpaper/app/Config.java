package com.tsukiseele.fastblurwallpaper.app;

import com.google.gson.Gson;
import com.tsukiseele.fastblurwallpaper.utils.IOUtil;

import java.awt.Toolkit;
import java.io.IOException;

public class Config {
	public static final transient Config INSTANCE;
	
	public int wallpaperWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	public int wallpaperHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	public int radius = 20;
	public String format = "PNG";
	
	private Config() {}
	
	static {
		Gson config = new Gson();
		String configPath = System.getProperty("user.dir") + "/config.json";
		String configText = "{}";
		try {
			configText = IOUtil.readText(configPath, "UTF-8");
		} catch (IOException e) {
			System.out.println("警告：找不到配置文件，已使用默认配置");
		}
		INSTANCE = config.fromJson(configText, Config.class);
	}
}
