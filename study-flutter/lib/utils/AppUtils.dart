import 'dart:io';

import 'package:open_file/open_file.dart';
import 'package:permission_handler/permission_handler.dart';

class AppUtils {
  static int getCurrentVersion() {
    return 0;
  }

  static Future<bool> openSettings() {
    return openAppSettings();
  }

  static String getDeviceId() {
    return "";
  }

  static String getPackageId() {
    return "com.bage.flutter.app_lu_lu";
  }
}
