package com.polimi.mrf.appartapp;

import java.awt.image.BufferedImage;

public class BufferedImageWrapper {
    private final String imageType;
    private final BufferedImage bufferedimage;

    public BufferedImageWrapper(String imageType, BufferedImage bufferedimage) {
        System.out.println("in Buffered image Wrapper");
        this.imageType = imageType;
        this.bufferedimage = bufferedimage;
    }

    public String getImageType() {
        return imageType;
    }

    public BufferedImage getBufferedimage() {
        return bufferedimage;
    }
}