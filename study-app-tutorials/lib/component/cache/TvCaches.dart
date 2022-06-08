import 'dart:convert';

import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/component/sp/SharedPreferenceHelper.dart';
import 'package:tutorials/constant/SpConstant.dart';

class TvCaches {
  static Map<String, dynamic> map = Map();

  static void init() {
    SharedPreferenceHelper.get(SpConstant.tv_list_cache_key, '')
        .then((value) => {loadToCache(value)});
  }

  static void loadToCache(String mapJson) {
    Logs.info('TvCaches1 : ${mapJson}');
    if (mapJson == '') {
      return;
    }
    map = json.decode(mapJson);
    Logs.info('TvCaches2 : ${json.encode(map)}');
  }

  static int getTvIndex(int id) {
    return map[id.toString()]??0;
  }

  static void setTvIndex(int id, int index) async {
    Map<String, dynamic> tempMap = Map();
    String jsonMap =
        await SharedPreferenceHelper.get(SpConstant.tv_list_cache_key, '');
    if (jsonMap != '') {
      tempMap = json.decode(jsonMap);
    }
    tempMap[id.toString()] = index;
    SharedPreferenceHelper.set(
            SpConstant.tv_list_cache_key, json.encode(tempMap))
        .then((value) => {map[id.toString()] = index});
  }
}
