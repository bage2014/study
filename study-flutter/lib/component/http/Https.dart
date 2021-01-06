import 'package:dio/dio.dart';
import 'package:flutter_study/component/log/Logs.dart';

class Https {
  static Dio dio;

  static void init() {
    BaseOptions options = new BaseOptions(
      baseUrl: "http://101.132.119.250:8088/tutorials/ignore/",
      connectTimeout: 10000,
      receiveTimeout: 5000,
    );
    dio = new Dio(options);
  }

  static void getHttp() async {
    try {
      Response response = await Dio().get("http://www.google.com");
      print(response);
    } catch (e) {
      Logs.info(e);
    }
  }
}
