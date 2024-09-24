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

import 'model/profile/ProfileAcitvityRequest.dart';
import 'origin/profile_activity_result.dart';

class ProfileActivityRequests {


  static Future<ProfileActivityResult> query(
      ProfileAcitvityRequest requestParam) async {
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(
        HttpRequests.get(HttpConstant.url_profile_activity, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return ProfileActivityResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static ProfileActivityResult mock() {
    String mock = "{\n" +
        "    \"code\": 200,\n" +
        "    \"originCode\": 0,\n" +
        "    \"msg\": \"success\",\n" +
        "    \"originMsg\": null,\n" +
        "    \"data\": {\n" +
        "        \"profile\": {\n" +
        "            \"id\": 123,\n" +
        "            \"name\": \"小陆\",\n" +
        "            \"location\": \"\",\n" +
        "            \"starCounts\": 123,\n" +
        "            \"followerCounts\": 1222,\n" +
        "            \"feedbackCounts\": 34,\n" +
        "            \"desc\": \"看了这组照片,网友们也明白了,为什么很多大牌商品喜欢请神仙姐姐去做代言了,因为她只要站在那里,隔着屏幕都能感受到满满的高级感。 别看这次出游,刘亦菲表现得十分欢脱,吃了很多美食...\"\n" +
        "        },\n" +
        "        \"activities\": [\n" +
        "            {\n" +
        "                \"id\": 123,\n" +
        "                \"desc\": \"哈哈哈哈，今天天气！非常好！！\",\n" +
        "                \"imageUrls\": [\n" +
        "                    \"https://avatars.githubusercontent.com/u/18094768?v=4\"\n" +
        "                ]\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "}";
    Logs.info('request result mock : ${mock}');
    return ProfileActivityResult.fromJson(jsonDecode(mock));

  }

}
