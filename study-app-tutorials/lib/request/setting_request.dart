import 'dart:collection';

import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_param.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_result.dart';

class SettingRequests {
  static Future<AppVersionCheckRequestResult> checkVersion(
      AppVersionCheckRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');

    // String url = HttpConstant.url_settings_version_check
    //     .replaceAll("{version}", AppUtils.getCurrentVersion().toString());
    // HttpResult httpResult = await HttpRequests.get(url, null, null);
    // String? temp = httpResult?.responseBody;
    // String responseBody = temp == null ? "" : temp;
    // Logs.info("responseBody = ${responseBody}");
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 2), () => mock());
  }

  static AppVersionCheckRequestResult mock() {
    AppVersionCheckRequestResult result = AppVersionCheckRequestResult();
    result.common.code = 200;
    // result.common.code = ErrorCodeConstant.loginSecurityCodeRequired;
    result.common.message = "404啦啦啦";
    result.id = 12345;
    result.versionCode = 2;
    result.description = '版本信息\n'
        '说明1\n'
        '说明2\n'
        '哈哈哈哈哈哈哈';
    result.fileId = 12345;
    result.fileUrl =
        "https://github.com/bage2014/study/blob/master/study-app-tutorials/apks/app-release.apk";
    result.versionName = "Beta";
    result.fileSize = 123;
    result.updateType = "Default";
    Logs.info('request result : ${result?.toString()}');
    return result;
  }
}
