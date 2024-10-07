import 'dart:io';

import 'package:flutter/widgets.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:connectivity/connectivity.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:device_info/device_info.dart';
import 'package:tutorials/utils/log_utils.dart';

class AppUtils {
  static Future<Null> exitApp() async {
    SystemChannels.platform.invokeMethod('SystemNavigator.pop');
  }

  static void pop(BuildContext context,dynamic random) async {
    if (Navigator.canPop(context)) {
      Navigator.pop(context, random);
    } else {
      SystemNavigator.pop();
    }
  }

  static Future<String> getDeviceId() async {
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
      Logs.info('android Running on ${androidInfo.model}'); // e.g. "Moto G (4)"
      Logs.info('android id ${androidInfo.id}'); //
      Logs.info('android id ${androidInfo.androidId}'); //
      return androidInfo.id;
    }
    IosDeviceInfo iosInfo = await deviceInfo.iosInfo;
    Logs.info('ios Running on ${iosInfo.utsname.machine}'); // e.g. "iPod7,1"
    Logs.info('ios id ${iosInfo.identifierForVendor}'); // e.g. "iPod7,1"
    Logs.info('ios name ${iosInfo.name}'); // e.g. "iPod7,1"
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
    return "com.bage.flutter.tutorials";
  }

  static void toPage(
    BuildContext context,
    String route, {
    Object? args,
  }) {
    Navigator.of(context).pushNamed(route, arguments: args);
  }

  static Future toPageWithResult(
    BuildContext context,
    String route, {
    Object? args,
  }) {
    return Navigator.of(context).pushNamed(route, arguments: args);
  }

  static Object? getArgs(BuildContext context) {
    return ModalRoute.of(context)?.settings?.arguments;
  }
  static void back(BuildContext context) {
    Navigator.of(context).pop();
  }
}
