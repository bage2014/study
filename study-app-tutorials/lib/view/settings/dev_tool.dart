import 'package:flutter/material.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';

class DevTool extends StatefulWidget {
  @override
  _DevTool createState() => new _DevTool();
}

class _DevTool extends State<DevTool> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "settings.devTool.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        margin: EdgeInsets.only(left: 16),
        child: Column(children: <Widget>[
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text('Environment'),
              trailing: Icon(Icons.chevron_right),
              onTap: (){
                Navigator.of(context).pushNamed(
                    RouteNameConstant.route_name_env);
              },
            ),
          ),
        ]),
      ),
    );
  }
}
