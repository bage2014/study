import 'dart:collection';
import 'dart:convert';

import 'package:app_lu_lu/component/cache/UserCaches.dart';
import 'package:app_lu_lu/component/http/HttpRequests.dart';
import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/constant/HttpConstant.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/AboutAuthorTab.dart';
import 'package:app_lu_lu/model/AppFeedback.dart';
import 'package:app_lu_lu/model/FeedbackQueryResult.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class FeedbackTabView extends StatelessWidget {
  FeedbackTabView({Key? key, required this.tab, required this.feedbacks})
      : super(key: key);

  TabTitle tab;
  List<AppFeedback> feedbacks;
  static const String key_all = 'all';
  static const String key_my = 'my';

  static List<TabTitle> init() {
    List<TabTitle> tabs = [];
    tabs.add(new TabTitle(key_all, '所有留言'));
    tabs.add(new TabTitle(key_my, '我的留言'));
    return tabs;
  }

  @override
  Widget build(BuildContext context) {
    switch (tab.key) {
      case key_all:
        return _FeedbackTabView();
      case key_my:
        return _FeedbackTabView();
      default:
        return Text('');
    }
  }
}

class _FeedbackTabView extends StatefulWidget {
  @override
  _FeedbackTabState createState() => new _FeedbackTabState();
}

class _FeedbackTabState extends State<_FeedbackTabView> {
  List<AppFeedback> list = [];
  bool _isLoading = false;

  @override
  void initState() {
    _isLoading = true;
    _onRefresh();
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: _onRefresh,
      child: Container(
        child: _isLoading
            ? Center(
                child: CircularProgressIndicator(
                backgroundColor: Colors.grey[200],
                valueColor: AlwaysStoppedAnimation(Colors.blue),
              ))
            : (list.length == 0
                ? Center(
                    child: Text(
                      Translations.textOf(context, "all.list.view.no.data"),
                      textAlign: TextAlign.center,
                    ),
                  )
                : ListView.separated(
                    itemCount: list.length,
                    shrinkWrap: true,
                    itemBuilder: (context, index) {
                      return new GestureDetector(
                          onTap: () {},
                          child: Card(
                            margin: EdgeInsets.all(8.0),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              children: <Widget>[
                                ClipOval(
                                  child: Image(
                                      image: AssetImage(
                                          "assets/images/logo128.png")),
                                ),
                                Column(
                                  //测试Row对齐方式，排除Column默认居中对齐的干扰
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: <Widget>[
                                    Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.start,
                                      children: <Widget>[
                                        Text("Bage lu ",
                                            style: TextStyle(
                                                fontSize: 17.0,
                                                fontWeight: FontWeight.bold)),
                                      ],
                                    ),
                                    Padding(
                                        padding: EdgeInsets.fromLTRB(
                                            0.0, 8.0, 0.0, 0.0),
                                        child: Container(
                                          constraints: BoxConstraints(
                                            maxWidth: 200,
                                          ),
                                          child: Text(
                                            "hello world, " * 8,
                                            maxLines: 3,
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                        )),
                                    Padding(
                                        padding: EdgeInsets.fromLTRB(
                                            0.0, 8.0, 0.0, 0.0),
                                        child: Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.spaceBetween,
                                          children: <Widget>[
                                            Row(
                                                mainAxisSize: MainAxisSize.min,
                                                children: <Widget>[
                                                  Icon(
                                                    Icons.favorite,
                                                    color: Colors.red,
                                                  ),
                                                  Text("4 "),
                                                ]),
                                            Row(
                                                mainAxisSize: MainAxisSize.min,
                                                children: <Widget>[
                                                  Icon(
                                                    Icons.access_time,
                                                    color: Colors.blue,
                                                  ),
                                                  Text("4 minute ago"),
                                                ])
                                          ],
                                        ))
                                  ],
                                )
                              ],
                            ),
                          ));
                    },
                    separatorBuilder: (context, index) => Divider(height: .0),
                  )),
      ),
    );
  }

  Future<Null> _onRefresh() async {
    Map<String, dynamic> paramJson = new HashMap();
    paramJson.putIfAbsent("fromUserId", () => UserCaches.getUserId());
    paramJson.putIfAbsent("targetPage", () => 1);
    paramJson.putIfAbsent("pageSize", () => 100);
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(paramJson));
    HttpRequests.get(HttpConstant.url_settings_app_feedback_query, param, null)
        .then((result) {
      hideLoading();
      Logs.info('_onRefresh responseBody=' + (result.responseBody ?? ""));
      setState(() {
        FeedbackQueryResult feedbackQueryResult = FeedbackQueryResult.fromJson(
            json.decode(result?.responseBody ?? ""));
        if (feedbackQueryResult.code == 200) {
          list = feedbackQueryResult.data ?? [];
        }
      });
    }).catchError((error) {
      print(error.toString());
      hideLoading();
    });
  }

  hideLoading() {
    setState(() {
      _isLoading = false;
    });
  }
}
