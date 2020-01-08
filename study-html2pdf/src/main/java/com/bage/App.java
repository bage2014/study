package com.bage;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        boolean success = HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml("<p><em>Apples</em>, not oranges</p>"))
                .convert("/path/to/file.pdf");
        System.out.println("Hello World!");
    }
}
