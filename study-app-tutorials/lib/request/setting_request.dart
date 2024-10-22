import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_param.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_result.dart';
import 'package:tutorials/utils/app_utils.dart';

class SettingRequests {
  static Future<AppVersionCheckRequestResult> checkVersion(
      AppVersionCheckRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }
    String url = HttpConstant.url_settings_version_check
        .replaceAll("{version}", AppUtils.getCurrentVersion().toString());

    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = new HashMap();
    // param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(HttpRequests.get(url, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return AppVersionCheckRequestResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static AppVersionCheckRequestResult mock() {
    Data data = Data();
    data.versionCode = 2;
    data.description = '版本信息\n'
        '说明1\n'
        '说明2\n'
        '哈哈哈哈哈哈哈';
    data.fileId = 12345;
    data.fileUrl =
    "https://raw.githubusercontent.com/bage2014/study/master/study-app-tutorials/apks/app-release.apk";
    data.versionName = "Beta";
    data.fileSize = 123;
    data.updateType = "Default";
    AppVersionCheckRequestResult result = AppVersionCheckRequestResult();
    result.code = 200;
    result.msg = "成功";
    result.data = data;

    Logs.info('request mock result : ${result?.toString()}');
    return result;
  }
}
