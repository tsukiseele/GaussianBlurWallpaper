package com.tsukiseele.fastblurwallpaper;

import com.tsukiseele.fastblurwallpaper.app.Config;
import com.tsukiseele.fastblurwallpaper.app.FastBlurWallpaper;
import com.tsukiseele.fastblurwallpaper.utils.IOUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        
        do {
            Scanner scan = new Scanner(System.in);
            System.out.println("=============== CONFIG ===============");
            System.out.println("radius: \t" + Config.INSTANCE.radius);
            System.out.println("width: \t\t" + Config.INSTANCE.wallpaperWidth);
            System.out.println("height: \t" + Config.INSTANCE.wallpaperHeight);
            System.out.println("=============== CONFIG ===============");
            System.out.print("输入图像路径：");
            String[] params = new String[] {scan.nextLine()};
            
            File imagePath = null;
            
            imagePath = new File(params[0].replaceAll("\"", ""));
            
            
            File outPath = IOUtil.appendFilename(new File(imagePath.getParent(),
                    IOUtil.getUrlFilename(imagePath.getName())), "_BLUR" + Config.INSTANCE.radius);
            System.out.println("正在处理...");
//            System.out.println(outPath);
            BufferedImage inputImage = ImageIO.read(imagePath);
            
            BufferedImage backgroundImage = false//params.length > 1
                    ? ImageIO.read(new File(params[1].replaceAll("\"", "")))
                    : inputImage;
            
            BufferedImage image = FastBlurWallpaper.createBlurWallpaper(inputImage, backgroundImage,
                    Config.INSTANCE.wallpaperWidth, Config.INSTANCE.wallpaperHeight, Config.INSTANCE.radius);
    
//            FileUtil.writeByteArray(outPath.getAbsolutePath(), ImageUtil.toBytes(image, 0.8F));
            String format = Config.INSTANCE.format;
            ImageIO.write(image, format, IOUtil.replaceFileSuffix(outPath, format.toLowerCase()));
           
            System.out.println("处理完毕, 已保存到: " + outPath.getAbsolutePath() + '\n');
            
        } while (true);
    }
}

