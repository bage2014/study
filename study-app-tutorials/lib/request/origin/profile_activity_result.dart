class ProfileActivityResult {
  int? code;
  int? originCode;
  String? msg;
  Null? originMsg;
  Data? data;

  ProfileActivityResult(
      {this.code, this.originCode, this.msg, this.originMsg, this.data});

  ProfileActivityResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
    data = json['data'] != null ? new Data.fromJson(json['data']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    if (this.data != null) {
      data['data'] = this.data!.toJson();
    }
    return data;
  }
}

class Data {
  Profile? profile;
  List<Activities>? activities;

  Data({this.profile, this.activities});

  Data.fromJson(Map<String, dynamic> json) {
    profile =
    json['profile'] != null ? new Profile.fromJson(json['profile']) : null;
    if (json['activities'] != null) {
      activities = <Activities>[];
      json['activities'].forEach((v) {
        activities!.add(new Activities.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.profile != null) {
      data['profile'] = this.profile!.toJson();
    }
    if (this.activities != null) {
      data['activities'] = this.activities!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Profile {
  int? id;
  String? name;
  String? location;
  int? starCounts;
  int? followerCounts;
  int? feedbackCounts;
  String? imageUrl;
  String? desc;

  Profile(
      {this.id,
        this.name,
        this.location,
        this.starCounts,
        this.followerCounts,
        this.feedbackCounts,
        this.imageUrl,
        this.desc});

  Profile.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    location = json['location'];
    starCounts = json['starCounts'];
    followerCounts = json['followerCounts'];
    feedbackCounts = json['feedbackCounts'];
    imageUrl = json['imageUrl'];
    desc = json['desc'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['location'] = this.location;
    data['starCounts'] = this.starCounts;
    data['followerCounts'] = this.followerCounts;
    data['feedbackCounts'] = this.feedbackCounts;
    data['imageUrl'] = this.imageUrl;
    data['desc'] = this.desc;
    return data;
  }
}

class Activities {
  int? id;
  String? desc;
  List<String>? imageUrls;

  Activities({this.id, this.desc, this.imageUrls});

  Activities.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    desc = json['desc'];
    imageUrls = json['imageUrls'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['desc'] = this.desc;
    data['imageUrls'] = this.imageUrls;
    return data;
  }
}
