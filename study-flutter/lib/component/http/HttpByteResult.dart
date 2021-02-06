import 'dart:collection';

class HttpByteResult {
  int statusCode;
  var responseBytes;
  Map<String, List<String>> headers = HashMap();
}
