import 'dart:io';

import 'package:connectivity/connectivity.dart';
import 'package:permission_handler/permission_handler.dart';

class AppUtils {

  static Future<bool> isConnected() async {
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.mobile) {
      // I am connected to a mobile network.
      return true;
    } else if (connectivityResult == ConnectivityResult.wifi) {
      // I am connected to a wifi network.
      return true;
    }
    return false;
  }

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
