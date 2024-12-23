package com.raphau.springboot.stockExchange.interceptor;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CacheBodyServletInputStream extends ServletInputStream {

    private final InputStream cacheBodyInputStream;

    public CacheBodyServletInputStream(byte[] cacheBody) {
        this.cacheBodyInputStream = new ByteArrayInputStream(cacheBody);
    }

    @Override
    public boolean isFinished() {
        try {
            return cacheBodyInputStream.available() == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return cacheBodyInputStream.read();
    }
}