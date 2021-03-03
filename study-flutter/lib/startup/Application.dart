import 'package:flutter/cupertino.dart';
import 'package:flutter_study/component/cache/HttpRequestCaches.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/prop/HttpProp.dart';

class Application {
  /**
   * 应用初始化
   */
  static void init(BuildContext context) {
    HttpRequestCaches.init();
    HttpRequests.init();
  }
}
