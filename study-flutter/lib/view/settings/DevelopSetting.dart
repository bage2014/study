import 'package:flutter/material.dart';
import 'package:flutter_study/component/dialog/Dialogs.dart';

class DevelopSetting extends StatefulWidget {
  @override
  _DevelopSetting createState() => new _DevelopSetting();
}

class _DevelopSetting extends State<DevelopSetting> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Develop Setting"),
      ),
      body: Container(
        alignment: Alignment.center,
        margin: EdgeInsets.only(left: 16),
        child: Column(children: <Widget>[
          Container(
            child: new GestureDetector(
              onTap: () {
                List<String> list = [];
                list.add("http");
                list.add("https");
                Dialogs.showListBottomSheet(context, list)
                    .then((value) => {print(value)});
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text('协议'),
                  Text('http', style: TextStyle(fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: Icon(
                      Icons.chevron_right,
                    ),
                    onPressed: () => {},
                  ),
                ],
              ),
            ),
          ),
          Container(
            child: new GestureDetector(
              onTap: () {
                Dialogs.showInputDialog(context, "域名")
                    .then((value) => {print(value)});
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text('域名'),
                  Text('101.132.119.250',
                      style: TextStyle(fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: Icon(
                      Icons.chevron_right,
                    ),
                    onPressed: () => {},
                  ),
                ],
              ),
            ),
          ),
          Container(
            child: new GestureDetector(
              onTap: () {
                Dialogs.showInputDialog(context, "端口")
                    .then((value) => {print(value)});
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text('端口'),
                  Text('8088', style: TextStyle(fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: Icon(
                      Icons.chevron_right,
                    ),
                    onPressed: () => {},
                  ),
                ],
              ),
            ),
          ),
        ]),
      ),
    );
  }
}
