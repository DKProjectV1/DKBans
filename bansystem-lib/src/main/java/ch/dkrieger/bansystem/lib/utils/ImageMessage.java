/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.lib.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageMessage {

    private final Color[] colors = {
            new Color(0, 0, 0),
            new Color(0, 0, 170),
            new Color(0, 170, 0),
            new Color(0, 170, 170),
            new Color(170, 0, 0),
            new Color(170, 0, 170),
            new Color(255, 170, 0),
            new Color(170, 170, 170),
            new Color(85, 85, 85),
            new Color(85, 85, 255),
            new Color(85, 255, 85),
            new Color(85, 255, 255),
            new Color(255, 85, 85),
            new Color(255, 85, 255),
            new Color(255, 255, 85),
            new Color(255, 255, 255) };
    private String[] lines;

    public ImageMessage(BufferedImage image, int height, char imgChar) {
        ChatColor[][] chatColors = toChatColorArray(image, height);
        this.lines = toImgMessage(chatColors, imgChar);
    }
    public ImageMessage(ChatColor[][] chatColors, char imgChar) {
        this.lines = toImgMessage(chatColors, imgChar);
    }
    public ImageMessage(String... imgLines) {
        this.lines = imgLines;
    }
    public ImageMessage appendText(String... text) {
        for(int y = 0; y < this.lines.length; y++) {
            if(text.length > y) {
                int tmp16_15 = y;
                String[] tmp16_12 = this.lines;
                tmp16_12[tmp16_15] = (tmp16_12[tmp16_15]+" "+text[y]);
            }
        }
        return this;
    }
    private ChatColor[][] toChatColorArray(BufferedImage image, int height) {
        double ratio = image.getHeight() / image.getWidth();
        int width = (int)(height / ratio);
        if(width > 10) width = 10;
        BufferedImage resized = resizeImage(image, (int)(height / ratio), height);
        ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for(int x = 0; x < resized.getWidth(); x++) {
            for(int y = 0; y < resized.getHeight(); y++) {
                int rgb = resized.getRGB(x, y);
                ChatColor closest = getClosestChatColor(new Color(rgb, true));
                chatImg[x][y] = closest;
            }
        }
        return chatImg;
    }
    private String[] toImgMessage(ChatColor[][] colors, char imgchar) {
        String[] lines = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; y++) {
            String line = "";
            for (int x = 0; x < colors.length; x++) {
                ChatColor color = colors[x][y];
                line = line + (color != null ? colors[x][y].toString() + imgchar : Character.valueOf(' '));
            }
            lines[y] = (line + ChatColor.RESET);
        }
        return lines;
    }
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        AffineTransform af = new AffineTransform();
        af.scale(width / originalImage.getWidth(),
                height / originalImage.getHeight());
        AffineTransformOp operation = new AffineTransformOp(af, 1);
        return operation.filter(originalImage, null);
    }
    private double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0D;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2.0D + rmean / 256.0D;
        double weightG = 4.0D;
        double weightB = 2.0D + (255.0D - rmean) / 256.0D;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }
    private boolean areIdentical(Color c1, Color c2) {
        return (Math.abs(c1.getRed() - c2.getRed()) <= 5) &&
                (Math.abs(c1.getGreen() - c2.getGreen()) <= 5) &&
                (Math.abs(c1.getBlue() - c2.getBlue()) <= 5);
    }
    private ChatColor getClosestChatColor(Color color) {
        if(color.getAlpha() < 128) return null;
        int index = 0;
        double best = -1.0D;
        for(int i = 0; i < this.colors.length; i++) if(areIdentical(this.colors[i], color)) return ChatColor.values()[i];
        for(int i = 0; i < this.colors.length; i++){
            double distance = getDistance(color, this.colors[i]);
            if((distance < best) || (best == -1.0D)){
                best = distance;
                index = i;
            }
        }
        return ChatColor.values()[index];
    }
    public String[] getLines() {
        return this.lines;
    }
}
