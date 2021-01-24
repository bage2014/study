import 'package:dio/dio.dart';
import 'package:flutter_study/component/log/Logs.dart';

class HttpProp {

  static String baseUrl;
  static int timeout;
  static String contentType;

  static void init() {
    baseUrl = "http://101.132.119.250:8088/tutorials";
//    baseUrl = "http://192.168.1.104:8088/tutorials";
    timeout = 5000;
    contentType = "application/json; charset=utf-8";
  }

}
