package com.bage;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String args[]) throws IOException {
        // IO
        try {
            generatePDFFromHTML("E:\\workspace\\untitled\\study\\study-itext\\src\\main\\java\\com\\bage\\hello.html");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void generatePDFFromHTML(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream("E:\\workspace\\untitled\\study\\study-itext\\src\\main\\java\\com\\bage\\hello.pdf"));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(filename));
        document.close();
    }

}
