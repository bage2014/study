import 'package:flutter/material.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/request/login_requests.dart';
import 'package:tutorials/request/model/login_request_param.dart';
import 'package:tutorials/utils/app_utils.dart';

class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  _LoginView createState() => _LoginView();
}

class _LoginView extends State<Login> {
  bool _obscureText = true;
  bool hideSecurityCode = false;

  TextEditingController userNameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController securityCodeController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFFCFCFC),
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const SizedBox(height: 40),
                const Image(image: AssetImage("assets/images/logo128.png")),
                const SizedBox(height: 62),
                Text(
                  Translations.textOf(context, 'login.title'),
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
                    hintText:
                        Translations.textOf(context, 'login.account.hint'),
                    hintStyle: const TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w400,
                      color: Color(0xFFA8A8A8),
                    ),
                    prefixIcon:
                        const Icon(Icons.person, color: Color(0xFFA8A8A8)),
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
                        controller: passwordController,
                        obscureText: _obscureText,
                        decoration: InputDecoration(
                          hintText: Translations.textOf(
                              context, 'login.password.hint'),
                          hintStyle: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w400,
                            color: Color(0xFFA8A8A8),
                          ),
                          suffixIcon: IconButton(
                              icon: Icon(!_obscureText
                                  ? Icons.visibility
                                  : Icons.visibility_off),
                              onPressed: () {
                                setState(() {
                                  _obscureText = !_obscureText;
                                });
                              }),
                          prefixIcon: const Icon(Icons.vpn_key,
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
                  ],
                ),
                const SizedBox(height: 14),
                hideSecurityCode
                    ? const SizedBox(height: 0)
                    : Row(
                        children: [
                          Expanded(
                            child: TextField(
                              controller: securityCodeController,
                              decoration: InputDecoration(
                                hintText: Translations.textOf(
                                    context, 'login.security.hint'),
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
                                    borderSide:
                                        BorderSide(color: Color(0xFFD0D0D0))),
                                focusedBorder: const OutlineInputBorder(
                                    borderSide:
                                        BorderSide(color: Color(0xFFD0D0D0))),
                                enabledBorder: const OutlineInputBorder(
                                    borderSide:
                                        BorderSide(color: Color(0xFFD0D0D0))),
                              ),
                            ),
                          ),
                          const SizedBox(width: 10),
                          Container(
                            width: 74,
                            height: 65,
                            decoration: BoxDecoration(
                              border: Border.all(
                                color: const Color(0xFFD0D0D0),
                              ),
                            ),
                            child: const Center(
                              child: Image(
                                  image: AssetImage(
                                      "assets/images/user_null.png")),
                            ),
                          ),
                        ],
                      ),
                const SizedBox(height: 24),
                ElevatedButton(
                  onPressed: () {
                    LoginRequests.login(LoginRequestParam(
                            userNameController.text,
                            passwordController.text,
                            securityCodeController.text))
                        .then((value) => {
                              AppUtils.toPage(
                                  context, RouteNameConstant.route_name_home)
                            });
                  },
                  style: ElevatedButton.styleFrom(
                    primary: const Color(0xFF0043CE),
                    elevation: 0,
                    shadowColor: Colors.transparent,
                    fixedSize: Size(342, 64),
                  ),
                  child: Text(
                    Translations.textOf(context, 'login.button'),
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                      color: Color(0xFFF4F4F4),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                _button(
                    text: Translations.textOf(context, 'login.password.reset'),
                    route: RouteNameConstant.route_name_forget_password,
                    isTransparent: true),
                _button(
                    text: Translations.textOf(context, 'login.register'),
                    route: RouteNameConstant.route_name_register,
                    isTransparent: true),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _button(
          {required String text,
          required String route,
          bool isTransparent = false}) =>
      ElevatedButton(
        onPressed: () {
          AppUtils.toPage(context, route);
        },
        style: ElevatedButton.styleFrom(
          primary: isTransparent ? Colors.transparent : const Color(0xFF0043CE),
          elevation: 0,
          shadowColor: Colors.transparent,
          fixedSize: Size(342, 64),
        ),
        child: Text(
          text,
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.w600,
            color: isTransparent
                ? const Color(0xFF0043CE)
                : const Color(0xFFF4F4F4),
          ),
        ),
      );
}
