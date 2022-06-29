import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/dialog/dialogs.dart';
import 'package:tutorials/component/event/event_bus.dart';
import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/locale/Translations.dart';
import 'package:tutorials/model/AboutAuthorTab.dart';
import 'package:tutorials/model/AppFeedback.dart';
import 'package:tutorials/utils/date_time_utils.dart';
import 'package:tutorials/view/settings/feedback_update_event.dart';
import 'package:flutter/material.dart';

import 'feedback_tab_view.dart';

class Feedbacks extends StatefulWidget {
  @override
  _Feedbacks createState() => _Feedbacks();
}

class _Feedbacks extends State<Feedbacks> with SingleTickerProviderStateMixin {
  late TabController _tabController; //需要定义一个Controller
  List<TabTitle> tabs = [];

  Future<void> _insertFeedback() async {
    String msgContent =
        await Dialogs.showInputBottomSheet(context, "请输入留言内容", "") ?? "";
    if (msgContent.isEmpty) {
      return;
    }
    AppFeedback feedback = AppFeedback();
    feedback.fromUserId = UserCaches.getUserId();
    feedback.msgContent = msgContent;
    feedback.sendTime = DateTimeUtils.format(DateTime.now());
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => jsonEncode(feedback.toJson()));
    HttpRequests.post(
            HttpConstant.url_settings_app_feedback_insert, param, null)
        .then((result) {
      Logs.info('_insertFeedback responseBody=' + (result?.responseBody ?? ""));
      setState(() {
        FeedbackUpdateEvent event = FeedbackUpdateEvent();
        event.data = "hhh";
        EventBus.publish(event);
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
        child: Column(children: <Widget>[
          Expanded(
            child: Container(
              child: TabBarView(
                controller: _tabController,
                children: tabs.map((e) {
                  return Container(
                    child: FeedbackTabView(
                      tab: e,
                      feedbacks: [],
                    ),
                  );
                }).toList(),
              ),
            ),
          ),
        ]),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _insertFeedback,
        tooltip: '+',
        child: new Icon(Icons.add),
      ),
    );
  }
}
