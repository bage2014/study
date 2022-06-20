class CommonParam {
  CommonParam();
  String? traceId;
  String? deviceId;
  String? version;

  CommonParam.fromJson(Map<String, dynamic> json){
    traceId = json['traceId'];
    deviceId = json['deviceId'];
    version = json['version'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['traceId'] = traceId;
    _data['deviceId'] = deviceId;
    _data['version'] = version;
    return _data;
  }
}