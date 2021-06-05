import 'package:app_lu_lu/component/http/HttpRequests.dart';
import 'package:app_lu_lu/constant/HttpConstant.dart';
import 'package:flutter/material.dart';
import 'package:app_lu_lu/constant/RouteNameConstant.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:share/share.dart';

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
            padding: const EdgeInsets.only(top: 16),
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
              children: <Widget>[
                Text(Translations.textOf(context, "all.app.name"),
                    style:
                        TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold))
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text(Translations.textOf(context, "all.app.version"))
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "about.author")),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                Navigator.of(context)
                    .pushNamed(RouteNameConstant.route_name_about_author);
              },
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "about.share")),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                String url = HttpRequests.rebuildUrl(
                    HttpConstant.url_settings_app_latest);
                Share.share('【小陆，小陆，简简单单的小陆APP】 ${url} ', subject: '');
              },
            ),
          ),
        ]),
      ),
    );
  }
}
