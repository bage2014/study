import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
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

class ProfileRequests {


  static Future<TvQueryRequestResult> query(
      TvQueryRequestParam requestParam) async {
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(
        HttpRequests.get(HttpConstant.url_tv_query_page, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return TvQueryRequestResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static TvQueryRequestResult mock() {
    String mock = "{\n" +
        "    \"code\": 200,\n" +
        "    \"originCode\": 0,\n" +
        "    \"msg\": \"success\",\n" +
        "    \"originMsg\": null,\n" +
        "    \"data\": [\n" +
        "        {\n" +
        "            \"id\": 100,\n" +
        "            \"name\": \"BBC_News\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"https://cdn4.skygo.mn/live/disk1/BBC_News/HLSv3-FTA/BBC_News.m3u8\"\n" +
        "            ]\n" +
        "        }" +
        "    ],\n" +
        "    \"pagination\": {\n" +
        "        \"targetPage\": 1,\n" +
        "        \"pageSize\": 10,\n" +
        "        \"total\": 32\n" +
        "    }\n" +
        "}";
    Logs.info('request result mock : ${mock}');
    return TvQueryRequestResult.fromJson(jsonDecode(mock));

  }

}
