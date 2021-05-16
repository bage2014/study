import 'package:flutter/material.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/locale/Translations.dart';

class About extends StatefulWidget {
  @override
  _About createState() => new _About();
}

class _About extends State<About> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Image(image: AssetImage("assets/images/logo128.png"))
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[Text("v1.0.0 beta")],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "about.author")),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                Navigator.of(context).pushNamed(
                    RouteNameConstant.route_name_about_author);
              },
            ),
          ),
        ]),
      ),
    );
  }
}
