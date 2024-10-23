import 'dart:collection';

import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/app_versions_request.dart';
import 'package:flutter/material.dart';
import 'package:timelines/timelines.dart';
import 'package:tutorials/request/model/app_versions_result.dart';
import 'package:tutorials/request/model/common/default_page_query_request_param.dart';
import 'package:tutorials/utils/date_time_utils.dart';

class AboutVersions extends StatefulWidget {
  @override
  _AboutVersions createState() => new _AboutVersions();
}

class _AboutVersions extends State<AboutVersions> {
  List<Data> list = [];
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

    AppVersionsRequests.getVersions(DefaultPageQueryRequestParam()).then((result) {
      Logs.info('getVersions result=' + (result.toString() ?? ""));
      hideLoading();
      if (result.code == ErrorCodeConstant.success) {
        setState(() {
            list = result.data ?? [];
        });
      } else {
        Toasts.show(result.msg??'未知错误');
      }
    }).catchError((error) {
      Logs.info(error.toString());
      hideLoading();
      Toasts.show(error.msg??'未知错误');
    });

  }
}

class _DeliveryProcesses extends StatelessWidget {
  const _DeliveryProcesses({Key? key, required this.processes})
      : super(key: key);

  final List<Data> processes;

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
                  padding: EdgeInsets.only(left: 12.0),
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

              var createTime =  DateTimeUtils.parse(version.createTime);
              return Padding(
                padding: EdgeInsets.only(left: 12.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Row(
                      children: [
                        Text(
                          '${version.versionName}',
                          style: DefaultTextStyle.of(context).style.copyWith(
                                fontSize: 20.0,
                              ),
                        ),
                        Spacer(),
                        Text(
                          '${createTime?.year}-${createTime?.month}-${createTime?.day}',
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
