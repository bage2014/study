import 'package:app_lu_lu/model/AppVersionResult.dart';

class AppVersionsResult {
  int? code;
  int? originCode;
  String? msg;
  Null? originMsg;
  List<AppVersion> data = [];

  AppVersionsResult(int n){
    for(int i = 0;i < n; i ++){
      AppVersion value = new AppVersion();
      value.id = n - i;
      value.versionCode = n - i;
      value.versionName = 'v1.0.${n - i}';
      value.description = '第一个版本\n电视功能\n家庭族谱功能\n';
      value.createTime = DateTime.now();
      data.add(value);
    }
    AppVersion value = new AppVersion();
    value.versionName = 'Start';
    data.add(value);
  }


}
