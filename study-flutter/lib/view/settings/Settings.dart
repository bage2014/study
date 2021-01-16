import 'package:flutter/material.dart';

class Settings extends StatefulWidget {
  @override
  _Settings createState() => new _Settings();
}

class _Settings extends State<Settings> {
  bool _switchSelected=true; //维护单选开关状态
  bool _checkboxSelected=true;//维护复选框状态
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings"),
      ),
      body: Column(
        children: <Widget>[
          Switch(
            value: _switchSelected,//当前状态
            onChanged:(value){
              //重新构建页面
              setState(() {
                _switchSelected=value;
              });
            },
          ),
          Checkbox(
            value: _checkboxSelected,
            activeColor: Colors.red, //选中时的颜色
            onChanged:(value){
              setState(() {
                _checkboxSelected=value;
              });
            } ,
          )
        ],
      ),
    );
  }
}