package com.bage.study.spring.boot.html;

public class HtmlUtils {
    public static void main(String[] args) {
        // HTML转义
        String html = "<div>Hello, World!</div>";
        String escapedHtml = org.springframework.web.util.HtmlUtils.htmlEscape(html);
        System.out.println("Escaped HTML: " + escapedHtml);

        // HTML反转义
        String unescapedHtml = org.springframework.web.util.HtmlUtils.htmlUnescape(escapedHtml);
        System.out.println("Unescaped HTML: " + unescapedHtml);

        // URL编码
        String url = "http://example.com/?q=hello world";
        String encodedUrl = org.springframework.web.util.UriUtils.encode(url, "UTF-8");
        System.out.println("Encoded URL: " + encodedUrl);

        // URL解码
        String decodedUrl = org.springframework.web.util.UriUtils.decode(encodedUrl, "UTF-8");
        System.out.println("Decoded URL: " + decodedUrl);
    }
}
