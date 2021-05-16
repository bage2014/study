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
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Image(image: AssetImage("assets/images/user_null.png"))
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "about.author.name")),
              trailing: Icon(Icons.chevron_right),
              onTap: () {},
            ),
          ),
        ]),
      ),
    );
  }
}
