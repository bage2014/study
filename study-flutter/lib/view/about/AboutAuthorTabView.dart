import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/AboutAuthorTab.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AboutAuthorTabView extends StatelessWidget {
  const AboutAuthorTabView({Key? key, required this.tab}) : super(key: key);

  final AboutAuthorTab tab;
  static const String key_basic = 'basic';
  static const String key_activity = 'activity';

  static List<AboutAuthorTab> init() {
    List<AboutAuthorTab> tabs = [];
    tabs.add(new AboutAuthorTab(key_basic, 'Basic'));
    tabs.add(new AboutAuthorTab(key_activity, 'Activity'));
    return tabs;
  }

  @override
  Widget build(BuildContext context) {
    switch (tab.key) {
      case key_basic:
        return _AboutAuthorTabViewBasic();
      case key_activity:
        return _AboutAuthorTabViewActivity();
      default:
        return Text('');
    }
  }
}

class _AboutAuthorTabViewBasic extends StatelessWidget {
  const _AboutAuthorTabViewBasic({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(8.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          ListTile(
            leading: Icon(Icons.mail_outline),
            title: Text('893542907@qq.com'),
            onTap: () {},
          ),
          ListTile(
            leading: Icon(Icons.link_outlined),
            title: Text('https://github.com/bage2014'),
            onTap: () {},
          ),
          ListTile(
            leading: Icon(Icons.info_outline),
            title: Text('来自广西壮族自治区，现就职于上海某互联网公司，Java 研发工程师，5年研发服务端经验'),
            onTap: () {},
          ),
        ],
      ),
    );
  }
}

class _AboutAuthorTabViewActivity extends StatefulWidget {
  @override
  _AboutAuthorTabViewActivityState createState() =>
      new _AboutAuthorTabViewActivityState();
}

class _AboutAuthorTabViewActivityState
    extends State<_AboutAuthorTabViewActivity> {
  List<_OrderInfo> list = [];

  @override
  void initState() {
    list.add(_data(list.length + 1));
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: _onRefresh,
      child: list.length == 0
          ? Center(
              child: Text(
                Translations.textOf(context, "all.list.view.no.data"),
                textAlign: TextAlign.center,
              ),
            )
          : ListView.separated(
              itemCount: list.length,
              itemBuilder: (context, index) {
                _OrderInfo data = list[index];
                return new GestureDetector(
                  onTap: () {},
                  child: Card(
                    margin: EdgeInsets.all(8.0),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Padding(
                          padding: const EdgeInsets.all(20.0),
                          child: _OnTimeBar(driver: data),
                        ),
                        Divider(height: 1.0),
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Text(
                            data.driverInfo.name,
                            style: DefaultTextStyle.of(context).style.copyWith(
                                  fontSize: 14.0,
                                ),
                          ),
                        )
                      ],
                    ),
                  ),
                );
              },
              separatorBuilder: (context, index) => Divider(height: .0),
            ),
    );
  }

  Future<Null> _onRefresh() async {
    setState(() {
      list.add(_data(list.length + 1));
    });
  }
}

class _OnTimeBar extends StatelessWidget {
  const _OnTimeBar({Key? key, required this.driver}) : super(key: key);

  final _OrderInfo driver;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        MaterialButton(
          onPressed: () {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(driver.driverInfo.name),
              ),
            );
          },
          elevation: 0,
          shape: StadiumBorder(),
          color: Color(0xff66c97f),
          textColor: Colors.white,
          child: Text('Activity'),
        ),
        Spacer(),
        Text(
          '${driver.date.day}/${driver.date.month}/${driver.date.year}',
          style: TextStyle(
            color: Color(0xffb6b2b2),
          ),
        ),
      ],
    );
  }
}

_OrderInfo _data(int id) => _OrderInfo(
      id: id,
      date: DateTime.now(),
      driverInfo: _DriverInfo(
        name: 'Philipe',
        thumbnailUrl:
            'https://i.pinimg.com/originals/08/45/81/084581e3155d339376bf1d0e17979dc6.jpg',
      ),
      deliveryProcesses: [
        _DeliveryProcess(
          'Event3',
          _DeliveryMessage('13:00pm', 'This is a message for test'),
        ),
        _DeliveryProcess(
          'Event2',
          _DeliveryMessage('13:00pm', 'This is a message for test'),
        ),
        _DeliveryProcess(
          'Event1',
          _DeliveryMessage('11:30am', 'Reached halfway mark'),
        ),
        _DeliveryProcess.complete(),
      ],
    );

class _OrderInfo {
  const _OrderInfo({
    required this.id,
    required this.date,
    required this.driverInfo,
    required this.deliveryProcesses,
  });

  final int id;
  final DateTime date;
  final _DriverInfo driverInfo;
  final List<_DeliveryProcess> deliveryProcesses;
}

class _DriverInfo {
  const _DriverInfo({
    required this.name,
    required this.thumbnailUrl,
  });

  final String name;
  final String thumbnailUrl;
}

class _DeliveryProcess {
  const _DeliveryProcess(this.name, this.message);

  const _DeliveryProcess.complete()
      : this.name = 'Start',
        this.message = const _DeliveryMessage('', '');

  final String name;
  final _DeliveryMessage message;

  bool get isCompleted => true;
}

class _DeliveryMessage {
  const _DeliveryMessage(this.createdAt, this.message);

  final String createdAt; // final DateTime createdAt;
  final String message;

  @override
  String toString() {
    return '$createdAt $message';
  }
}
