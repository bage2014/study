package com.bage.study.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;


import java.io.File;
import java.io.IOException;
 
public class PDFReader {
    public static void main(String[] args) {

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile("yourfile.pdf")))
        {
            for (PDPage page : document.getPages())
            {

            }
        }

        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfReader("/Users/bage/Downloads/STR_Form.pdf"));
            ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            String text = PdfTextExtractor.getTextFromPage(pdfDoc, strategy); // 提取第一页的文本
            System.out.println(text);
            pdfDoc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (PDDocument document = PDDocument.load(new File("/Users/bage/Downloads/STR_Form.pdf"))) {
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            String text = pdfStripper.getText(document);
//            System.out.println(text);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}