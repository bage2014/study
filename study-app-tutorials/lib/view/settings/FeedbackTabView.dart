import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/dialog/Dialogs.dart';
import 'package:tutorials/component/event/EventBus.dart';
import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/constant/locale_constant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/model/AboutAuthorTab.dart';
import 'package:tutorials/model/AppFeedback.dart';
import 'package:tutorials/request/model/FeedbackQueryResult.dart';
import 'package:tutorials/view/settings/FeedbackUpdateEvent.dart';
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
        return _FeedbackTabView(false);
      case key_my:
        return _FeedbackTabView(true);
      default:
        return Text('');
    }
  }
}

class _FeedbackTabView extends StatefulWidget {
  late bool _isMe;

  _FeedbackTabView(bool isMe) {
    _isMe = isMe;
  }

  @override
  _FeedbackTabState createState() => new _FeedbackTabState(_isMe);
}

class _FeedbackTabState extends State<_FeedbackTabView> {
  List<AppFeedback> list = [];
  bool _isLoading = false;
  bool _isMe = false;

  _FeedbackTabState(bool isMe) {
    _isMe = isMe;
  }

  @override
  void initState() {
    showLoading();
    _onRefresh();
    EventBus.consume<FeedbackUpdateEvent>((event) {
      Logs.info('event = ${event.toString()}');
      _onRefresh();
    });
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
                            elevation: 2,
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
                                                "assets/images/user_null.png")),
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
                                                          '${appFeedback.fromUserName ?? '佚名'}',
                                                          overflow: TextOverflow
                                                              .ellipsis,
                                                          style: TextStyle(
                                                              color:
                                                                  Colors.black,
                                                              fontSize: 15)),
                                                    ),
                                                  ],
                                                )),
                                                _isMe
                                                    ? Expanded(
                                                        child: Column(
                                                            crossAxisAlignment:
                                                                CrossAxisAlignment
                                                                    .end,
                                                            children: <Widget>[
                                                              new GestureDetector(
                                                                  onTap: () {
                                                                    _delete(
                                                                        appFeedback);
                                                                  },
                                                                  child: Icon(
                                                                    Icons
                                                                        .delete,
                                                                    color: Colors
                                                                        .grey,
                                                                    size: 20.0,
                                                                  ))
                                                            ]),
                                                      )
                                                    : SizedBox.shrink(),
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
                                                        '${appFeedback.msgContent ?? ''}',
                                                        maxLines: 3,
                                                        style: TextStyle(
                                                            fontSize: 14,
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
                                                // Expanded(
                                                //     child: Container(
                                                //         margin: EdgeInsets.only(
                                                //             top: 8.0,
                                                //             right: 0.0),
                                                //         child: Row(
                                                //             crossAxisAlignment:
                                                //                 CrossAxisAlignment
                                                //                     .start,
                                                //             children: <Widget>[
                                                //               Icon(
                                                //                 Icons.thumb_up,
                                                //                 color:
                                                //                     Colors.grey,
                                                //                 size: 12.0,
                                                //               ),
                                                //               Text(
                                                //                   " 100 " +
                                                //                       '\t\t\t',
                                                //                   style: TextStyle(
                                                //                       fontSize:
                                                //                           12,
                                                //                       color: Colors
                                                //                           .grey)),
                                                //               Icon(
                                                //                 Icons
                                                //                     .thumb_down,
                                                //                 color:
                                                //                     Colors.grey,
                                                //                 size: 12.0,
                                                //               ),
                                                //               Text(" 5 ",
                                                //                   style: TextStyle(
                                                //                       fontSize:
                                                //                           12,
                                                //                       color: Colors
                                                //                           .grey)),
                                                //             ]))),
                                                Expanded(
                                                    child: Column(
                                                  crossAxisAlignment:
                                                      CrossAxisAlignment.end,
                                                  children: <Widget>[
                                                    Container(
                                                      margin: EdgeInsets.only(
                                                          top: 8.0, right: 8.0),
                                                      child: Text(
                                                        '${appFeedback.sendTime}',
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
    if (!mounted) {
      return ;
    }
    Map<String, dynamic> paramJson = new HashMap();
    paramJson.putIfAbsent("targetPage", () => 1);
    paramJson.putIfAbsent("pageSize", () => 100);
    if (_isMe) {
      paramJson.putIfAbsent("fromUserId", () => UserCaches.getUserId());
    }

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

  Future<Null> _delete(AppFeedback appFeedback) async {
    // 确认框
    String? showConfirmDialog = await Dialogs.showConfirmDialog(context,
        Translations.textOf(context, LocaleConstant.all_delete_confirm), null);
    if ("true" == showConfirmDialog) {
      Map<String, dynamic> param = new HashMap();
      param.putIfAbsent("param", () => appFeedback.id);
      showLoading();
      HttpRequests.post(
              HttpConstant.url_settings_app_feedback_delete, param, null)
          .then((result) {
        Logs.info('_delete responseBody=' + (result.responseBody ?? ""));
        _onRefresh();
      }).catchError((error) {
        print(error.toString());
        hideLoading();
      });
    }
  }

  hideLoading() {
    setState(() {
      _isLoading = false;
    });
  }

  showLoading() {
    setState(() {
      _isLoading = true;
    });
  }
}
