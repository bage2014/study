import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/AppVersionResult.dart';
import 'package:app_lu_lu/model/AppVersionsResult.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:timelines/timelines.dart';

class AboutVersions extends StatefulWidget {
  @override
  _AboutVersions createState() => new _AboutVersions();
}

class _AboutVersions extends State<AboutVersions> {
  List<AppVersion> list = [];

  @override
  void initState() {
      list = new AppVersionsResult().data;
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
          new SliverFixedExtentList(
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
              if (processes[index].versionCode==null) return Padding(
                padding: EdgeInsets.only(left: 8.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(
                      processes[index].versionName??'',
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
                          '${processes[index].versionName}',
                          style: DefaultTextStyle.of(context).style.copyWith(
                            fontSize: 20.0,
                          ),
                        ),
                        Spacer(),
                        Text(
                          '${processes[index].createTime?.day}/${processes[index].createTime?.month}/${processes[index].createTime?.year}',
                          style: TextStyle(
                            color: Color(0xffb6b2b2),
                          ),
                        ),
                      ],
                    ),
                    Padding(
                      padding: EdgeInsets.fromLTRB(0.0,8.0,0.0,0.0),
                      child: Text(processes[index].description??'',),
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
