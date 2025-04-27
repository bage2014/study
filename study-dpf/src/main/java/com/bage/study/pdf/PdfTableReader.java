package com.bage.study.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;

public class PdfTableReader {
    public static void main(String[] args) {
        try {
            PDDocument document = PDDocument.load(new File("/Users/bage/Downloads/STR_Form.pdf"));
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println(text);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}