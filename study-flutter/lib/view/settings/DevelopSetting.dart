import 'package:flutter/material.dart';
import 'package:flutter_app_upgrade/flutter_app_upgrade.dart';

class DevelopSetting extends StatefulWidget {
  @override
  _DevelopSetting createState() => new _DevelopSetting();
}

class _DevelopSetting extends State<DevelopSetting> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Develop Setting"),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[

          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text("Host"),
              trailing: Icon(Icons.chevron_right),
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text("port"),
              trailing: Icon(Icons.chevron_right),
              onTap: () {},
            ),
          ),
        ]),
      ),
    );
  }

}
