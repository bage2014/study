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

class TVRequests {


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
        "            \"name\": \"CCTV1-综合\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-1/.m3u8\",\n" +
        "                \"http://117.169.120.140:8080/live/cctv-2/.m3u8\",\n" +
        "                \"http://117.169.120.140:8080/live/cctv-3/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 101,\n" +
        "            \"name\": \"CCTV2-财经\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-2/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 102,\n" +
        "            \"name\": \"CCTV3-综艺\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-3/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 103,\n" +
        "            \"name\": \"CCTV4-中文国际\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-4/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 104,\n" +
        "            \"name\": \"CCTV5-体育\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-5/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 105,\n" +
        "            \"name\": \"CCTV6-电影\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-6/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 106,\n" +
        "            \"name\": \"CCTV7-国防军事\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-7/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 107,\n" +
        "            \"name\": \"CCTV8-电视剧\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-8/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 108,\n" +
        "            \"name\": \"CCTV9-纪录\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-9/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 109,\n" +
        "            \"name\": \"CCTV10-科教\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://117.169.120.140:8080/live/cctv-10/.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 300,\n" +
        "            \"name\": \"上海东方卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://ivi.bupt.edu.cn/hls/dfhd.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 301,\n" +
        "            \"name\": \"东南卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://ivi.bupt.edu.cn/hls/dntv.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 302,\n" +
        "            \"name\": \"云南卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://ivi.bupt.edu.cn/hls/yntv.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 303,\n" +
        "            \"name\": \"北京卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://ivi.bupt.edu.cn/hls/btv1hd.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 304,\n" +
        "            \"name\": \"吉林卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.76/PLTV/88888888/224/3221225637/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 305,\n" +
        "            \"name\": \"四川卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225621/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 306,\n" +
        "            \"name\": \"天津卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225648/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 307,\n" +
        "            \"name\": \"宁夏卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.76/PLTV/88888888/224/3221225632/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 308,\n" +
        "            \"name\": \"安徽卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225631/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 309,\n" +
        "            \"name\": \"山东卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225650/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 310,\n" +
        "            \"name\": \"山西卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225624/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 311,\n" +
        "            \"name\": \"广东卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225624/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 312,\n" +
        "            \"name\": \"广西卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225622/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 313,\n" +
        "            \"name\": \"江苏卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.76/PLTV/88888888/224/3221225613/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 314,\n" +
        "            \"name\": \"河北卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225615/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 315,\n" +
        "            \"name\": \"河南卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://112.17.40.140/PLTV/88888888/224/3221226130/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 316,\n" +
        "            \"name\": \"浙江卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225612/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 317,\n" +
        "            \"name\": \"湖北卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://112.17.40.140/PLTV/88888888/224/3221226726/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 318,\n" +
        "            \"name\": \"甘肃卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.76/PLTV/88888888/224/3221225633/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 319,\n" +
        "            \"name\": \"贵州卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225626/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 320,\n" +
        "            \"name\": \"辽宁卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225619/index.m3u8\"\n" +
        "            ]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 321,\n" +
        "            \"name\": \"重庆卫视\",\n" +
        "            \"logo\": \"XXX\",\n" +
        "            \"isFavorite\": false,\n" +
        "            \"favoriteId\": null,\n" +
        "            \"userId\": null,\n" +
        "            \"urls\": [\n" +
        "                \"http://111.40.205.87/PLTV/88888888/224/3221225618/index.m3u8\"\n" +
        "            ]\n" +
        "        }\n" +
        "    ],\n" +
        "    \"pagination\": {\n" +
        "        \"targetPage\": 1,\n" +
        "        \"pageSize\": 10,\n" +
        "        \"total\": 32\n" +
        "    }\n" +
        "}";
    return TvQueryRequestResult.fromJson(jsonDecode(mock));

  }

}
