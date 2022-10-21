import 'package:flutter/material.dart';
import 'package:tutorials/component/cache/http_request_caches.dart';
import 'package:tutorials/component/dialog/dialogs.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/sp/shared_preference_helper.dart';
import 'package:tutorials/constant/sp_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/utils/app_utils.dart';

class EnvironmentEdit extends StatefulWidget {
  @override
  _EnvironmentEdit createState() => new _EnvironmentEdit();
}

class _EnvironmentEdit extends State<EnvironmentEdit> {
  String _protocol = "";
  String _host = "";
  String _port = "";

  @override
  Widget build(BuildContext context) {
    initValues();
    String str = AppUtils.getArgs(context).toString();
    Logs.info("param index : $str");


    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "env.edit.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        margin: EdgeInsets.only(left: 16),
        child: Column(children: <Widget>[
          Container(
            child: new GestureDetector(
              onTap: () {
                List<String> list = [];
                list.add("http");
                list.add("https");
                Dialogs.showListBottomSheet(context, list).then((value) => {
                      refreshState(SpConstant.protocol_key,
                          list[value == null ? 0 : value])
                    });
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(Translations.textOf(
                      context, "settings.devTool.protocol")),
                  Text('${_protocol}',
                      style: TextStyle(fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: Icon(
                      Icons.chevron_right,
                    ),
                    onPressed: () {},
                  ),
                ],
              ),
            ),
          ),
          Container(
            child: new GestureDetector(
              onTap: () {
                Dialogs.showInputDialog(
                        context,
                        Translations.textOf(context, "settings.devTool.host"),
                        '${_host}')
                    .then((value) => {
                          refreshState(
                              SpConstant.host_key, value == null ? "" : value)
                        });
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(Translations.textOf(context, "settings.devTool.host")),
                  Text('${_host}',
                      style: TextStyle(fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: Icon(
                      Icons.chevron_right,
                    ),
                    onPressed: () {},
                  ),
                ],
              ),
            ),
          ),
          Container(
            child: new GestureDetector(
              onTap: () {
                Dialogs.showInputDialog(
                        context,
                        Translations.textOf(context, "settings.devTool.port"),
                        '${_port}')
                    .then((value) => {
                          refreshState(
                              SpConstant.port_key, value == null ? "" : value)
                        });
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(Translations.textOf(context, "settings.devTool.port")),
                  Text('${_port}',
                      style: TextStyle(fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: Icon(
                      Icons.chevron_right,
                    ),
                    onPressed: () => {},
                  ),
                ],
              ),
            ),
          ),
        ]),
      ),
    );
  }

  void refreshState(String key, String value) async {
    bool res = await SharedPreferenceHelper.set(key, value);
    if (res) {
      setState(() {
        if (key == SpConstant.protocol_key) {
          _protocol = value;
        } else if (key == SpConstant.host_key) {
          _host = value;
        } else if (key == SpConstant.port_key) {
          _port = value;
        }
      });
      HttpRequestCaches.init();
    }
  }

  void initValues() {
    setState(() {
      _protocol = HttpRequestCaches.getProtocol();
      _host = HttpRequestCaches.getHost();
      _port = HttpRequestCaches.getPort();
    });
  }
}
