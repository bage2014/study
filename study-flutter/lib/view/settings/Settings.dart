import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app_upgrade/flutter_app_upgrade.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/constant/HttpConstant.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/utils/AppUtils.dart';
import 'package:open_file/open_file.dart';
import 'package:path_provider/path_provider.dart';

class Settings extends StatefulWidget {
  @override
  _Settings createState() => new _Settings();
}

class _Settings extends State<Settings> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings"),
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
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[Text("[未登录]")],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text("检查更新"),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                AppUpgrade.appUpgrade(
                  context,
                  _checkAppInfo(),
                  iosAppId: 'id1345678',
                );
              },
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text("开发者工具"),
              trailing: Icon(Icons.chevron_right),
              onTap: () {
                Navigator.of(context)
                    .pushNamed(RouteNameConstant.route_name_setting_develop);
              },
            ),
          ),
        ]),
      ),
    );
  }

  Future<AppUpgradeInfo> _checkAppInfo() async {
    try {
      // 版本校验
      HttpResult httpResult = await HttpRequests.get(HttpConstant.url_settings_version_check, null, null);
      // 下载
      HttpRequests.bytes(httpResult.responseBody, null, null)
          .then((result) {

      }).catchError((error){
        print(error.toString());
      });

      Directory dir = await getTemporaryDirectory();
//      Directory dir = await getExternalStorageDirectory();
      File file = File('${dir.path}/abcde.apk');
      var raf = file.openSync(mode: FileMode.write);
      var data = response.data;
      raf.writeFromSync(data);
      await raf.close();

      print('donwload apk finished... ${file.path}');

      AppUtils.openFile(file);
    } catch (e) {
      print(e);
    }

  }
}
