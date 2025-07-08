import 'package:get/get.dart';
import 'en.dart';
import 'zh.dart';

class MyTranslations extends Translations {
  @override
  Map<String, Map<String, String>> get keys => {
        'zh_CN': zh,
        'en_US': en,
      };
}