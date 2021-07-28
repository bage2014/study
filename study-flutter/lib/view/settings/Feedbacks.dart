import 'dart:collection';
import 'dart:convert';

import 'package:app_lu_lu/component/cache/UserCaches.dart';
import 'package:app_lu_lu/component/http/HttpRequests.dart';
import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/constant/HttpConstant.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/AboutAuthorTab.dart';
import 'package:flutter/material.dart';

import 'FeedbackTabView.dart';

class Feedbacks extends StatefulWidget {
  @override
  _Feedbacks createState() => _Feedbacks();
}

class _Feedbacks extends State<Feedbacks> with SingleTickerProviderStateMixin {
  late TabController _tabController; //需要定义一个Controller
  List<TabTitle> tabs = [];

  void _insertFeedback() {
    Map<String, dynamic> paramJson = new HashMap();
    paramJson.putIfAbsent("fromUserId", () => UserCaches.getUserId());
    paramJson.putIfAbsent("targetPage", () => 1);
    paramJson.putIfAbsent("pageSize", () => 100);
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(paramJson));
    HttpRequests.post(
            HttpConstant.url_settings_app_feedback_insert, param, null)
        .then((result) {
      Logs.info('_insertFeedback responseBody=' + (result?.responseBody ?? ""));
      setState(() {
        // FeedbackQueryResult feedbackQueryResult =
        // FeedbackQueryResult.fromJson(json.decode(result?.responseBody ?? ""));
        // if (feedbackQueryResult.code == 200) {
        //   list = feedbackQueryResult.data ?? [];
        // }
      });
    }).catchError((error) {
      print(error.toString());
    });
  }

  @override
  void initState() {
    tabs = FeedbackTabView.init();
    _tabController = TabController(length: tabs.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "settings.feedbacks")),
        bottom: TabBar(
            //生成Tab菜单
            controller: _tabController,
            tabs: tabs
                .map((e) => Tab(
                      text: e.text,
                    ))
                .toList()),
      ),
      body: Container(
        alignment: Alignment.center,
        child:
        Card(
            elevation: 5,//阴影
            shape: const RoundedRectangleBorder(//形状
              //修改圆角
              borderRadius: BorderRadius.all(Radius.circular(10)),
            ),
            color: Colors.white, //颜色
            margin: EdgeInsets.all(20), //margin
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                const ListTile(
                  leading: Icon(Icons.android),
                  title: Text('标题'),
                  subtitle: Text('副标题'),
                  trailing: Icon(Icons.chevron_right),
                ),
                ButtonTheme.bar(
                  // make buttons use the appropriate styles for cards
                  child: ButtonBar(
                    children: <Widget>[
                      FlatButton(
                        child: const Text('选项一'),
                        onPressed: () {
                          /* ... */
                        },
                      ),
                      FlatButton(
                        child: const Text('选项二'),
                        onPressed: () {
                          /* ... */
                        },
                      ),
                    ],
                  ),
                ),
                Divider(),
                Padding(
                  padding: EdgeInsets.all(20),
                  child: Text("AAAAAAAAAAAA"),
                )
              ],
            )
        )

        // Column(children: <Widget>[
        //   Expanded(
        //     child: Container(
        //       child: TabBarView(
        //         controller: _tabController,
        //         children: tabs.map((e) {
        //           return Container(
        //             child:
        //             FeedbackTabView(
        //               tab: e,
        //               feedbacks: [],
        //             ),
        //           );
        //         }).toList(),
        //       ),
        //     ),
        //   ),
        // ]),

      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _insertFeedback,
        tooltip: '+',
        child: new Icon(Icons.add),
      ),
    );
  }
}
