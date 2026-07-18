package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {

    @Test
    void builder_ShouldCreatePageResult() {
        List<String> items = List.of(\