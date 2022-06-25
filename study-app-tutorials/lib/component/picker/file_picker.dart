import 'dart:io';

import 'package:file_picker/file_picker.dart' as origin_file_picker;

/// https://pub.flutter-io.cn/packages/file_picker
class FilePicker {
  static Future<String?> pickDirectory() async {
    return await origin_file_picker.FilePicker.platform.getDirectoryPath();
  }
}
