import 'package:tutorials/component/cache/HttpRequestCaches.dart';

class HttpProp {
  static String getBaseUrl() {
    return HttpRequestCaches.getProtocol() +
        "://" +
        HttpRequestCaches.getHost() +
        ":" +
        HttpRequestCaches.getPort() +
        "/tutorials";
  }

  static int getTimeout() {
    return 5000;
  }

  static String getContentType() {
    return "application/json; charset=utf-8";
  }
}
