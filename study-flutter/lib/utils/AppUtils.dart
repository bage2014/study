import 'dart:io';

import 'package:open_file/open_file.dart';
import 'package:permission_handler/permission_handler.dart';

class AppUtils {

  static void openFile(File file) {
    OpenFile.open(file.path)
        .then((value) => print('openFile then value = ${value.message}'))
        .catchError((error) => {print('openFile catchError error = $error')});
  }

  static Future<bool> openSettings() {
    return openAppSettings();
  }

  static String getDeviceId() {
    return "";
  }

  static String getPackageId() {
    return "com.bage.flutter.flutter_study";
  }

}
