import 'package:app_lu_lu/component/cache/HttpRequestCaches.dart';
import 'package:app_lu_lu/component/cache/UserCaches.dart';
import 'package:app_lu_lu/component/http/HttpRequests.dart';
import 'package:app_lu_lu/utils/AppUtils.dart';
import 'package:flutter/cupertino.dart';

class Application {
  /**
   * 应用初始化
   */
  static void init(BuildContext context) {
    HttpRequestCaches.init();
    AppUtils.getDeviceId()
        .then((deviceId) => {UserCaches.setUserId(deviceId.hashCode)});
    HttpRequests.init();
  }
}
