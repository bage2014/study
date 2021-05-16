import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_study/component/dialog/Dialogs.dart';
import 'package:flutter_study/component/http/CancelRequests.dart';
import 'package:flutter_study/component/http/HttpByteResult.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/constant/HttpConstant.dart';
import 'package:flutter_study/constant/LocaleConstant.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/locale/Translations.dart';
import 'package:flutter_study/model/AppVersionResult.dart';
import 'package:flutter_study/utils/AppUtils.dart';
import 'package:flutter_study/utils/FileUtils.dart';
//import 'package:url_launcher/url_launcher.dart';

class Settings extends StatefulWidget {
  @override
  _Settings createState() => new _Settings();
}

class _Settings extends State<Settings> {
  BuildContext _context = null;
  bool _isDownloading = false;
  CancelRequests cancelRequests = CancelRequests();

  @override
  Widget build(BuildContext context) {
    _context = context;
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "settings.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
//                Image(image: AssetImage("assets/images/user_null.png"))
                GestureDetector(
                  child: Image(image: AssetImage("assets/images/logo128.png")),
                  onDoubleTap: () {
                    Navigator.of(context).pushNamed(
                        RouteNameConstant.route_name_setting_dev_tool);
                  },
                ),
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
//              children: <Widget>[Text("[未登录]")],
              children: <Widget>[
                Text(Translations.textOf(context, "app.name"))
              ],
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title:
                  Text(Translations.textOf(context, "settings.checkForUpdate")),
              trailing: Icon(Icons.chevron_right),
              onTap: _checkAppInfo,
            ),
          ),
        ]),
      ),
    );
  }

  Future<bool> onWillPop() async {
    // 确认框
    String showConfirmDialog = await Dialogs.showConfirmDialog(
        context,
        Translations.textOf(context, LocaleConstant.settings_upgrade_cancel),
        null);
    Logs.info('showConfirmDialog = $showConfirmDialog');
    if ("true" == showConfirmDialog) {
      cancelRequests.cancel('_onWillPop');
      return true;
    }
    // You can do some work here.
    // Returning true allows the pop to happen, returning false prevents it.
    return false;
  }

  void _checkAppInfo() async {
    try {
//      if (1 + 1 == 2) {
//        String url = HttpRequests.rebuildUrl('https://f-droid.org/F-Droid.apk');
//        if (await canLaunch(url)) {
//          await launch(url);
//          return;
//        }
//      }

      // 版本校验
      String url = HttpConstant.url_settings_version_check
          .replaceAll("{version}", AppUtils.getCurrentVersion().toString());
      HttpResult httpResult = await HttpRequests.get(url, null, null);
      Logs.info("responseBody = ${httpResult.responseBody}");
      // ignore: null_aware_in_condition
      if (httpResult?.responseBody?.isEmpty) {
        Dialogs.showInfoDialog(_context,
            Translations.textOf(context, "settings.alreadyLatestVersion"));
        return;
      }

      AppVersionResult appVersionResult =
          AppVersionResult.fromJson(json.decode(httpResult?.responseBody));
      if (appVersionResult?.code == 200) {
        // 确认框
        String showConfirmDialog = await Dialogs.showConfirmDialog(
            context,
            Translations.textOf(
                context, LocaleConstant.settings_upgrade_dialog),
            appVersionResult.data.description.replaceAll('|', '\n'));
        Logs.info('showConfirmDialog = $showConfirmDialog');

        if ("true" == showConfirmDialog) {

          Directory downloadDir = await FileUtils.getDownloadDir();
          String fileName =
              '${downloadDir.path}/latest-app-${appVersionResult.data.versionCode}.apk';
          File file = File(fileName);
          if (file.existsSync()) {
            // 已经下载过，直接安装
            FileUtils.openFile(file);
            print('open file ${file.path}');
            return;
          }
          _isDownloading = true;
          Logs.info('cancelRequests is ${cancelRequests.token}');
          Dialogs.showProgress(_context,
              Translations.textOf(context, "settings.downloading"), onWillPop);
          // 下载
          HttpByteResult httpByteResult = await HttpRequests.getBytes(
              appVersionResult.data.fileUrl, null, null, (int sent, int total) {
            double percent = sent / total;
            _isDownloading = sent < total;
            print("_doDownloadRequest onReceiveProgress ${percent}%");
          }, cancelRequests);
          // 保存
          if (httpByteResult.responseBytes == null) {
            return;
          }
          print('donwload apk finished...');
          if (httpByteResult.responseBytes.isEmpty) {
            Dialogs.dismiss(_context);
            Dialogs.showInfoDialog(_context,
                Translations.textOf(context, "settings.alreadyLatestVersion"));
            return;
          }

          bool isSuccess =
              await FileUtils.write(file, httpByteResult.responseBytes);
          print('save file isSuccess = ${isSuccess} to ${file.path}');

          Dialogs.dismiss(_context);

          // 打开文件
          if (isSuccess) {
            FileUtils.openFile(file);
            print('open file ${file.path}');
          }
        }
      }
    } catch (e) {
      print(e);
    }
  }
}
