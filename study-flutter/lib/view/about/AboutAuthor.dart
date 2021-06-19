import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:timelines/timelines.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor>
    with SingleTickerProviderStateMixin {
  List<_OrderInfo> list = [];
  late TabController _tabController; //需要定义一个Controller
  List tabs = ["新闻", "历史", "图片"];

  @override
  void initState() {
    list.add(_data(list.length + 1));
    _tabController = TabController(length: tabs.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    const double edgeLeft = 16.0;
    const double edgeRight = 16.0;
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            padding: const EdgeInsets.fromLTRB(edgeLeft, 16.0, edgeRight, 0.0),
            child: ClipOval(
              child: CachedNetworkImage(
                imageUrl:
                    "https://avatars.githubusercontent.com/u/18094768?v=4",
                placeholder: (context, url) => CircularProgressIndicator(),
                errorWidget: (context, url, error) =>
                    Image(image: AssetImage("assets/images/user_null.png")),
                height: 128,
                width: 128,
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(edgeLeft, 0.0, edgeRight, 0.0),
            child: Text('陆瑞华', textScaleFactor: 1.4),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(edgeLeft, 0.0, edgeRight, 0.0),
            child: TabBar(
                //生成Tab菜单
                controller: _tabController,
                tabs: tabs.map((e) => Tab(text: e)).toList()),
          ),
          Card(
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
          ),
          Expanded(
            child: RefreshIndicator(
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
                                    style: DefaultTextStyle.of(context)
                                        .style
                                        .copyWith(
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
            ),
          ),
        ]),
      ),
    );
  }

  Future<Null> _onRefresh() async {
    setState(() {
      list.add(_data(list.length + 1));
    });

//    Map<String, dynamic> paramJson = new HashMap();
//    paramJson.putIfAbsent("targetPage", () => 1);
//    paramJson.putIfAbsent("pageSize", () => 100);
//    Map<String, String> param = new HashMap();
//    param.putIfAbsent("param", () => json.encode(paramJson));
//    print(json.encode(paramJson));
//     list.add("hello");
//    HttpRequests.get(HttpConstant.url_tv_query_page, param, null)
//        .then((result) {
//      Logs.info('_onRefresh responseBody=' + result?.responseBody);
//      setState(() {
//        QueryTvResult tvResult =
//        QueryTvResult.fromJson(json.decode(result?.responseBody));
//        if (tvResult.code == 200) {
//          list = tvResult.data;
//        }
//      });
//    }).catchError((error) {
//      print(error.toString());
//    });
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
