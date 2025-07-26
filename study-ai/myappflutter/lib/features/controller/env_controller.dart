import 'package:get/get.dart';
import '../../core/constants/constants.dart';

class EnvController extends GetxController {
  // 默认生产环境
  final RxString currentEnv = 'prod'.obs;

  // 切换环境
  void changeEnv(String env) {
    currentEnv.value = env;
    update();
  }

  // 获取当前环境对应的baseUrl
  String getBaseUrl() {
    switch (currentEnv.value) {
      case 'dev':
        return Constants.devBaseUrl;
      case 'mock':
        return ''; // Mock环境不需要baseUrl
      default:
        return Constants.prodBaseUrl;
    }
  }
}
