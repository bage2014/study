package com.bage.study.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class PdfPageReader {
    public static void main(String[] args) {
        File file = new File("/Users/bage/Downloads/STR_Form.pdf");
        
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            
            // 获取总页数
            int pageCount = document.getNumberOfPages();
            System.out.println("总页数: " + pageCount);
            
            // 逐页读取
            for (int i = 1; i <= pageCount; i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String pageText = stripper.getText(document);
                System.out.println("===== 第 " + i + " 页内容 =====");
                System.out.println(pageText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}