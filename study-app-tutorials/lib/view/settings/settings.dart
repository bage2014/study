import 'dart:io';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/event/event_bus.dart';
import 'package:tutorials/component/picker/file_picker.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/locale/supported_locales.dart';
import 'package:tutorials/model/app_version.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/dialog/dialogs.dart';
import 'package:tutorials/component/http/cancel_requests.dart';
import 'package:tutorials/component/http/http_byte_result.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/locale_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_param.dart';
import 'package:tutorials/request/model/setting/app_version_check_request_result.dart';
import 'package:tutorials/request/setting_request.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:tutorials/utils/file_utils.dart';
import 'package:tutorials/view/home/locale_update_event.dart';
import 'package:url_launcher/url_launcher.dart';

class Settings extends StatefulWidget {
  GlobalKey<_Settings> changeLocalizationStateKey = new GlobalKey<_Settings>();
  @override
  _Settings createState() => new _Settings();
}

class _Settings extends State<Settings> {
  late BuildContext _context;
  bool _isLoading = false;
  double? _value;
  CancelRequests cancelRequests = CancelRequests();
  late AppVersion _currentVersionInfo;

  @override
  void initState() {
    _currentVersionInfo = AppVersion.getCurrentVersionInfo();
  }

  @override
  Widget build(BuildContext context) {
    _context = context;
    return Stack(
      alignment: Alignment.center, //指定未定位或部分定位widget的对齐方式
      children: <Widget>[
        Scaffold(
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
                      child: const Image(
                          image: AssetImage("assets/images/logo128.png")),
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
                        style: const TextStyle(
                            fontSize: 17.0, fontWeight: FontWeight.bold))
                  ],
                ),
              ),
              Container(
                alignment: Alignment.center,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text(_currentVersionInfo?.versionName ?? '')
                  ],
                ),
              ),
              Container(
                alignment: Alignment.center,
                child: ListTile(
                  title: Text(
                      Translations.textOf(context, "settings.checkForUpdate")),
                  trailing: Icon(Icons.chevron_right),
                  onTap: _checkAppUpdateInfo,
                ),
              ),
              Container(
                alignment: Alignment.center,
                child: ListTile(
                  title:
                      Text(Translations.textOf(context, "settings.feedbacks")),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.of(context).pushNamed(
                        RouteNameConstant.route_name_setting_feedbacks);
                  },
                ),
              ),
              Container(
                alignment: Alignment.center,
                child: ListTile(
                  title: Text(
                      Translations.textOf(context, "settings.dir.setting")),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    downloadDirSetting();
                  },
                ),
              ),
              Container(
                alignment: Alignment.center,
                child: ListTile(
                  title: Text(
                      Translations.textOf(context, "settings.lang.setting")),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    langSetting();
                  },
                ),
              ),

            ]),
          ),
        ),
        Container(
          child: _isLoading
              ? Container(
                  color: Colors.black54.withOpacity(0.5),
                  width: double.infinity,
                )
              : null,
        ),
        Container(
          child: _isLoading
              ? CircularProgressIndicator(
                  value: _value,
                  backgroundColor: Colors.grey[200],
                  valueColor: const AlwaysStoppedAnimation(Colors.blue),
                )
              : null,
        ),
      ],
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

  showLoading({double? value}) {
    setState(() {
      _value = value;
      _isLoading = true;
    });
  }

  hideLoading() {
    setState(() {
      _value = null;
      _isLoading = false;
    });
  }

  void _checkAppUpdateInfo() async {
    showLoading();
    // 版本校验
    SettingRequests.checkVersion(AppVersionCheckRequestParam()).then((result) {
      Logs.info('checkVersion result=' + (result.toString() ?? ""));
      hideLoading();
      if (result.common.code == ErrorCodeConstant.success) {
        showUpdateDialog(result);
      } else {
        Toasts.show(result.common.message);
      }
    }).catchError((error) {
      Logs.info(error.toString());
      hideLoading();
    });
  }

  void showUpdateDialog(AppVersionCheckRequestResult result) async {
    // 确认框
    String? showConfirmDialog = await Dialogs.showConfirmDialog(
        context,
        Translations.textOf(context, LocaleConstant.settings_upgrade_dialog),
        result?.description?.replaceAll('|', '\n'));
    Logs.info('showConfirmDialog = $showConfirmDialog');

    if ("true" == showConfirmDialog) {
      showUpdateSelector(result);
    }
  }

  void showUpdateSelector(AppVersionCheckRequestResult result) async {
    List<String> contents = [
      Translations.textOf(context, "settings.upgrade.open.by.browser"),
      Translations.textOf(context, "settings.upgrade.open.by.app"),
      Translations.textOf(context, "settings.upgrade.open.cancel")
    ];
    int? index = await Dialogs.showButtonSelectDialog(context, contents, null);
    Logs.info('select index = $index');

    if (index == null || index == contents.length - 1) {
      // Cancel
      return;
    }

    if (index == 0) {
      // open with browser
      String url = HttpRequests.rebuildUrl(result?.fileUrl ?? "");
      if (await canLaunch(url)) {
        await launch(url);
        return;
      }
    } else if (index == 1) {
      // download from app
      downloadFromApp(result);
    }
  }

  void downloadFromApp(AppVersionCheckRequestResult result) async {
    String downloadDir = await FileUtils.getDownloadDirectory();
    String fileName = '${downloadDir}/latest-app-${result?.versionCode}.apk';
    File file = File(fileName);
    if (file.existsSync()) {
      // Logs.info('delete is ${file.path}');
      // file.delete();
      // 已经下载过，直接安装
      Logs.info('open file ${file.path}');
      FileUtils.openFile(file);
      return;
    }

    Logs.info('cancelRequests is ${cancelRequests.token}');
    showLoading(value: _value);
    // 下载
    HttpByteResult httpByteResult = await HttpRequests.getBytes(
        result?.fileUrl ?? "", null, null, (int sent, int total) {
      setState(() {
        _value = sent / total;
      });
      Logs.info("_doDownloadRequest onReceiveProgress $sent / $total");
      Logs.info("_doDownloadRequest onReceiveProgress value = $_value");
    }, cancelRequests);

    // 保存
    if (httpByteResult.responseBytes == null) {
      return;
    }
    Logs.info('download apk finished...');
    if (httpByteResult.responseBytes.isEmpty) {
      Dialogs.dismiss(_context);
      Dialogs.showInfoDialog(_context, null,
          Translations.textOf(context, "settings.alreadyLatestVersion"));
      return;
    }

    bool isSuccess = await FileUtils.write(file, httpByteResult.responseBytes);
    Logs.info('save file isSuccess = ${isSuccess} to ${file.path}');

    hideLoading();

    // 打开文件
    if (isSuccess) {
      FileUtils.openFile(file);
      Logs.info('open file ${file.path}');
    }
  }

  Future<void> downloadDirSetting() async {
    String? dir = await FilePicker.pickDirectory();
    if (dir != null) {
      SettingCaches.cacheDownloadDirectory(dir);
      Toasts.show(Translations.textOf(
          context, "settings.download.dir.setting.success"));
    }
  }

  void langSetting() async {
    List<String> contents = [
      Translations.textOf(context, "settings.lang.zh"),
      Translations.textOf(context, "settings.lang.en"),
      Translations.textOf(context, "settings.lang.cancel")
    ];
    // 确认框
    int? index = await Dialogs.showButtonSelectDialog(context, contents, null);
    Logs.info('select index = $index');

    if (index == null || index == contents.length - 1) {
      // Cancel
      return;
    }

    if (index == 0) {
      // zh
      Translations.load(SupportedLocales.zhLocale);
    } else if (index == 1) {
      // en
      Translations.load(SupportedLocales.enLocale);
    }

    LocaleUpdateEvent event = LocaleUpdateEvent();
    event.data = index.toString();
    EventBus.publish(event);

    setState(() {
      Toasts.show('Success');
    });
  }


  Future<void> _nameCard() async {
    AppUtils.toPage(context, RouteNameConstant.route_name_name_card);
  }
}
