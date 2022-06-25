

import 'dart:collection';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/request/model/security_code_request_param.dart';
import 'package:tutorials/request/model/security_code_request_result.dart';


class SecurityCodeRequests {

  static String url(SecurityCodeRequestParam requestParam) {
    Logs.info('request param : ${requestParam?.toString()}');
    // return 'https://image.baidu.com/seahttps://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E9%AA%8C%E8%AF%81%E7%A0%81%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=15&spn=0&di=7108135681917976577&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1547709124%2C3054267786&os=2693430515%2C2304968001&simid=4101863331%2C744506482&adpicid=0&lpn=0&ln=1657&fr=&fmq=1655734912923_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2Fwww.pianshen.com%2Fimages%2F172%2F21030f46e0c39219ea347563a767aacc.JPEG%26refer%3Dhttp%3A%2F%2Fwww.pianshen.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Dauto%3Fsec%3D1658326922%26t%3D93ea1d3d5912c74fc3cbc82babc73764&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Brtwgfijg_z%26e3Bv54AzdH3Fw6ptvsjAzdH3Fdcl98db8mmaAzdH3F&gsm=10&rpstart=0&rpnum=0&islist=&querylist=&nojc=undefined&dyTabStr=MCwzLDIsMSw0LDYsNSw4LDcsOQ%3D%3Drch/detail?ct=503316480&z=0&ipn=d&word=%E9%AA%8C%E8%AF%81%E7%A0%81%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=15&spn=0&di=7108135681917976577&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1547709124%2C3054267786&os=2693430515%2C2304968001&simid=4101863331%2C744506482&adpicid=0&lpn=0&ln=1657&fr=&fmq=1655734912923_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2Fwww.pianshen.com%2Fimages%2F172%2F21030f46e0c39219ea347563a767aacc.JPEG%26refer%3Dhttp%3A%2F%2Fwww.pianshen.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Dauto%3Fsec%3D1658326922%26t%3D93ea1d3d5912c74fc3cbc82babc73764&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Brtwgfijg_z%26e3Bv54AzdH3Fw6ptvsjAzdH3Fdcl98db8mmaAzdH3F&gsm=10&rpstart=0&rpnum=0&islist=&querylist=&nojc=undefined&dyTabStr=MCwzLDIsMSw0LDYsNSw4LDcsOQ%3D%3D';
    return 'https://avatars.githubusercontent.com/u/18094768?v=4';
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
