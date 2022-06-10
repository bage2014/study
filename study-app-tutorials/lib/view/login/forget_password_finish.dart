import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/utils/app_utils.dart';

class ForgetPasswordFinish extends StatefulWidget {
  const ForgetPasswordFinish({Key? key}) : super(key: key);

  @override
  _ForgetPasswordFinishState createState() => _ForgetPasswordFinishState();
}

class _ForgetPasswordFinishState extends State<ForgetPasswordFinish> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          SizedBox(height: 24),
          Padding(
            padding:
            EdgeInsets.only(left: 24, right: 24, top: 24, bottom: 0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                GestureDetector(
                  onTap: () {
                    AppUtils.back(context);
                  },
                  child: const Icon(Icons.arrow_back_outlined),
                ),
              ],
            ),
          ),
          SizedBox(height: 64),
          Container(
            margin: const EdgeInsets.symmetric(horizontal: 40),
            child: Column(
              children: [
                Text(
                  '重置成功!',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                SizedBox(
                  height: 25,
                ),
                Text(
                  '请查看邮箱，获取最新密码，重新登陆，开启新篇章吧.',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 64),
          Container(
            height: 128,
            // color: Colors.green,
            padding: EdgeInsets.only(left: 50, right: 50),
            child: Image(image: AssetImage("assets/images/logo128.png")),
          ),
          Spacer(),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 20),
            child: ElevatedButton(
              onPressed: () {
                AppUtils.toPage(context,RouteNameConstant.route_name_login);
              },
              style: ElevatedButton.styleFrom(
                primary: const Color(0xFF161616),
                elevation: 0,
                shadowColor: Colors.transparent,
                fixedSize: Size(342, 54),
              ),
              child: Text(
                '重新登陆',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w600,
                  color: Colors.white,
                ),
              ),
            ),
          ),
          SizedBox(height: 88),
        ],
      ),
    );
  }
}
