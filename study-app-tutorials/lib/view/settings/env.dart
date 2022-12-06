import 'package:flutter/material.dart';
import 'package:tutorials/component/cache/http_request_caches.dart';
import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/dialog/dialogs.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/sp/shared_preference_helper.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/constant/sp_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/utils/app_utils.dart';

class Environment extends StatefulWidget {
  @override
  _Environment createState() => new _Environment();
}

class _Environment extends State<Environment> {
  static List<FMRadioModel> _list = [];

  int groupValue = 1;
  String mock_switch = "false";

  @override
  void initState() {
    super.initState();
    initData();
  }

  Future<void> initData() async {
    groupValue = int.parse(HttpRequestCaches.getIndex());
    _list = [];
    _list.add(FMRadioModel(1, "Development"));
    _list.add(FMRadioModel(2, "Test"));
    _list.add(FMRadioModel(3, "Production"));

    mock_switch = await SettingCaches.getMockSwitch();

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    _list[0] = (FMRadioModel(1, Translations.textOf(context, "env.dev")));
    _list[1] = (FMRadioModel(2, Translations.textOf(context, "env.test")));
    _list[2] = (FMRadioModel(3, Translations.textOf(context, "env.prod")));

    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "env.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            padding: const EdgeInsets.fromLTRB(20.0, 40.0, 0.0, 0.0),
            child: Row(
              children: [
                Text(
                  'Mockbale',
                  style: TextStyle(
                    fontSize: 20,
                    color: Colors.black,
                    fontStyle: FontStyle.normal,
                    fontWeight: FontWeight.w400,
                  ),
                ),
                Switch(
                  // This bool value toggles the switch.
                  value: mock_switch == "true",
                  activeColor: Colors.blue,
                  onChanged: (bool value) {
                    mock_switch = value ? "true" : "false";
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
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 0.0),
            child: Text(
              'Env',
              style: TextStyle(
                fontSize: 20,
                color: Colors.black,
                fontStyle: FontStyle.normal,
                fontWeight: FontWeight.w400,
              ),
            ),
          ),

          Expanded(
            child: ListView.builder(
              itemCount: _list.length,
              itemBuilder: (context, index) {
                FMRadioModel model = _list[index];
                return _buildRow(model);
              },
            ),
          ),
        ]),
      ),
    );
  }

  Row _buildRow(FMRadioModel model) {
    return Row(
      children: [
        _radio(model),
        GestureDetector(
          child: Text(model.text),
          onTap: () {
            go(model.index);
          },
        ),
      ],
    );
  }

  Radio _radio(FMRadioModel model) {
    return Radio(
        value: model.index,
        groupValue: groupValue,
        onChanged: (index) {
          groupValue = index;
          Logs.info('index : ${index}');
          HttpRequestCaches.setIndex(index.toString()).then((value) {
            setState(() {
              Toasts.show(value
                  ? Translations.textOf(context, "all.save.success")
                  : Translations.textOf(context, "all.save.failure"));
            });
          });
        });
  }

  void go(int index) {
    String str = index.toString();
    Logs.info("json : $str");
    AppUtils.toPage(context, RouteNameConstant.route_name_env_edit, args: str);
  }
}

class FMRadioModel extends Object {
  int index;
  String text;

  FMRadioModel(this.index, this.text);
}
