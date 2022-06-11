import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/color_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/utils/app_utils.dart';

class Register extends StatefulWidget {
  const Register({Key? key}) : super(key: key);

  @override
  _RegisterState createState() => _RegisterState();
}

class _RegisterState extends State<Register> {
  bool _obscureText = true;
  bool _obscureText2 = true;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        iconTheme: IconThemeData(color: ColorConstant.app_bar_only_back_color),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Image(image: AssetImage("assets/images/logo128.png")),
                SizedBox(height: 16),
                Text(
                  '用户注册',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w600,
                    color: const Color(0xFF262626),
                  ),
                ),
                SizedBox(height: 24),
                _textField(
                  hintText: '请输入邮箱',
                  prefixIcon: const Icon(Icons.email, color: Color(0xFFA8A8A8)),
                ),
                SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        obscureText: _obscureText,
                        decoration: InputDecoration(
                          hintText: '请输入密码',
                          hintStyle: TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w400,
                            color: const Color(0xFFA8A8A8),
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
                          contentPadding: EdgeInsets.symmetric(
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
                SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        obscureText: _obscureText2,
                        decoration: InputDecoration(
                          hintText: '请再次输入密码',
                          hintStyle: TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w400,
                            color: const Color(0xFFA8A8A8),
                          ),
                          suffixIcon: IconButton(
                              icon: Icon(!_obscureText2
                                  ? Icons.visibility
                                  : Icons.visibility_off),
                              onPressed: () {
                                setState(() {
                                  _obscureText2 = !_obscureText2;
                                });
                              }),
                          prefixIcon: const Icon(Icons.vpn_key,
                              color: Color(0xFFA8A8A8)),
                          contentPadding: EdgeInsets.symmetric(
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
                SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: _textField(
                        hintText: '请输入验证码',
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
                _button(text: '下一步',route: RouteNameConstant.route_name_register_verify),
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
          Logs.info("hello onPressed222333");
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
