package com.raphau.springboot.stockExchange.interceptor;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.*;

public class CacheBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] cacheBody;

    public CacheBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cacheBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CacheBodyServletInputStream(this.cacheBody);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cacheBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }
}