package com.bage;

import org.springframework.stereotype.Component;

@Component
public class FileServiceImpl implements FileService {
    public String hello() {
        return "FileServiceImpl";
    }
}
