import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/locale/translations.dart';

class ProfileIconBasic extends StatefulWidget {
  const ProfileIconBasic(
      {Key? key, this.url = "assets/images/user_null.png", this.onTap})
      : super(key: key);
  final String url;
  final GestureTapCallback? onTap;

  @override
  _ProfileIconBasicState createState() => _ProfileIconBasicState();
}

class _ProfileIconBasicState extends State<ProfileIconBasic> {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: GestureDetector(
        onTap: widget.onTap,
        child: ClipOval(
          child: Image(image: AssetImage(widget.url)),
        ),
      ),
    );
  }
}
