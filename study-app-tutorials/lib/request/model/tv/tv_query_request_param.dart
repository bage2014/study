import 'package:tutorials/request/model/common/common_param.dart';

class TvQueryRequestParam {
  int? favoriteUserId;
  bool? isOnlyFavorite;
  int? targetPage;
  int? pageSize;


  TvQueryRequestParam(
      {this.favoriteUserId,
        this.isOnlyFavorite,
        this.targetPage,
        this.pageSize});

  CommonParam common = CommonParam();

  @override
  String toString() {
    return super.toString();
  }

}
