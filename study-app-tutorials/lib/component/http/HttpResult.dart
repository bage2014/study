import 'dart:collection';

class HttpResult {
  int statusCode = 0;
  String responseBody = "";
  Map<String, List<String>> headers = HashMap();
}
