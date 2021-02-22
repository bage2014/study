import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/prop/HttpProp.dart';

class Application {
  /**
   * 应用初始化
   */
  static void init() {
    HttpRequests.init();
    HttpProp.init();
    EasyLoading.init();
  }
}
