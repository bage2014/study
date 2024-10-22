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
    String json = "{\n" +
        "    \"pagination\": {\n" +
        "        \"targetPage\": 1,\n" +
        "        \"pageSize\": 10,\n" +
        "        \"total\": 2\n" +
        "    },\n" +
        "    \"data\": [\n" +
        "        {\n" +
        "            \"id\": 405367500653416,\n" +
        "            \"versionCode\": 203393314,\n" +
        "            \"description\": \"版本信息\\n'\\n说明1\\n说明2\\n 哈哈哈哈哈哈哈\",\n" +
        "            \"fileId\": 405367499731375,\n" +
        "            \"fileUrl\": \"https://raw.githubusercontent.com/bage2014/study/master/study-app-tutorials/apks/app-release.apk\",\n" +
        "            \"versionName\": \"N4eFWsYp8D\",\n" +
        "            \"fileSize\": 405367499968666,\n" +
        "            \"updateType\": \"FORCE_UPDATE\",\n" +
        "            \"createTime\": \"2024-10-21T23:26:11.994942\",\n" +
        "            \"updateTime\": \"2024-10-21T23:26:11.994942\",\n" +
        "            \"createStaffId\": \"dUvxVaNTQi\",\n" +
        "            \"updateStaffId\": \"3M8mZZWh9C\",\n" +
        "            \"deleteState\": \"zmTJ4Syu15\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 405367500653416,\n" +
        "            \"versionCode\": 203393314,\n" +
        "            \"description\": \"版本信息\\n说明11\\n说明22\\n哈哈哈哈哈哈哈\",\n" +
        "            \"fileId\": 405367499731375,\n" +
        "            \"fileUrl\": \"https://raw.githubusercontent.com/bage2014/study/master/study-app-tutorials/apks/app-release.apk\",\n" +
        "            \"versionName\": \"N4eFWsYp8D\",\n" +
        "            \"fileSize\": 405367499968666,\n" +
        "            \"updateType\": \"FORCE_UPDATE\",\n" +
        "            \"createTime\": \"2024-10-21T23:30:07.439\",\n" +
        "            \"updateTime\": \"2024-10-21T23:26:11.994942\",\n" +
        "            \"createStaffId\": \"dUvxVaNTQi\",\n" +
        "            \"updateStaffId\": \"3M8mZZWh9C\",\n" +
        "            \"deleteState\": \"zmTJ4Syu15\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"code\": 200,\n" +
        "    \"originCode\": 0,\n" +
        "    \"msg\": \"success\",\n" +
        "    \"originMsg\": null\n" +
        "}";
    AppVersionsResult result = AppVersionsResult.fromJson(jsonDecode(json));
    Logs.info('request mock result : ${result?.toString()}');
    return result;
  }
}
