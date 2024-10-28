import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/app_versions_result.dart';
import 'package:tutorials/request/model/common/default_page_query_request_param.dart';

class AppVersionsRequests {
  static Future<AppVersionsResult> getVersions(
      DefaultPageQueryRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }
    String url = HttpConstant.url_settings_app_versions;

    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = new HashMap();
    // param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(HttpRequests.get(url, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return AppVersionsResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static AppVersionsResult mock() {
    String json = '{"pagination":{"targetPage":1,"pageSize":10,"total":2},"data":[{"id":123,"versionCode":1,"description":"说明1\\n说明2\\n说明3\\n说明4\\n","fileId":123,"fileUrl":"https://raw.githubusercontent.com/bage2014/study/master/study-app-tutorials/apks/app-release.apk","versionName":"V1.0","fileSize":123,"updateType":null,"createTime":"2024-10-22T08:44:43.945","updateTime":"2024-10-22T08:44:43.945","createStaffId":null,"updateStaffId":null,"deleteState":null},{"id":123,"versionCode":1,"description":"说明11\\n说明22\\n说明33\\n","fileId":123,"fileUrl":"https://raw.githubusercontent.com/bage2014/study/master/study-app-tutorials/apks/app-release.apk","versionName":"V1.0","fileSize":123,"updateType":null,"createTime":"2024-10-22T08:44:43.945","updateTime":"2024-10-22T08:44:43.945","createStaffId":null,"updateStaffId":null,"deleteState":null}],"code":200,"originCode":0,"msg":"success","originMsg":null}';
    AppVersionsResult result = AppVersionsResult.fromJson(jsonDecode(json));
    Logs.info('request mock result : ${result?.toString()}');
    return result;
  }
}
