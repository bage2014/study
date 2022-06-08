import 'package:tutorials/component/dialog/Dialogs.dart';
import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/constant/HttpConstant.dart';
import 'package:tutorials/model/AppVersion.dart';
import 'package:tutorials/utils/AppUtils.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/constant/RouteNameConstant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:share/share.dart';

class About extends StatefulWidget {
  @override
  _About createState() => new _About();
}

class _About extends State<About> {

  late AppVersion _currentVersionInfo;

  @override
  void initState() {
    _currentVersionInfo = AppVersion.getCurrentVersionInfo();
  }

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
              children: <Widget>[
                Text(Translations.textOf(context, "all.app.name"),
                    style:
                        TextStyle(fontSize: 17.0, fontWeight: FontWeight.bold))
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text(_currentVersionInfo?.versionName??'')
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "about.versions")),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                Navigator.of(context)
                    .pushNamed(RouteNameConstant.route_name_about_versions);
              },
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "about.device.id")),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                // 确认框
                showDevice();
              },
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

  void showDevice() {
    AppUtils.getDeviceId().then((value) => {
          Dialogs.showInfoDialog(
              context, Translations.textOf(context, "about.device.id"), value)
        });
  }
}
