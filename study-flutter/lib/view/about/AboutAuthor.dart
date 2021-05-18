import 'package:flutter/material.dart';
import 'package:flutter_study/locale/Translations.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            padding: const EdgeInsets.fromLTRB(24.0,8.0,0.0,0.0),
            child: Row(
              children: <Widget>[
                Image(image: AssetImage("assets/images/user_null.png")),
                Container(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center, //垂直方向居中对齐
                    children: <Widget>[
                      Text('陆瑞华',textScaleFactor: 1.5),
                      Text("上海")
                    ],
                  ),
                ),
              ],
            ),
          ),
          Container(
            alignment: Alignment.centerLeft,
            child: Text('hello'),
//            child: Text(Translations.textOf(context, "about.author.name")),
          ),

        ]),
      ),
    );
  }
}
