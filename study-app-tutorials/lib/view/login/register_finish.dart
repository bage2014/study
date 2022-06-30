import 'package:flutter/material.dart';
import 'package:tutorials/constant/color_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/utils/app_utils.dart';

class RegisterFinish extends StatefulWidget {
  const RegisterFinish({Key? key}) : super(key: key);

  @override
  _RegisterFinishState createState() => _RegisterFinishState();
}

class _RegisterFinishState extends State<RegisterFinish> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        iconTheme: IconThemeData(color: ColorConstant.app_bar_only_back_color),
      ),
      body: Column(
        children: [
          const SizedBox(height: 64),
          Container(
            margin: const EdgeInsets.symmetric(horizontal: 40),
            child: Column(
              children: [
                Text(
                  Translations.textOf(context, 'register.finish.title'),
                  style: const TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                const SizedBox(
                  height: 24,
                ),
                Text(
                  Translations.textOf(context, 'register.finish.hint'),
                  textAlign: TextAlign.center,
                  style: const TextStyle(
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 64),
          Container(
            height: 128,
            // color: Colors.green,
            padding: const EdgeInsets.only(left: 50, right: 50),
            child: const Image(image: AssetImage("assets/images/logo128.png")),
          ),
          const Spacer(),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: ElevatedButton(
              onPressed: () {
                AppUtils.toPage(context,RouteNameConstant.route_name_login);
              },
              style: ElevatedButton.styleFrom(
                primary: const Color(0xFF161616),
                elevation: 0,
                shadowColor: Colors.transparent,
                fixedSize: const Size(342, 54),
              ),
              child: Text(
                Translations.textOf(context, 'register.finish.login'),
                style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w600,
                  color: Colors.white,
                ),
              ),
            ),
          ),
          const SizedBox(height: 88),
        ],
      ),
    );
  }
}
