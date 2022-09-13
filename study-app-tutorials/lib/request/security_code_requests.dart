

import 'dart:collection';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/security_code_request_param.dart';
import 'package:tutorials/request/model/security_code_request_result.dart';
import 'package:tutorials/utils/app_utils.dart';


class SecurityCodeRequests {

  static String url(SecurityCodeRequestParam requestParam) {
    Logs.info('request param : ${requestParam?.toString()}');
    // return HttpConstant.url_validate_code + '?clientId=123456';
    return 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.616pic.com%2Fys_bnew_img%2F00%2F29%2F79%2F9UMK4fzdwr.jpg&refer=http%3A%2F%2Fpic.616pic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665674476&t=9eb76ad34a4addda1b09213e625ae99c';
  }

  static Future<SecurityCodeRequestResult> load(SecurityCodeRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1),() => mock());
  }

  static SecurityCodeRequestResult mock(){
    SecurityCodeRequestResult result = SecurityCodeRequestResult();
    result.common.code = 200;
    // result.message = "404啦啦啦";
    result.url = 'https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87%20%E9%AA%8C%E8%AF%81%E7%A0%81&step_word=&hs=0&pn=3&spn=0&di=7077204560107798529&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=3617651942%2C1398176784&os=2089719489%2C3083133450&simid=3404834121%2C19490762&adpicid=0&lpn=0&ln=1767&fr=&fmq=1655655586039_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2Fpic.616pic.com%2Fys_bnew_img%2F00%2F29%2F79%2F9UMK4fzdwr.jpg%26refer%3Dhttp%3A%2F%2Fpic.616pic.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Dauto%3Fsec%3D1658247603%26t%3Dcc4a8e5405c8fe5d28354a69412930b3&fromurl=ippr_z2C%24qAzdH3FAzdH3Fm8mrtv_z%26e3Bv54AzdH3Ff7vwtAzdH3Fzd6tshd2e_z%26e3Bip4s&gsm=4&rpstart=0&rpnum=0&islist=&querylist=&nojc=undefined&dyTabStr=MCwzLDUsMSw2LDQsNyw4LDIsOQ%3D%3D';
    Logs.info('request result : ${result?.toString()}');
    return result;
  }

}
