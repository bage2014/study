package com.bage;

import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        boolean success = HtmlToPdf.create()
//                .object(HtmlToPdfObject.forHtml("<p><em>Apples</em>, not oranges</p>"))
//                .convert("C:\\Users\\bage\\Desktop\\file.pdf");

        boolean success = HtmlToPdf.create()
                .object(HtmlToPdfObject.forUrl("https://github.com/wooio/htmltopdf-java"))
                .convert("C:\\Users\\bage\\Desktop\\file.pdf");

    }
}
