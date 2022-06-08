import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/RouteNameConstant.dart';
import 'package:tutorials/utils/AppUtils.dart';

class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  _LoginView createState() => _LoginView();
}

class _LoginView extends State<Login> {
  @override
  Widget build(BuildContext context) {
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
                  'Hi there, welcome!',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w600,
                    color: const Color(0xFF262626),
                  ),
                ),
                Padding(
                  padding: EdgeInsets.only(right: 37, top: 8, bottom: 23),
                  child: Text(
                    'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Convallis vestibulum augue massa sed aenean.',
                    style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w400,
                      color: const Color(0xFF262626),
                      height: 1.28,
                    ),
                  ),
                ),
                _textField(
                  hintText: 'Your full name',
                  prefixIcon:
                  const Icon(Icons.person, color: Color(0xFFA8A8A8)),
                ),
                SizedBox(height: 14),
                _textField(
                  hintText: 'Your email address',
                  prefixIcon: const Icon(Icons.email, color: Color(0xFFA8A8A8)),
                ),
                SizedBox(height: 14),
                Row(
                  children: [
                    Expanded(
                      child: _textField(
                        hintText: '*******',
                        prefixIcon:
                        const Icon(Icons.vpn_key, color: Color(0xFFA8A8A8)),
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
                SizedBox(height: 95),
                _button(text: 'Create new account'),
                _button(text: 'Forgot password', isTransparent: true),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _button({required String text, bool isTransparent = false}) =>
      ElevatedButton(
        onPressed: () {
          // GetIt.I.get<NavigationService>().back(); // todo bage
          Logs.info("hello onPressed");
          AppUtils.toPage(context,RouteNameConstant.route_name_home);
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

  Widget _textField({required String hintText, required Widget prefixIcon}) =>
      TextField(
        onTap: () {
          Logs.info("hello onTap");
          AppUtils.toPage(context,RouteNameConstant.route_name_home)
        },
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
