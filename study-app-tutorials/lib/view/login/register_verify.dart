import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';

class RegisterVerify extends StatefulWidget {
  const RegisterVerify({Key? key}) : super(key: key);

  @override
  _RegisterVerifyState createState() => _RegisterVerifyState();
}

class _RegisterVerifyState extends State<RegisterVerify> {

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Scaffold(
      resizeToAvoidBottomInset: true,
      body:
      SafeArea(
          child: SingleChildScrollView(
          child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 24),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          SizedBox(
            height: 64,
          ),
          Container(
            margin: const EdgeInsets.symmetric(horizontal: 40),
            child: Column(
              children: [
                Text(
                  '请输入验证码',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                SizedBox(
                  height: 25,
                ),
                Text(
                  '请输入6位验证码，已发送到 ',
                  textAlign: TextAlign.center,
                ),
                Text(
                  'your.mail@qq.com',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    decoration: TextDecoration.underline,
                  ),
                ),
                SizedBox(
                  height: 100,
                ),
                Container(
                  height: 70,
                  width: size.width,
                  padding: EdgeInsets.symmetric(vertical: 0, horizontal: 0),
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
          SizedBox(
            height: 100,
          ),
          SizedBox(
            // color: Colors.blue,
            height: 64,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                ElevatedButton(
                  onPressed: () {
                    Logs.info("onPressed is callled,,,");
                  },
                  style: ElevatedButton.styleFrom(
                    primary: const Color(0xFF161616),
                    elevation: 0,
                    shadowColor: Colors.transparent,
                    fixedSize: Size(325, 50),
                  ),
                  child: Text(
                    '确认注册',
                    style: TextStyle(
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
        color: Color(0xFFF4F4F4),
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
      child: TextField(
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

