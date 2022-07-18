import 'dart:convert';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/color_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/model/forget/forget_password_request_param.dart';
import 'package:tutorials/request/model/security_code_request_param.dart';
import 'package:tutorials/request/security_code_requests.dart';
import 'package:tutorials/utils/app_utils.dart';

class ForgetPassword extends StatefulWidget {
  const ForgetPassword({Key? key}) : super(key: key);

  @override
  _ForgetPasswordState createState() => _ForgetPasswordState();
}

class _ForgetPasswordState extends State<ForgetPassword> {
  TextEditingController userNameController = TextEditingController();
  TextEditingController securityCodeController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    String _imageUrl = SecurityCodeRequests.url(SecurityCodeRequestParam());
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
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Image(image: AssetImage("assets/images/logo128.png")),
                const SizedBox(height: 24),
                Text(
                  Translations.textOf(context, 'forget.password.title'),
                  style: const TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w600,
                    color: Color(0xFF262626),
                  ),
                ),
                const SizedBox(height: 24),
                TextField(
                  controller: userNameController,
                  decoration: InputDecoration(
                    hintText: Translations.textOf(
                        context, 'forget.password.mail.hint'),
                    hintStyle: const TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w400,
                      color: Color(0xFFA8A8A8),
                    ),
                    prefixIcon:
                        const Icon(Icons.email, color: Color(0xFFA8A8A8)),
                    contentPadding: const EdgeInsets.symmetric(
                        horizontal: 17, vertical: 22),
                    border: const OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                    focusedBorder: const OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                    enabledBorder: const OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                  ),
                ),
                const SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: securityCodeController,
                        decoration: InputDecoration(
                          hintText: Translations.textOf(
                              context, 'forget.password.security.code.hint'),
                          hintStyle: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w400,
                            color: Color(0xFFA8A8A8),
                          ),
                          prefixIcon: const Icon(Icons.security_rounded,
                              color: Color(0xFFA8A8A8)),
                          contentPadding: const EdgeInsets.symmetric(
                              horizontal: 17, vertical: 22),
                          border: const OutlineInputBorder(
                              borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                          focusedBorder: const OutlineInputBorder(
                              borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                          enabledBorder: const OutlineInputBorder(
                              borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                        ),
                      ),
                    ),
                    const SizedBox(width: 10),
                    GestureDetector(
                      onTap: () {
                        _imageUrl = SecurityCodeRequests.url(
                            SecurityCodeRequestParam());
                      },
                      child: Container(
                        width: 120,
                        height: 64,
                        decoration: BoxDecoration(
                          border: Border.all(
                            color: const Color(0xFFD0D0D0),
                          ),
                        ),
                        child: CachedNetworkImage(
                          imageUrl: _imageUrl,
                          placeholder: (context, url) =>
                              const CircularProgressIndicator(),
                          errorWidget: (context, url, error) => const Image(
                              image: AssetImage("assets/images/user_null.png")),
                          height: 64,
                          width: 64,
                        ),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 64),
                ElevatedButton(
                  onPressed: () {
                    next();
                  },
                  style: ElevatedButton.styleFrom(
                    primary: const Color(0xFF0043CE),
                    elevation: 0,
                    shadowColor: Colors.transparent,
                    fixedSize: const Size(342, 64),
                  ),
                  child: Text(
                    Translations.textOf(context, 'forget.password.go'),
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                      color: Color(0xFFF4F4F4),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void next() {
    ForgetPasswordRequestParam param = ForgetPasswordRequestParam();
    param.userName = userNameController.text;
    param.securityCode = securityCodeController.text;
    String str = json.encode(param.toJson());
    Logs.info("json : $str");
    AppUtils.toPage(
        context, RouteNameConstant.route_name_forget_password_verify,
        args: str);
  }
}
