import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/common/default_page_query_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';
import 'package:tutorials/request/model/feedback/message_feedback_delete_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_delete_request_result.dart';
import 'package:tutorials/request/model/feedback/message_feedback_insert_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_insert_request_result.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_result.dart';
import 'package:tutorials/request/model/tv/tv_query_request_param.dart';
import 'package:tutorials/request/model/tv/tv_query_request_result.dart';
import 'package:tutorials/utils/app_utils.dart';

import 'model/common/default_query_request_param.dart';
import 'origin/author_query_result.dart';

class AuthorQueryRequests {


  static Future<AuthorQueryResult> query(
      DefaultQueryRequestParam requestParam) async {
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(
        HttpRequests.get(HttpConstant.url_tv_query_page, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return AuthorQueryResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static AuthorQueryResult mock() {
    String mock = "{\n" +
        "    \"data\": {\n" +
        "        \"id\": 123,\n" +
        "        \"firstName\": \"陆\",\n" +
        "        \"lastName\": \"八哥\",\n" +
        "        \"iconUrl\": \"https://avatars.githubusercontent.com/u/18094768?v\\u003d4\",\n" +
        "        \"homePageUrl\": \"https://github.com/bage2014\",\n" +
        "        \"description\": \"上海Java 服务端研发\",\n" +
        "        \"email\": \"893542907@qq.com\"\n" +
        "    },\n" +
        "    \"code\": 200,\n" +
        "    \"originCode\": 0,\n" +
        "    \"msg\": \"success\",\n" +
        "    \"originMsg\": null\n" +
        "}";
    Logs.info('request result mock : ${mock}');
    return AuthorQueryResult.fromJson(jsonDecode(mock));

  }

}
