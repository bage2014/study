import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/model/AppVersion.dart';
import 'package:tutorials/model/AppVersionsResult.dart';
import 'package:flutter/material.dart';
import 'package:timelines/timelines.dart';

class AboutVersions extends StatefulWidget {
  @override
  _AboutVersions createState() => new _AboutVersions();
}

class _AboutVersions extends State<AboutVersions> {
  List<AppVersion> list = [];
  bool isLoading = true;

  @override
  void initState() {
    _onRefresh();
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: CustomScrollView(
        slivers: <Widget>[
          //AppBar，包含一个导航栏
          SliverAppBar(
            pinned: true,
            expandedHeight: 250.0,
            flexibleSpace: FlexibleSpaceBar(
              title: Text(Translations.textOf(context, "about.versions")),
              background: Image.asset("assets/images/logo128.png"),
            ),
          ),
          //List
          isLoading
              ? SliverToBoxAdapter(
                  child: LinearProgressIndicator(
                    backgroundColor: Colors.grey[200],
                    valueColor: AlwaysStoppedAnimation(Colors.blue),
                  ),
                )
              : new SliverFixedExtentList(
                  itemExtent: 1000.0,
                  delegate: new SliverChildBuilderDelegate(
                      (BuildContext context, int index) {
                    //创建列表项
                    return _DeliveryProcesses(processes: list);
                  }, childCount: 1 //列表项
                      ),
                ),
        ],
      ),
    );
  }

  void hideLoading() {
    setState(() {
      isLoading = false;
    });
  }

  Future<Null> _onRefresh() async {
    isLoading = true;
    Map<String, String> param = new HashMap();
    HttpRequests.get(HttpConstant.url_settings_app_versions, param, null)
        .then((result) {
      Logs.info('_onRefresh responseBody=' + (result?.responseBody ?? ""));
      hideLoading();
      setState(() {
        AppVersionsResult response =
            AppVersionsResult.fromJson(json.decode(result?.responseBody ?? ""));
        if (response.code == 200) {
          list = response.data ?? [];
          if (list.length > 0) {
            AppVersion value = new AppVersion();
            value.versionName = Translations.textOf(context, 'about.version.start');
            list.insert(0, value);
          }
        }
      });
    }).catchError((error) {
      print(error.toString());
      hideLoading();
    });
  }
}

class _DeliveryProcesses extends StatelessWidget {
  const _DeliveryProcesses({Key? key, required this.processes})
      : super(key: key);

  final List<AppVersion> processes;

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
              var version = processes[processes.length - 1 - index];
              if (version.versionCode == null)
                return Padding(
                  padding: EdgeInsets.only(left: 8.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        version.versionName ?? '',
                        style: DefaultTextStyle.of(context).style.copyWith(
                              fontSize: 18.0,
                            ),
                      ),
                    ],
                  ),
                );

              return Padding(
                padding: EdgeInsets.only(left: 8.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Row(
                      children: [
                        Text(
                          '${Translations.textOf(context, 'about.version.name')} ${version.versionName}',
                          style: DefaultTextStyle.of(context).style.copyWith(
                                fontSize: 20.0,
                              ),
                        ),
                        Spacer(),
                        Text(
                          '${version.createTime?.year}-${version.createTime?.month}-${version.createTime?.day}',
                          style: TextStyle(
                            color: Color(0xffb6b2b2),
                          ),
                        ),
                      ],
                    ),
                    Padding(
                      padding: EdgeInsets.fromLTRB(0.0, 0.0, 0.0, 0.0),
                      child: Text(
                        version.description?.replaceAll('|', '\n') ?? '',
                      ),
                    ),
                  ],
                ),
              );
            },
            indicatorBuilder: (_, index) {
              return DotIndicator(
                color: Color(0xff66c97f),
                child: Icon(
                  Icons.check,
                  color: Colors.white,
                  size: 12.0,
                ),
              );
            },
            connectorBuilder: (_, index, ___) => SolidLineConnector(
              color: Color(0xff66c97f),
            ),
          ),
        ),
      ),
    );
  }
}
