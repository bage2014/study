import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/color_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/request/model/register_request_param.dart';
import 'package:tutorials/utils/app_utils.dart';

class RegisterVerify extends StatefulWidget {
  const RegisterVerify({Key? key}) : super(key: key);

  @override
  _RegisterVerifyState createState() => _RegisterVerifyState();
}

class _RegisterVerifyState extends State<RegisterVerify> {
  @override
  Widget build(BuildContext context) {
    String str = AppUtils.getArgs(context).toString();
    Logs.info("json : $str");
    RegisterRequestParam param =
        RegisterRequestParam.fromJson(json.decode(str));
    Logs.info("param : ${param.userName}");

    Size size = MediaQuery.of(context).size;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        iconTheme: IconThemeData(color: ColorConstant.app_bar_only_back_color),
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
                        Translations.textOf(context, 'register.security.hint'),
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
                        padding:
                            const EdgeInsets.symmetric(vertical: 0, horizontal: 0),
                        decoration: BoxDecoration(
                          // color: Colors.purple,
                          borderRadius: BorderRadius.circular(4),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: const [
                            VerificationTextField(),
                            VerificationTextField(),
                            VerificationTextField(),
                            VerificationTextField(),
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
                          AppUtils.toPage(context,
                              RouteNameConstant.route_name_register_finish);
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
    );
  }
}

class VerificationTextField extends StatelessWidget {
  const VerificationTextField({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 64,
      height: 64,
      padding: const EdgeInsets.symmetric(horizontal: 5),
      decoration: BoxDecoration(
        color: const Color(0xFFF4F4F4),
        borderRadius: BorderRadius.circular(4),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.6),
            spreadRadius: 2,
            blurRadius: 2,
            offset: Offset.zero,
          ),
        ],
      ),
      child: const TextField(
        textAlign: TextAlign.center,
        maxLength: 1,
        style: TextStyle(
          fontSize: 28,
        ),
        decoration: InputDecoration(
          counterText: "",
        ),
      ),
    );
  }
}
