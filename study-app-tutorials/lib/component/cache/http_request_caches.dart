import 'package:tutorials/component/sp/shared_preference_helper.dart';
import 'package:tutorials/constant/sp_constant.dart';
import 'package:tutorials/model/request_host.dart';

class HttpRequestCaches {
  static List<RequestHost> caches = [
    RequestHost(),
    RequestHost(),
    RequestHost(),
    RequestHost()
  ];
  static String _index = "1";

  static void inits() async {
    init("1");
    init("2");
    init("3");
    _index = await SharedPreferenceHelper.get(SpConstant.environment_index, "1");
  }

  static void init(String index) async {
    String _protocol = await SharedPreferenceHelper.get(
        _buildKey(SpConstant.protocol_key, index),
        SpConstant.protocol_value_default);
    String _host = await SharedPreferenceHelper.get(
        _buildKey(SpConstant.host_key, index), SpConstant.host_value_default);
    String _port = await SharedPreferenceHelper.get(
        _buildKey(SpConstant.port_key, index), SpConstant.port_value_default);
    caches[int.parse(index)].protocol = _protocol;
    caches[int.parse(index)].host = _host;
    caches[int.parse(index)].port = _port;
  }

  static Future<bool> setProtocol(String value, String index) async {
    caches[int.parse(index)].protocol = value;
    return SharedPreferenceHelper.set(
        _buildKey(SpConstant.protocol_key, index), value);
  }

  static Future<bool> setHost(String value, String index) async {
    caches[int.parse(index)].host = value;
    return SharedPreferenceHelper.set(
        _buildKey(SpConstant.host_key, index), value);
  }

  static Future<bool> setPort(String value, String index) async {
    caches[int.parse(index)].port = value;
    return SharedPreferenceHelper.set(
        _buildKey(SpConstant.port_key, index), value);
  }

  static void setIndex(String index) {
    _index = index;
    SharedPreferenceHelper.set(SpConstant.environment_index, index);
  }

  static String getIndex() {
    return _index;
  }

  static String getProtocol() {
    return caches[int.parse(_index)].protocol;
  }

  static String getIndexProtocol(String index) {
    return caches[int.parse(index)].protocol;
  }

  static String getHost() {
    return caches[int.parse(_index)].host;
  }

  static String getIndexHost(String index) {
    return caches[int.parse(index)].host;
  }

  static String getPort() {
    return caches[int.parse(_index)].port;
  }

  static String getIndexPort(String index) {
    return caches[int.parse(index)].port;
  }

  static String _buildKey(String key, String index) {
    return "http_request_" + index + "_" + key;
  }
}
