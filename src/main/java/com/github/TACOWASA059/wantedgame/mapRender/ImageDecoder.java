package com.github.TACOWASA059.wantedgame.mapRender;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageDecoder {
    public static BufferedImage decodeImage(String base64Image) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        BufferedImage image = ImageIO.read(bis);
        bis.close();
        return image;
    }
    public static BufferedImage extractFace(BufferedImage img){
        int l1 = 9;
        int l2 = 10;

        int size = 8 * l2; // 変換後の画像サイズを計算する
        BufferedImage refList = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        int central = 4 * l2; // 中心座標を計算する

        // 変換処理を行う
        for (int i = 0; i < 8 * l1; i++) {
            for (int j = 0; j < 8 * l1; j++) {
                int x = i / l1 + 8;
                int y = j / l1 + 8;
                int pixel = img.getRGB(y, x);
                refList.setRGB( (int) central - 4 * l1 + j,(int) central - 4 * l1 + i, pixel);
            }
        }

        for (int i = 0; i < 8 * l2; i++) {
            for (int j = 0; j < 8 * l2; j++) {
                int x = i / l2 + 8;
                int y = j / l2 + 40;
                int pixel = img.getRGB(y, x);
                if ((pixel & 0xff000000) != 0) {
                    refList.setRGB((int) central - 4 * l2 + j,(int) central - 4 * l2 + i, pixel);
                }
            }
        }
        return refList;
    }
    public static BufferedImage addtext(BufferedImage originalImage,String text){
        // 画像の幅と高さを取得
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        // 新しい画像を作成
        BufferedImage newImage = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);

        // 新しい画像に元の画像をコピー
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(originalImage, 20, 40, null);

        // 文字列を書き込む
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        int imageWidth1 = newImage.getWidth();
        int textWidth = g2d.getFontMetrics().stringWidth("WANTED"); // 描画するテキストの幅を取得
        int x = (imageWidth1 - textWidth) / 2; // 中央に揃えるためのx座標を計算
        g2d.drawString("WANTED", x,  23);
        g2d.drawLine(x, 4, x+textWidth, 4);
        g2d.drawLine(x, 25, x+textWidth, 25);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        int imageWidth2 = newImage.getWidth();
        int textWidth1 = g2d.getFontMetrics().stringWidth(text); // 描画するテキストの幅を取得
        x = (imageWidth2 - textWidth1) / 2; // 中央に揃えるためのx座標を計算
        g2d.drawString(text, x,  35);
        g2d.drawLine(x, 37, x+textWidth1, 37);
        g2d.dispose();
        return newImage;
    }
}
