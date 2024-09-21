import 'package:flutter/material.dart';
import 'package:tutorials/component/cache/http_request_caches.dart';
import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';

class DevTool extends StatefulWidget {
  @override
  _DevTool createState() => new _DevTool();
}

class _DevTool extends State<DevTool> {

  String mockSwitch = "false";


  @override
  void initState() {
    super.initState();
    initData();
  }

  Future<void> initData() async {
    mockSwitch = await SettingCaches.getMockSwitch();
    setState(() {});
  }


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
              title: Text(Translations.textOf(context, "settings.devTool.mock")),
              trailing: Switch(
                // This bool value toggles the switch.
                value: mockSwitch == "true",
                activeColor: Colors.blue,
                onChanged: (bool value) {
                  mockSwitch = value ? "true" : "false";
                  // This is called when the user toggles the switch.
                  SettingCaches.cacheMockSwitch(value ? "true" : "false")
                      .then((result) {
                    setState(() {
                      Toasts.show(result
                          ? Translations.textOf(context, "all.save.success")
                          : Translations.textOf(context, "all.save.failure"));
                    });
                  });
                },
              ),
            ),
          ),

          Container(
            alignment: Alignment.center,
            child: ListTile(
              title: Text(Translations.textOf(context, "settings.devTool.env")),
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
