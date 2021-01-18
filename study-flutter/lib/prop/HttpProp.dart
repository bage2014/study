import 'package:dio/dio.dart';
import 'package:flutter_study/component/log/Logs.dart';

class HttpProp {

  static String baseUrl;
  static int timeout;
  static String contentType;

  static void init() {
    baseUrl = "http://101.132.119.250:8088/tutorials/ignore/";
//    baseUrl = "http://10.0.2.2:8088/tutorials/ignore/";
    timeout = 5000;
    contentType = "application/json; charset=utf-8";
  }

}
