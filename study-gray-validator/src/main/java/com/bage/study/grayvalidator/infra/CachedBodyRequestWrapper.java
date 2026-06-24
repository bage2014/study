package com.bage.study.grayvalidator.infra;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CachedBodyRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public CachedBodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.cachedBody = request.getInputStream().readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream stream = new ByteArrayInputStream(cachedBody);
        return new ServletInputStream() {
            @Override public int     read()                            { return stream.read(); }
            @Override public boolean isFinished()                     { return stream.available() == 0; }
            @Override public boolean isReady()                        { return true; }
            @Override public void    setReadListener(ReadListener rl) {}
        };
    }

    @Override
    public BufferedReader getReader() {
        Charset charset = charset();
        return new BufferedReader(new InputStreamReader(getInputStream(), charset));
    }

    public byte[] getCachedBody() {
        return cachedBody;
    }

    private Charset charset() {
        String enc = getCharacterEncoding();
        try {
            return enc != null ? Charset.forName(enc) : StandardCharsets.UTF_8;
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }
}