package com.nulstudio.hit_b02_340.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public final class QRCodeGenerator {
    ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
    String content = "";
    int width = 800;
    int height = 800;
    int margin = 0;
    String charset = "UTF-8";
    int back, front;

    public QRCodeGenerator() {

    }

    public QRCodeGenerator setErrorCorrectionLevel(ErrorCorrectionLevel level) {
        this.level = level;
        return this;
    }

    public QRCodeGenerator setContent(String content) {
        this.content = content;
        return this;
    }

    public QRCodeGenerator setWidth(int width) {
        if(width > 0)
            this.width = width;
        return this;
    }

    public QRCodeGenerator setHeight(int height) {
        if(height > 0)
            this.height = height;
        return this;
    }

    public QRCodeGenerator setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public QRCodeGenerator setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public QRCodeGenerator setBackColor(int color) {
        this.back = color;
        return this;
    }

    public QRCodeGenerator setFrontColor(int color) {
        this.front = color;
        return this;
    }

    public Bitmap create() throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        hints.put(EncodeHintType.ERROR_CORRECTION, level);
        hints.put(EncodeHintType.MARGIN, margin);

        BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);

        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = front;
                } else {
                    pixels[y * width + x] = back;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
