import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/common/page_query_request_param.dart';
import 'package:tutorials/request/model/school/school_card_query_request_param.dart';
import 'package:tutorials/request/origin/common_update_result.dart';
import 'package:tutorials/request/origin/school_card_query_result.dart';
import 'package:tutorials/request/origin/school_meta_data_query_result.dart';
import 'package:tutorials/request/origin/subject_meta_data_query_result.dart';

class SchoolSubjectSelectRequests {


  static Future<SchoolMetaDataQueryResult> querySchool(
      CommonPageQueryRequestParam requestParam) async {
    Logs.info('request param : ${json.encode(requestParam?.toString())}');

    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mockSchool());
    }

    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(
        HttpRequests.get(HttpConstant.url_tv_query_page, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return SchoolMetaDataQueryResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static SchoolMetaDataQueryResult mockSchool() {
    String json = "{\n" +
        "  \"code\": 200,\n" +
        "  \"originCode\": 0,\n" +
        "  \"msg\": null,\n" +
        "  \"originMsg\": null,\n" +
        "  \"data\": [\n" +
        "    {\n" +
        "      \"id\": 10000,\n" +
        "      \"name\": \"清华大学\",\n" +
        "      \"tags\": [\n" +
        "        \"双一流\",\n" +
        "        \"985\",\n" +
        "        \"211\"\n" +
        "      ],\n" +
        "      \"imageUrl\": \"https://www.shanghairanking.cn/_uni/logo/27532357.png\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10001,\n" +
        "      \"name\": \"北京大学\",\n" +
        "      \"tags\": [\n" +
        "        \"双一流\",\n" +
        "        \"985\",\n" +
        "        \"211\"\n" +
        "      ],\n" +
        "      \"imageUrl\": \"https://www.shanghairanking.cn/_uni/logo/86350223.png\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10002,\n" +
        "      \"name\": \"浙江大学\",\n" +
        "      \"tags\": [\n" +
        "        \"双一流\",\n" +
        "        \"985\",\n" +
        "        \"211\"\n" +
        "      ],\n" +
        "      \"imageUrl\": \"https://www.shanghairanking.cn/_uni/logo/88311656.png\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10003,\n" +
        "      \"name\": \"上海交通大学\",\n" +
        "      \"tags\": [\n" +
        "        \"双一流\",\n" +
        "        \"985\",\n" +
        "        \"211\"\n" +
        "      ],\n" +
        "      \"imageUrl\": \"https://www.shanghairanking.cn/_uni/logo/27403919.png\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10004,\n" +
        "      \"name\": \"南京大学\",\n" +
        "      \"tags\": [\n" +
        "        \"双一流\",\n" +
        "        \"985\",\n" +
        "        \"211\"\n" +
        "      ],\n" +
        "      \"imageUrl\": \"https://www.shanghairanking.cn/_uni/logo/44696062.png\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"pagination\": {\n" +
        "    \"targetPage\": 0,\n" +
        "    \"pageSize\": 10,\n" +
        "    \"total\": 5\n" +
        "  }\n" +
        "}";
    Logs.info('request result mock : ${json}');
    return SchoolMetaDataQueryResult.fromJson(jsonDecode(json));
  }

  static Future<SubjectMetaDataQueryResult> querySubject(
      CommonPageQueryRequestParam requestParam) async {
    Logs.info('request param : ${json.encode(requestParam?.toString())}');

    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mockSubject());
    }

    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(
        HttpRequests.get(HttpConstant.url_tv_query_page, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return SubjectMetaDataQueryResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static SubjectMetaDataQueryResult mockSubject() {
    String json = "{\n" +
        "  \"code\": 200,\n" +
        "  \"originCode\": 0,\n" +
        "  \"msg\": null,\n" +
        "  \"originMsg\": null,\n" +
        "  \"data\": [\n" +
        "    {\n" +
        "      \"id\": 10000,\n" +
        "      \"schoolId\": 10001,\n" +
        "      \"name\": \"计算机科学与技术\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10001,\n" +
        "      \"schoolId\": 10001,\n" +
        "      \"name\": \"软件工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10002,\n" +
        "      \"schoolId\": 10001,\n" +
        "      \"name\": \"信息安全\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10003,\n" +
        "      \"schoolId\": 10001,\n" +
        "      \"name\": \"网络工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10004,\n" +
        "      \"schoolId\": 10001,\n" +
        "      \"name\": \"物联网工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10005,\n" +
        "      \"schoolId\": 10002,\n" +
        "      \"name\": \"计算机科学与技术\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10006,\n" +
        "      \"schoolId\": 10002,\n" +
        "      \"name\": \"软件工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10007,\n" +
        "      \"schoolId\": 10002,\n" +
        "      \"name\": \"信息安全\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10008,\n" +
        "      \"schoolId\": 10002,\n" +
        "      \"name\": \"网络工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10009,\n" +
        "      \"schoolId\": 10002,\n" +
        "      \"name\": \"物联网工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10010,\n" +
        "      \"schoolId\": 10003,\n" +
        "      \"name\": \"计算机科学与技术\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10011,\n" +
        "      \"schoolId\": 10003,\n" +
        "      \"name\": \"软件工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10012,\n" +
        "      \"schoolId\": 10003,\n" +
        "      \"name\": \"信息安全\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10013,\n" +
        "      \"schoolId\": 10003,\n" +
        "      \"name\": \"网络工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10014,\n" +
        "      \"schoolId\": 10003,\n" +
        "      \"name\": \"物联网工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10015,\n" +
        "      \"schoolId\": 10004,\n" +
        "      \"name\": \"计算机科学与技术\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10016,\n" +
        "      \"schoolId\": 10004,\n" +
        "      \"name\": \"软件工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10017,\n" +
        "      \"schoolId\": 10004,\n" +
        "      \"name\": \"信息安全\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10018,\n" +
        "      \"schoolId\": 10004,\n" +
        "      \"name\": \"网络工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10019,\n" +
        "      \"schoolId\": 10004,\n" +
        "      \"name\": \"物联网工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10020,\n" +
        "      \"schoolId\": 10005,\n" +
        "      \"name\": \"计算机科学与技术\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10021,\n" +
        "      \"schoolId\": 10005,\n" +
        "      \"name\": \"软件工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10022,\n" +
        "      \"schoolId\": 10005,\n" +
        "      \"name\": \"信息安全\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10023,\n" +
        "      \"schoolId\": 10005,\n" +
        "      \"name\": \"网络工程\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10024,\n" +
        "      \"schoolId\": 10005,\n" +
        "      \"name\": \"物联网工程\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"pagination\": {\n" +
        "    \"targetPage\": 0,\n" +
        "    \"pageSize\": 10,\n" +
        "    \"total\": 25\n" +
        "  }\n" +
        "}";
    Logs.info('request result mock : ${json}');
    return SubjectMetaDataQueryResult.fromJson(jsonDecode(json));

  }


}
