import 'package:tutorials/request/model/common/common_param.dart';

class TvQueryRequestParam {
  int? favoriteUserId;
  bool? isOnlyFavorite;
  int? targetPage;
  int? pageSize;
  CommonParam? commonParam;

  TvQueryRequestParam(
      {this.favoriteUserId,
        this.isOnlyFavorite,
        this.targetPage,
        this.pageSize,
        this.commonParam});

  TvQueryRequestParam.fromJson(Map<String, dynamic> json) {
    favoriteUserId = json['favoriteUserId'];
    isOnlyFavorite = json['isOnlyFavorite'];
    targetPage = json['targetPage'];
    pageSize = json['pageSize'];
    commonParam = json['commonParam'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['favoriteUserId'] = this.favoriteUserId;
    data['isOnlyFavorite'] = this.isOnlyFavorite;
    data['targetPage'] = this.targetPage;
    data['pageSize'] = this.pageSize;
    data['commonParam'] = this.commonParam;
    return data;
  }
}

