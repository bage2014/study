import 'package:flutter/cupertino.dart';
import 'package:app_lu_lu/component/cache/HttpRequestCaches.dart';
import 'package:app_lu_lu/component/http/HttpRequests.dart';

class Application {
  /**
   * 应用初始化
   */
  static void init(BuildContext context) {
    HttpRequestCaches.init();
  }
}
