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
                      AppFeedback appFeedback = list[index];
                      return new GestureDetector(
                          onTap: () {},
                          child: Card(
                            clipBehavior: Clip.antiAlias,
                            elevation: 4,
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(8.0)),
                            margin: EdgeInsets.symmetric(
                                horizontal: 16, vertical: 6),
                            child: Container(
                              child: Row(
                                children: <Widget>[
                                  Container(
                                    padding:
                                        EdgeInsets.only(left: 10, right: 5),
                                    width: 100.0,
                                    child: AspectRatio(
                                      aspectRatio: 1.2,
                                      child: ClipOval(
                                        child: Image(
                                            width: 48.0,
                                            height: 48.0,
                                            image: AssetImage(
                                                "assets/images/logo128.png")),
                                      ),
                                    ),
                                  ),
                                  Expanded(
                                    flex: 1,
                                    child: Container(
                                        padding: EdgeInsets.symmetric(
                                            horizontal: 5, vertical: 10),
                                        color: Colors.white,
                                        child: Column(
                                          children: <Widget>[
                                            Row(
                                              children: <Widget>[
                                                Expanded(
                                                    child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.start,
                                                  children: <Widget>[
                                                    Container(
                                                      child: Text(
                                                          '${appFeedback.fromUserId}',
                                                          overflow: TextOverflow
                                                              .ellipsis,
                                                          style: TextStyle(
                                                              color:
                                                                  Colors.black,
                                                              fontSize: 15)),
                                                    ),
                                                  ],
                                                )),
                                              ],
                                            ),
                                            SizedBox(
                                              height: 8,
                                            ),
                                            Row(
                                              children: <Widget>[
                                                Expanded(
                                                    child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.start,
                                                  children: <Widget>[
                                                    Container(
                                                      child: Text(
                                                        '${appFeedback.msgContent}',
                                                        maxLines: 3,
                                                        style: TextStyle(
                                                            fontSize: 12,
                                                            color:
                                                                Colors.black87),
                                                        overflow: TextOverflow
                                                            .ellipsis,
                                                      ),
                                                    ),
                                                  ],
                                                )),
                                              ],
                                            ),
                                            Row(
                                              children: <Widget>[
                                                Expanded(
                                                    child: Container(
                                                        margin: EdgeInsets.only(
                                                            top: 8.0,
                                                            right: 0.0),
                                                        child: Row(
                                                            crossAxisAlignment:
                                                                CrossAxisAlignment
                                                                    .start,
                                                            children: <Widget>[
                                                              Icon(
                                                                Icons.thumb_up,
                                                                color:
                                                                    Colors.grey,
                                                              ),
                                                              Text(" 100 " +
                                                                  '\t\t\t'),
                                                              Icon(
                                                                Icons
                                                                    .thumb_down,
                                                                color:
                                                                    Colors.grey,
                                                              ),
                                                              Text(" 5 "),
                                                            ]))),
                                                Expanded(
                                                    child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.end,
                                                  children: <Widget>[
                                                    Container(
                                                      margin: EdgeInsets.only(
                                                          top: 8.0, right: 8.0),
                                                      child: Text(
                                                        '${appFeedback.createTime}',
                                                        style: TextStyle(
                                                            fontSize: 12,
                                                            color: Colors.grey),
                                                      ),
                                                    ),
                                                  ],
                                                )),
                                              ],
                                            ),
                                          ],
                                        )),
                                  ),
                                ],
                              ),
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
