package com.bage.study.java.spi;

import java.util.List;

/**
 * https://pdai.tech/md/interview/x-interview.html#_1-6-spi%E6%9C%BA%E5%88%B6
 */
public interface Search {
    public List<String> searchDoc(String keyword);
}