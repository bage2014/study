import 'dart:collection';

class HttpByteResult {
  int statusCode = 0;
  var responseBytes;
  Map<String, List<String>> headers = HashMap();
}
