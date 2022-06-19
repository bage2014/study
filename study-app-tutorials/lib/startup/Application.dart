import 'package:tutorials/component/cache/http_request_caches.dart';
import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:flutter/cupertino.dart';

class Application {
  /**
   * 应用初始化
   */
  static void init(BuildContext context) {
    HttpRequestCaches.init();
    AppUtils.getDeviceId()
        .then((deviceId) => {UserCaches.cacheUserId(deviceId.hashCode)});
    HttpRequests.init();
  }
}
