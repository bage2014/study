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
  String _index = "";

  @override
  Widget build(BuildContext context) {
    String str = AppUtils.getArgs(context).toString();
    Logs.info("param index : $str");
    _index = str;
    initValues();

    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "env.edit.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        margin: EdgeInsets.only(left: 16),
        child: Column(children: <Widget>[
          Container(
            child: GestureDetector(
              onTap: () {
                List<String> list = [];
                list.add("http");
                list.add("https");
                Dialogs.showListBottomSheet(context, list).then((value) {
                  if(value == null){
                    return ;
                  }
                  HttpRequestCaches.setProtocol(
                      list[value ?? 0], _index);
                  initValues();
                });
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(Translations.textOf(
                      context, "env.edit.protocol")),
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
            child: GestureDetector(
              onTap: () {
                Dialogs.showInputDialog(
                        context,
                        Translations.textOf(context, "env.edit.host"),
                        '${_host}')
                    .then((value) {
                  if(value == null || value == ""){
                    return ;
                  }
                  HttpRequestCaches.setHost(value ?? "", _index);
                  initValues();
                });
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(Translations.textOf(context, "env.edit.host")),
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
            child: GestureDetector(
              onTap: () {
                Dialogs.showInputDialog(
                        context,
                        Translations.textOf(context, "env.edit.port"),
                        '${_port}')
                    .then((value) {
                  if(value == null || value == ""){
                    return ;
                  }
                  HttpRequestCaches.setPort(value ?? "0", _index);
                  initValues();
                });
              },
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(Translations.textOf(context, "env.edit.port")),
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

  void initValues() {
    setState(() {
      _protocol = HttpRequestCaches.getIndexProtocol(_index);
      _host = HttpRequestCaches.getIndexHost(_index);
      _port = HttpRequestCaches.getIndexPort(_index);
    });
  }
}
