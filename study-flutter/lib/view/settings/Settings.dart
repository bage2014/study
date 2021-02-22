import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_study/component/dialog/Dialogs.dart';
import 'package:flutter_study/component/http/HttpByteResult.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/constant/HttpConstant.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/model/AppVersionResult.dart';
import 'package:flutter_study/utils/AppUtils.dart';
import 'package:flutter_study/utils/FileUtils.dart';

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
              onTap: _checkAppInfo,
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

  void _checkAppInfo() async {
    try {
      // 版本校验
      String url = HttpConstant.url_settings_version_check
          .replaceAll("{version}", AppUtils.getCurrentVersion().toString());
      HttpResult httpResult = await HttpRequests.get(url, null, null);
      Logs.info("responseBody = ${httpResult.responseBody}");
      // ignore: null_aware_in_condition
      if (httpResult?.responseBody?.isEmpty) {
        return ;
      }
      AppVersionResult appVersionResult =
          AppVersionResult.fromJson(json.decode(httpResult?.responseBody));
      if (appVersionResult?.code == 200) {
        Dialogs.showProgress(0, 'Downloading...');
        // 下载
        HttpByteResult httpByteResult =
            await HttpRequests.getBytes('https://f-droid.org/F-Droid.apk', null, null,(int sent, int total) {
              double percent = sent / total;
              print("_doDownloadRequest onReceiveProgress ${percent}%");
              Dialogs.showProgress(percent, 'Downloading...');
              if(sent >= total ){
                Dialogs.dismiss();
              }
            });
//        HttpByteResult httpByteResult =
//            await HttpRequests.bytes(appVersionResult.data.fileUrl, null, null);
        print('donwload apk finished...');
        // 保存
        Directory downloadDir = await FileUtils.getDownloadDir();
        File file = File('${downloadDir.path}/latest-app.apk');
        bool isSuccess = await FileUtils.write(file, httpByteResult.responseBytes);
        print('save file isSuccess = ${isSuccess} to ${file.path}');
        // 打开文件
        if(isSuccess){
          FileUtils.openFile(file);
          print('open file ${file.path}');
        }
      }
    } catch (e) {
      print(e);
    }
  }
}
