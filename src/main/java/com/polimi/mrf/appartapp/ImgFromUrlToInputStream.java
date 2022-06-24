package com.polimi.mrf.appartapp;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImgFromUrlToInputStream {
    private String url;

    public ImgFromUrlToInputStream(String url) {
        this.url=url;
    }

    public InputStream getInputStream() throws MalformedURLException, IOException {
        URLConnection openConnection = new URL(this.url).openConnection();
        openConnection.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        openConnection.connect();
        return openConnection.getInputStream();
    }

    public byte[] getImageAndTypeFromInputStream() throws MalformedURLException, IOException {

        String format = null;
        BufferedImage bufferedimage = null;
        InputStream input = null;

        URLConnection openConnection = new URL(this.url).openConnection();
        openConnection.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

        input = openConnection.getInputStream();
        BufferedInputStream in=new BufferedInputStream(input);

        ImageInputStream stream=ImageIO.createImageInputStream(in);

        Iterator readers=ImageIO.getImageReaders(stream);

        if (readers.hasNext()) {
            ImageReader reader = (ImageReader) readers.next();
            format = reader.getFormatName();
            reader.setInput(stream);
            bufferedimage = reader.read(0);

            new BufferedImageWrapper(format, bufferedimage);

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedimage, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            throw new MalformedURLException();
        }

    }
}
