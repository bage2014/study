import 'package:dio/dio.dart';
import 'package:tutorials/request/model/common/common_param.dart';

class FileUploadParam {
  CommonParam common = CommonParam();

  Map<String,dynamic>? formData;
  List<MultipartFile>? files;

}
