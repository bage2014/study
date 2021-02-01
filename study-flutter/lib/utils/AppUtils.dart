import 'package:permission_handler/permission_handler.dart';

class AppUtils {
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
