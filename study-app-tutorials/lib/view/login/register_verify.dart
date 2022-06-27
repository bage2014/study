import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/color_constant.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/request/model/register/register_request_param.dart';
import 'package:tutorials/request/register_requests.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:tutorials/widgets/verification_text_field.dart';

class RegisterVerify extends StatefulWidget {
  const RegisterVerify({Key? key}) : super(key: key);

  @override
  _RegisterVerifyState createState() => _RegisterVerifyState();
}

class _RegisterVerifyState extends State<RegisterVerify> {
  bool _isLoading = false;
  RegisterRequestParam param = RegisterRequestParam();

  TextEditingController code1Controller = TextEditingController();
  TextEditingController code2Controller = TextEditingController();
  TextEditingController code3Controller = TextEditingController();
  TextEditingController code4Controller = TextEditingController();

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    String str = AppUtils.getArgs(context).toString();
    Logs.info("json : $str");
    param = RegisterRequestParam.fromJson(json.decode(str));
    Logs.info("param : ${param.userName}");
    return Stack(
      alignment: Alignment.center, //指定未定位或部分定位widget的对齐方式
      children: <Widget>[
        Scaffold(
          appBar: AppBar(
            backgroundColor: Colors.transparent,
            elevation: 0,
            iconTheme:
                IconThemeData(color: ColorConstant.app_bar_only_back_color),
          ),
          body: SafeArea(
            child: SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    const SizedBox(
                      height: 64,
                    ),
                    Container(
                      margin: const EdgeInsets.symmetric(horizontal: 40),
                      child: Column(
                        children: [
                          Text(
                            Translations.textOf(
                                context, 'register.security.hint'),
                            style: const TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                          const SizedBox(
                            height: 24,
                          ),
                          Text(
                            Translations.textOf(
                                context, 'register.security.code.send.hint'),
                            textAlign: TextAlign.center,
                          ),
                          Text(
                            param.userName ?? '',
                            textAlign: TextAlign.center,
                            style: const TextStyle(
                              decoration: TextDecoration.underline,
                            ),
                          ),
                          const SizedBox(
                            height: 84,
                          ),
                          Container(
                            height: 64,
                            width: size.width,
                            padding: const EdgeInsets.symmetric(
                                vertical: 0, horizontal: 0),
                            decoration: BoxDecoration(
                              // color: Colors.purple,
                              borderRadius: BorderRadius.circular(4),
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                VerificationTextField(code1Controller),
                                VerificationTextField(code2Controller),
                                VerificationTextField(code3Controller),
                                VerificationTextField(code4Controller),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(
                      height: 96,
                    ),
                    SizedBox(
                      // color: Colors.blue,
                      height: 64,
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          ElevatedButton(
                            onPressed: () {
                              confirm();
                            },
                            style: ElevatedButton.styleFrom(
                              primary: const Color(0xFF161616),
                              elevation: 0,
                              shadowColor: Colors.transparent,
                              fixedSize: const Size(325, 50),
                            ),
                            child: Text(
                              Translations.textOf(context, 'register.go'),
                              style: const TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.w600,
                                color: Colors.white,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
        Container(
          child: _isLoading
              ? Container(
            color: Colors.black54.withOpacity(0.5),
            width: double.infinity,
          )
              : null,
        ),
        Container(
          child: _isLoading
              ? CircularProgressIndicator(
                  backgroundColor: Colors.grey[200],
                  valueColor: const AlwaysStoppedAnimation(Colors.blue),
                )
              : null,
        ),
      ],
    );
  }

  void confirm() {
    showLoading();
    param.securityCode = code1Controller.text +
        code2Controller.text +
        code3Controller.text +
        code4Controller.text;
    RegisterRequests.register(param).then((result) {
      Logs.info('login result=' + (result.toString() ?? ""));
      hideLoading();
      if (result.common.code == ErrorCodeConstant.success) {
        AppUtils.toPage(context, RouteNameConstant.route_name_register_finish);
      } else {
        Toasts.show(result.common.message);
      }
    }).catchError((error) {
      Logs.info(error.toString());
      hideLoading();
    });
  }

  showLoading() {
    setState(() {
      _isLoading = true;
    });
  }

  hideLoading() {
    setState(() {
      _isLoading = false;
    });
  }
}
