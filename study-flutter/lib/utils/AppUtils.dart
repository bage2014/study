import 'dart:io';

import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:connectivity/connectivity.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:device_info/device_info.dart';

class AppUtils {
  static Future<Null> exitApp() async {
    SystemChannels.platform.invokeMethod('SystemNavigator.pop');
  }

  static Future<String> getDeviceId() async {
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
      print('android Running on ${androidInfo.model}'); // e.g. "Moto G (4)"
      print('android id ${androidInfo.id}'); //
      print('android id ${androidInfo.androidId}'); //
      return androidInfo.id;
    }
    IosDeviceInfo iosInfo = await deviceInfo.iosInfo;
    print('ios Running on ${iosInfo.utsname.machine}'); // e.g. "iPod7,1"
    print('ios id ${iosInfo.identifierForVendor}'); // e.g. "iPod7,1"
    print('ios name ${iosInfo.name}'); // e.g. "iPod7,1"
    return iosInfo.identifierForVendor;
  }

  static Future<bool> isConnected() async {
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.mobile) {
      // I am connected to a mobile network.
      Logs.info('I am connected to a mobile network.');
      return true;
    } else if (connectivityResult == ConnectivityResult.wifi) {
      // I am connected to a wifi network.
      Logs.info('I am connected to a wifi network.');
      return true;
    }
    Logs.info('I am not connect to a network.');
    return false;
  }

  static int getCurrentVersion() {
    return 0;
  }

  static Future<bool> openSettings() {
    return openAppSettings();
  }

  static String getPackageId() {
    return "com.bage.flutter.app_lu_lu";
  }
}
