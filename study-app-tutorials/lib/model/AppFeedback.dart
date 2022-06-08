class AppFeedback {
  int? id;
  int? fromUserId;
  String? fromUserName;
  int? toUserId;
  String? toUserName;
  String? msgContent;
  String? readStatus;
  String? sendTime;
  String? readTime;
  String? msgType;
  String? createTime;
  String? updateTime;
  int? createStaffId;
  int? updateStaffId;
  String? deleteState;

  AppFeedback(
      {this.id,
      this.fromUserId,
      this.fromUserName,
      this.toUserId,
      this.toUserName,
      this.msgContent,
      this.readStatus,
      this.sendTime,
      this.readTime,
      this.msgType,
      this.createTime,
      this.updateTime,
      this.createStaffId,
      this.updateStaffId,
      this.deleteState});

  AppFeedback.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    fromUserId = json['fromUserId'];
    fromUserName = json['fromUserName'];
    toUserId = json['toUserId'];
    toUserName = json['toUserName'];
    msgContent = json['msgContent'];
    readStatus = json['readStatus'];
    sendTime = json['sendTime'];
    readTime = json['readTime'];
    msgType = json['msgType'];
    createTime = json['createTime'];
    updateTime = json['updateTime'];
    createStaffId = json['createStaffId'];
    updateStaffId = json['updateStaffId'];
    deleteState = json['deleteState'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['fromUserId'] = this.fromUserId;
    data['fromUserName'] = this.fromUserName;
    data['toUserId'] = this.toUserId;
    data['toUserName'] = this.toUserName;
    data['msgContent'] = this.msgContent;
    data['readStatus'] = this.readStatus;
    data['sendTime'] = this.sendTime;
    data['readTime'] = this.readTime;
    data['msgType'] = this.msgType;
    data['createTime'] = this.createTime;
    data['updateTime'] = this.updateTime;
    data['createStaffId'] = this.createStaffId;
    data['updateStaffId'] = this.updateStaffId;
    data['deleteState'] = this.deleteState;
    return data;
  }
}
