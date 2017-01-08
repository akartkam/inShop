package com.akartkam.inShop.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;



import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Captcha {
    private final String captchaString;
    private final byte[] captchaJpegBytes;
 
    private Captcha(CaptchaBuilder captchaBuilder) {
        this.captchaString = captchaBuilder.getCaptchaText();
        this.captchaJpegBytes = captchaBuilder.getJpegBytes();
    }
 
    public static Captcha newCaptcha(int width, int height) {
        CaptchaBuilder captchaBuilder = new CaptchaBuilder(width, height);
        return new Captcha(captchaBuilder.build());
    }

	public String getCaptchaString() {
		return captchaString;
	}

	public byte[] getCaptchaJpegBytes() {
		return captchaJpegBytes;
	}
 
 
}
 
class CaptchaBuilder {
    // ========================================================
    // Kick Ass Captcha JSP
    //
    // Michael Connor 2007
    //
    // I just couldn't handle the thought of downloading a
    // big jar and configuring some servlet.xml and having
    // little to no control of anything...
    // You can send in height and width parameters.
    // The captcha value will be placed in the session in
    // a parameter called 'captcha'
    //
    // Feel free to use this code and do whatever the hell
    // you want to it.
    // ========================================================
 
    // captcha constants
    private static final Color textColor = Color.white;
    private static final Color circleColor = new Color(160, 160, 160);
    private static final Font textFont = new Font("Arial", Font.PLAIN, 24);
    private static final int charsToPrint = 4;
 
    private static final int circlesToDraw = 4;
    private static final float horizMargin = 10.0f;
    // max is 1.0 (this is for jpeg)
    private static final float imageQuality = 0.95f;
    // this is radians
    private static final double rotationRange = 0.7;
 
    /*
     * i removed 1 and l and i because there are confusing to users... Z, z, and
     * N also get confusing when rotated 0, O, and o are also confusing...
     * lowercase G looks a lot like a 9 so i killed it this should ideally be
     * done for every language... i like controlling the characters though
     * because it helps prevent confusion
     */
    private static final String chars = "ABCDEFGHJKLMPQRSTUVWXYabcdefhjkmnpqrstuvwxy23456789";
 
    private final int width;
    private final int height;
    private final Random random;
 
    private BufferedImage bufferedImage;
    private Graphics2D graphic;
 
    private String captchaText = "";
    private byte[] jpegBytes = new byte[0];
 
    public CaptchaBuilder(int width, int height) {
        this.width = width;
        this.height = height;
 
        this.random = new Random();
    }
 
    public byte[] getJpegBytes() {
        return jpegBytes;
    }
 
    public String getCaptchaText() {
        return captchaText;
    }
 
    private void init() {
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.graphic = (Graphics2D) bufferedImage.getGraphics();
    }
 
    public CaptchaBuilder build() {
        init();
 
        generateCaptchaText();
 
        drawNoise();
        drawText();
 
        prepateJpegBytes();
 
        return this;
    }
 
    private void prepateJpegBytes() {
        graphic.dispose();
        jpegBytes = convertToBytes(bufferedImage);
    }
 
    private void generateCaptchaText() {
        StringBuilder builder = new StringBuilder(charsToPrint);
        for (int i = 0; i < charsToPrint; i++) {
            int randomIndex = random.nextInt(chars.length());
            builder.append(chars.charAt(randomIndex));
        }
        captchaText = builder.toString();
    }
 
    private void drawNoise() {
        // Draw an oval
        graphic.setColor(Color.DARK_GRAY);
        graphic.fillRect(0, 0, width, height);
 
        // lets make some noisy circles
        graphic.setColor(circleColor);
        for (int i = 0; i < circlesToDraw; i++) {
            int circleRadius = random.nextInt(height / 2);
            int circleX = random.nextInt(width) - circleRadius;
            int circleY = random.nextInt(height) - circleRadius;
            graphic.drawOval(circleX, circleY, circleRadius * 2, circleRadius * 2);
        }
    }
 
    private void drawText() {
        graphic.setColor(textColor);
        graphic.setFont(textFont);
 
        FontMetrics fontMetrics = graphic.getFontMetrics();
        int maxAdvance = fontMetrics.getMaxAdvance();
        int fontHeight = fontMetrics.getHeight();
 
        float spaceForLetters = -horizMargin * 2 + width;
        float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);
 
        for (int i = 0; i < charsToPrint; i++) {
            char characterToShow = captchaText.charAt(i);
 
            int charWidth = fontMetrics.charWidth(characterToShow);
            int charDim = Math.max(maxAdvance, fontHeight);
            int halfCharDim = (int) (charDim / 2);
 
            BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
            Graphics2D charGraphics = charImage.createGraphics();
            charGraphics.translate(halfCharDim, halfCharDim);
            double angle = (Math.random() - 0.5) * rotationRange;
            charGraphics.transform(AffineTransform.getRotateInstance(angle));
            charGraphics.translate(-halfCharDim, -halfCharDim);
            charGraphics.setColor(textColor);
            charGraphics.setFont(textFont);
 
            int charX = (int) (0.5 * charDim - 0.5 * charWidth);
            charGraphics.drawString("" + characterToShow, charX,
                    (int) ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));
 
            float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
            int y = (int) ((height - charDim) / 2);
            graphic.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
 
            charGraphics.dispose();
        }
    }
 
    private static byte[] convertToBytes(BufferedImage bufferedImage) {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
 
        ImageWriter writer = getJpegImageWriter();
 
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(imageQuality);
 
        // see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4894964
        ImageOutputStream imageOutputStream = null;
        try {
            imageOutputStream = ImageIO.createImageOutputStream(resultStream);
            writer.setOutput(imageOutputStream);
            IIOImage imageIO = new IIOImage(bufferedImage, null, null);
            writer.write(null, imageIO, iwp);
        } catch (IOException e) {
            throw new RuntimeException("problem occures while writing to temp file");
        } finally {
            if (imageOutputStream != null) {
                try {
                    imageOutputStream.close();
                } catch (IOException e) {
                }
            }
 
            try {
                resultStream.close();
            } catch (IOException e) {
            }
        }
 
        return resultStream.toByteArray();
    }
 
    private static ImageWriter getJpegImageWriter() {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
        if (!iter.hasNext()) {
            throw new RuntimeException("no encoder found");
        }
        return iter.next();
    }
}