import 'dart:collection';

class HttpResult {
  int statusCode;
  String responseBody;
  Map<String, List<String>> headers = HashMap();
}
