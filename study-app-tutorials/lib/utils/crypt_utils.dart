import 'dart:convert';

import 'dart:typed_data';

class CryptUtils {
  static String encode(String text) {
    return base64Encode(utf8.encode(text));
  }

  static String decode(String text) {
    Uint8List bytes = base64Decode(text);
    return String.fromCharCodes(bytes);
  }
}
