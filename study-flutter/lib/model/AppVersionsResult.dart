import 'package:app_lu_lu/model/AppVersionResult.dart';

class AppVersionsResult {
  int? code;
  int? originCode;
  String? msg;
  Null? originMsg;
  List<AppVersion> data = [];

  AppVersionsResult(){
    for(int i = 0;i < 5; i ++){
      AppVersion value = new AppVersion();
      value.id = 5 - i;
      value.versionCode = 5 - i;
      value.versionName = 'v1.0.${5 - i}';
      value.description = 'description${i}\ndescription${i}\ndescription${i}\n';
      value.createTime = DateTime.now();
      data.add(value);
    }
    AppVersion value = new AppVersion();
    value.versionName = 'Start';
    data.add(value);
  }


}
