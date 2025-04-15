package com.bage.study.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OpenPdfExample {
    public static void main(String[] args) {
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream("/Users/bage/Downloads/STR_Form.pdf"));
            document.open();

            String language = document.getDocumentLanguage();
//            document.add(new Paragraph("Hello, OpenPDF!"));
            document.close();
            System.out.println("PDF created successfully!");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}