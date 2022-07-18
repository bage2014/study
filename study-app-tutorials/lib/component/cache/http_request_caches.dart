import 'package:tutorials/component/sp/shared_preference_helper.dart';
import 'package:tutorials/constant/sp_constant.dart';

class HttpRequestCaches {
  static String _protocol = "";
  static String _host = "";
  static String _port = "";

  static void init() async {
    _protocol =
        await SharedPreferenceHelper.get(SpConstant.protocol_key, SpConstant.protocol_value_default);
    _host = await SharedPreferenceHelper.get(SpConstant.host_key, SpConstant.host_value_default);
    _port = await SharedPreferenceHelper.get(SpConstant.port_key, SpConstant.port_value_default);
  }

  static String getProtocol() {
    return _protocol;
  }

  static String getHost() {
    return _host;
  }

  static String getPort() {
    return _port;
  }
}
