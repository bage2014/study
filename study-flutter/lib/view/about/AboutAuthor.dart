import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:timelines/timelines.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor> {
  List<String> list = [];

  @override
  Widget build(BuildContext context) {
    const double edgeLeft = 16.0;
    const double edgeRight = 16.0;
    _OrderInfo data = _data(1);
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: SingleChildScrollView(
        child: Container(
          alignment: Alignment.center,
          child: Column(children: <Widget>[
            Container(
              padding:
              const EdgeInsets.fromLTRB(edgeLeft, 16.0, edgeRight, 0.0),
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
              ), //            child: Text(Translations.textOf(context, "about.author.name")),
            ),
            Container(
              padding: const EdgeInsets.fromLTRB(edgeLeft, 0.0, edgeRight, 0.0),
              child: Text('陆瑞华', textScaleFactor: 1.4),
//            child: Text(Translations.textOf(context, "about.author.name")),
            ),
            Container(
              padding:
              const EdgeInsets.fromLTRB(edgeLeft, 16.0, edgeRight, 16.0),
              child: Text('上海某互联网，Java 研发工程师，5年研发服务端经验'),
//            child: Text(Translations.textOf(context, "about.author.name")),
            ),
            Card(
              margin: EdgeInsets.all(20.0),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: _OnTimeBar(driver: data.driverInfo),
                  ),
                  Divider(height: 1.0),
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: _OrderTitle(
                      orderInfo: data,
                    ),
                  ),
                  Divider(height: 1.0),
                  _DeliveryProcesses(processes: data.deliveryProcesses),
                ],
              ),
            ),
          ]),
        ),
      ),
    );
  }

  Future<Null> _onRefresh() async {
//    Map<String, dynamic> paramJson = new HashMap();
//    paramJson.putIfAbsent("targetPage", () => 1);
//    paramJson.putIfAbsent("pageSize", () => 100);
//    Map<String, String> param = new HashMap();
//    param.putIfAbsent("param", () => json.encode(paramJson));
//    print(json.encode(paramJson));
    list.add("hello");
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

class _OrderTitle extends StatelessWidget {
  const _OrderTitle({
    Key? key,
    required this.orderInfo,
  }) : super(key: key);

  final _OrderInfo orderInfo;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Text(
          'Delivery #${orderInfo.id}',
          style: TextStyle(
            fontWeight: FontWeight.bold,
          ),
        ),
        Spacer(),
        Text(
          '${orderInfo.date.day}/${orderInfo.date.month}/${orderInfo.date
              .year}',
          style: TextStyle(
            color: Color(0xffb6b2b2),
          ),
        ),
      ],
    );
  }
}

class _DeliveryProcesses extends StatelessWidget {
  const _DeliveryProcesses({Key? key, required this.processes})
      : super(key: key);

  final List<_DeliveryProcess> processes;

  @override
  Widget build(BuildContext context) {
    return DefaultTextStyle(
      style: TextStyle(
        color: Color(0xff9b9b9b),
        fontSize: 12.5,
      ),
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: FixedTimeline.tileBuilder(
          theme: TimelineThemeData(
            nodePosition: 0,
            color: Color(0xff989898),
            indicatorTheme: IndicatorThemeData(
              position: 0,
              size: 20.0,
            ),
            connectorTheme: ConnectorThemeData(
              thickness: 2.5,
            ),
          ),
          builder: TimelineTileBuilder.connected(
            connectionDirection: ConnectionDirection.before,
            itemCount: processes.length,
            contentsBuilder: (_, index) {
              return Padding(
                padding: EdgeInsets.only(left: 8.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(
                      processes[index].name,
                      style: DefaultTextStyle
                          .of(context)
                          .style
                          .copyWith(
                        fontSize: 18.0,
                      ),
                    ),
                    Text(processes[index].message.message),
                  ],
                ),
              );
            },
            indicatorBuilder: (_, index) {
              if (processes[index].isCompleted) {
                return DotIndicator(
                  color: Color(0xff66c97f),
                  child: Icon(
                    Icons.check,
                    color: Colors.white,
                    size: 12.0,
                  ),
                );
              } else {
                return OutlinedDotIndicator(
                  borderWidth: 2.5,
                );
              }
            },
            connectorBuilder: (_, index, ___) =>
                SolidLineConnector(
                  color: processes[index].isCompleted
                      ? Color(0xff66c97f)
                      : null,
                ),
          ),
        ),
      ),
    );
  }
}

class _OnTimeBar extends StatelessWidget {
  const _OnTimeBar({Key? key, required this.driver}) : super(key: key);

  final _DriverInfo driver;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        MaterialButton(
          onPressed: () {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text('On-time!'),
              ),
            );
          },
          elevation: 0,
          shape: StadiumBorder(),
          color: Color(0xff66c97f),
          textColor: Colors.white,
          child: Text('On-time'),
        ),
        Spacer(),
        Text(
          'Driver\n${driver.name}',
          textAlign: TextAlign.center,
        ),
        SizedBox(width: 12.0),
        Container(
          width: 40.0,
          height: 40.0,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            image: DecorationImage(
              fit: BoxFit.fitWidth,
              image: AssetImage("assets/images/logo128.png"),
              // image: NetworkImage(
              //   driver.thumbnailUrl,
              // ),
            ),
          ),
        ),
      ],
    );
  }
}

_OrderInfo _data(int id) =>
    _OrderInfo(
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
