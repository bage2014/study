import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/utils/app_utils.dart';

class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  _LoginView createState() => _LoginView();
}

class _LoginView extends State<Login> {
  bool _isObscure = true;

  @override
  Widget build(BuildContext context) {
    bool hideSecurityCode = true;
    return Scaffold(
      backgroundColor: const Color(0xFFFCFCFC),
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(height: 40),
                Image(image: AssetImage("assets/images/logo128.png")),
                SizedBox(height: 62),
                Text(
                  '用户登陆',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w600,
                    color: const Color(0xFF262626),
                  ),
                ),
                Padding(
                  padding: EdgeInsets.only(right: 37, top: 8, bottom: 23),
                  child: Text(
                    '根据用户ID或者邮箱或者手机号进行登陆',
                    style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w400,
                      color: const Color(0xFF262626),
                      height: 1.28,
                    ),
                  ),
                ),
                SizedBox(height: 14),
                _textField(
                  hintText: '请输入账号',
                  prefixIcon: const Icon(Icons.person, color: Color(0xFFA8A8A8)),
                ),
                SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: _textField(
                        hintText: '请出入密码',
                        prefixIcon:
                        const Icon(Icons.vpn_key, color: Color(0xFFA8A8A8)),
                          obscureText: true
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 14),
                hideSecurityCode ? SizedBox(height: 0):
                Row(
                  children: [
                    Expanded(
                      child: _textField(
                        hintText: '请出入验证码',
                        prefixIcon:
                        const Icon(Icons.security_rounded, color: Color(0xFFA8A8A8)),
                      ),
                    ),
                    SizedBox(width: 10),
                    Container(
                      width: 74,
                      height: 65,
                      decoration: BoxDecoration(
                        border: Border.all(
                          color: const Color(0xFFD0D0D0),
                        ),
                      ),
                      child: Center(
                        child: Image(image: AssetImage("assets/images/user_null.png")),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 24),
                _button(text: '登陆',route: RouteNameConstant.route_name_home),
                SizedBox(height: 16),
                _button(text: '忘记密码',route: RouteNameConstant.route_name_forget_password, isTransparent: true),
                _button(text: '创建账号', route: RouteNameConstant.route_name_register,isTransparent: true),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _button({required String text,required String route, bool isTransparent = false}) =>
      ElevatedButton(
        onPressed: () {
          AppUtils.toPage(context,route);
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

  Widget _textField({required String hintText, required Widget prefixIcon, bool obscureText = false}) =>
      TextField(
        obscureText: obscureText,
        decoration: InputDecoration(
          hintText: hintText,
          hintStyle: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w400,
            color: const Color(0xFFA8A8A8),
          ),
            suffixIcon: obscureText ? IconButton(
                icon: Icon(_isObscure ? Icons.visibility : Icons.visibility_off),
                onPressed: () {
                  setState(() {
                    _isObscure = !_isObscure;
                    obscureText = !_isObscure;
                  });
                }) : null,
          prefixIcon: prefixIcon,
          contentPadding:
          EdgeInsets.symmetric(horizontal: 17, vertical: 22),
          border: const OutlineInputBorder(
              borderSide: BorderSide(color: Color(0xFFD0D0D0))),
          focusedBorder: const OutlineInputBorder(
              borderSide: BorderSide(color: Color(0xFFD0D0D0))),
          enabledBorder: const OutlineInputBorder(
              borderSide: BorderSide(color: Color(0xFFD0D0D0))),
        ),
      );
}
