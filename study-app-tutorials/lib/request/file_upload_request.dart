import 'dart:collection';

import 'package:dio/src/multipart_file.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_param.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_result.dart';
import 'package:tutorials/request/model/upload/file_upload_param.dart';
import 'package:tutorials/request/model/upload/file_upload_result.dart';

class FileUploadRequests {
  static Future<FileUploadResult> upload(FileUploadParam param, Function progress){
    // Logs.info('request param : ${param?.toString()}');

    // String url = HttpConstant.url_settings_version_check
    //     .replaceAll("{version}", AppUtils.getCurrentVersion().toString());
    // HttpResult httpResult = await HttpRequests.get(url, null, null);
    // String? temp = httpResult?.responseBody;
    // String responseBody = temp == null ? "" : temp;
    // Logs.info("responseBody = ${responseBody}");
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1), () => mock());
  }

  static FileUploadResult mock() {
    FileUploadResult result = FileUploadResult();
    result.common.code = 200;
    // result.common.code = ErrorCodeConstant.loginSecurityCodeRequired;
    result.common.message = "404啦啦啦";
    Logs.info('request result : ${result?.toString()}');
    return result;
  }

}
