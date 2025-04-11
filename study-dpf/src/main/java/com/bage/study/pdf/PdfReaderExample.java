package com.bage.study.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class PdfReaderExample {
    public static void main(String[] args) {
        File file = new File("/Users/bage/Downloads/STR_Form.pdf");
        try (PDDocument document = PDDocument.load(file)) {
            // 检查文档是否加密
            if (document.isEncrypted()) {
                System.err.println("文档已加密，无法读取");
                return;
            }
            
            // 创建PDFTextStripper对象提取文本
            PDFTextStripper stripper = new PDFTextStripper();
            
            // 设置是否排序文本
            stripper.setSortByPosition(true);
            
            // 获取全部文本
            String text = stripper.getText(document);
            System.out.println(text);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}