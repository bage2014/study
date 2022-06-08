import 'dart:convert';
import 'dart:io';

import 'package:tutorials/model/AppVersion.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/dialog/Dialogs.dart';
import 'package:tutorials/component/http/CancelRequests.dart';
import 'package:tutorials/component/http/HttpByteResult.dart';
import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/component/http/HttpResult.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/HttpConstant.dart';
import 'package:tutorials/constant/LocaleConstant.dart';
import 'package:tutorials/constant/RouteNameConstant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/model/AppVersionResult.dart';
import 'package:tutorials/utils/AppUtils.dart';
import 'package:tutorials/utils/FileUtils.dart';
import 'package:url_launcher/url_launcher.dart';

class Settings extends StatefulWidget {
  @override
  _Settings createState() => new _Settings();
}

class _Settings extends State<Settings> {
  late BuildContext _context;
  bool _isDownloading = false;
  CancelRequests cancelRequests = CancelRequests();
  late AppVersion _currentVersionInfo;

  @override
  void initState() {
    _currentVersionInfo = AppVersion.getCurrentVersionInfo();
  }

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
            padding: const EdgeInsets.fromLTRB(0.0, 0.0, 0.0, 0.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
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
              title:
                  Text(Translations.textOf(context, "settings.checkForUpdate")),
              trailing: Icon(Icons.chevron_right),
              onTap: _checkAppInfo,
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title:
              Text(Translations.textOf(context, "settings.feedbacks")),
              trailing: Icon(Icons.chevron_right),
              onTap: (){
                Navigator.of(context)
                    .pushNamed(RouteNameConstant.route_name_setting_feedbacks);
              },
            ),
          ),
        ]),
      ),
    );
  }

  Future<bool> onWillPop() async {
    // 确认框
    String? showConfirmDialog = await Dialogs.showConfirmDialog(
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
      // 版本校验
      String url = HttpConstant.url_settings_version_check
          .replaceAll("{version}", AppUtils.getCurrentVersion().toString());
      HttpResult httpResult = await HttpRequests.get(url, null, null);
      String? temp = httpResult?.responseBody;
      String responseBody = temp == null ? "" : temp;
      Logs.info("responseBody = ${responseBody}");
      // ignore: null_aware_in_condition
      if (responseBody.isEmpty) {
        return;
      }

      AppVersionResult appVersionResult =
          AppVersionResult.fromJson(json.decode(responseBody));
      if (appVersionResult?.code == 200) {
        // 确认框
        String? showConfirmDialog = await Dialogs.showConfirmDialog(
            context,
            Translations.textOf(
                context, LocaleConstant.settings_upgrade_dialog),
            appVersionResult.data?.description?.replaceAll('|', '\n'));
        Logs.info('showConfirmDialog = $showConfirmDialog');

        if ("true" == showConfirmDialog) {
          List<String> contents = [
            Translations.textOf(context, "settings.upgrade.open.by.browser"),
            Translations.textOf(context, "settings.upgrade.open.by.app"),
            Translations.textOf(context, "settings.upgrade.open.cancel")
          ];
          int? index = await Dialogs.showButtonSelectDialog(context, contents,null);
          Logs.info('index = $index');

          if (index == null || index == contents.length - 1) {
            // Cancel
            return;
          }

          if (index == 0) {
            // open with browser
            String url =
                HttpRequests.rebuildUrl(appVersionResult?.data?.fileUrl ?? "");
            if (await canLaunch(url)) {
              await launch(url);
              return;
            }
          }

          Directory downloadDir = await FileUtils.getDownloadDir();
          String fileName =
              '${downloadDir.path}/latest-app-${appVersionResult.data?.versionCode}.apk';
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
              appVersionResult?.data?.fileUrl ?? "", null, null,
              (int sent, int total) {
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
                null,
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
