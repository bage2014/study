
class AppVersion {
  int? id;
  int? versionCode;
  String? description;
  int? fileId;
  String? fileUrl;
  String? versionName;
  int? fileSize;
  String? updateType;
  DateTime? createTime;
  Null? updateTime;
  Null? createStaffId;
  Null? updateStaffId;
  Null? deleteState;

  AppVersion(
      {this.id,
        this.versionCode,
        this.description,
        this.fileId,
        this.fileUrl,
        this.versionName,
        this.fileSize,
        this.updateType,
        this.createTime,
        this.updateTime,
        this.createStaffId,
        this.updateStaffId,
        this.deleteState});

  static AppVersion getCurrentVersionInfo(){
    AppVersion appVersion = new AppVersion();
    appVersion.versionCode = 1;
    appVersion.versionName = "v1.0.0";
    return appVersion;
  }

  AppVersion.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    versionCode = json['versionCode'];
    description = json['description'];
    fileId = json['fileId'];
    fileUrl = json['fileUrl'];
    versionName = json['versionName'];
    fileSize = json['fileSize'];
    updateType = json['updateType'];
    createTime = json['createTime'];
    updateTime = json['updateTime'];
    createStaffId = json['createStaffId'];
    updateStaffId = json['updateStaffId'];
    deleteState = json['deleteState'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['versionCode'] = this.versionCode;
    data['description'] = this.description;
    data['fileId'] = this.fileId;
    data['fileUrl'] = this.fileUrl;
    data['versionName'] = this.versionName;
    data['fileSize'] = this.fileSize;
    data['updateType'] = this.updateType;
    data['createTime'] = this.createTime;
    data['updateTime'] = this.updateTime;
    data['createStaffId'] = this.createStaffId;
    data['updateStaffId'] = this.updateStaffId;
    data['deleteState'] = this.deleteState;
    return data;
  }
}
