import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/dialog/dialogs.dart';
import 'package:tutorials/component/event/event_bus.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/model/about_author_tab.dart';
import 'package:tutorials/request/feedback_request.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';
import 'package:tutorials/request/model/feedback/message_feedback_insert_request_param.dart';
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
    String msgContent = await Dialogs.showInputBottomSheet(context,
            Translations.textOf(context, 'settings.feedback.input.hint'), "") ??
        "";
    if (msgContent.isEmpty) {
      return;
    }

    MessageFeedback feedback = MessageFeedback();
    feedback.fromUserId = UserCaches.getUserId();
    feedback.fromUserIconUrl = UserCaches.getUser().iconUrl;
    feedback.msgContent = msgContent;
    feedback.sendTime = DateTimeUtils.format(DateTime.now());

    MessageFeedbackInsertRequestParam param =
        MessageFeedbackInsertRequestParam();
    param.feedback = feedback;
    MessageFeedbackRequests.insert(param).then((result) {
      Logs.info('_insertFeedback responseBody=' + (result?.toString() ?? ""));
      if (result.common.code == ErrorCodeConstant.success) {
        setState(() {
          FeedbackUpdateEvent event = FeedbackUpdateEvent();
          event.data = result.common.message;
          EventBus.publish(event);
        });
      } else {
        Toasts.show(result.common.message);
      }
    }).catchError((error) {
      Logs.info(error.toString());
    });
  }

  @override
  Widget build(BuildContext context) {
    String all = Translations.textOf(context, 'settings.feedback.all');
    String my = Translations.textOf(context, 'settings.feedback.my');

    tabs = FeedbackTabView.init(all, my);
    _tabController = TabController(length: tabs.length, vsync: this);


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
      floatingActionButton: FloatingActionButton(
        onPressed: _insertFeedback,
        tooltip: '+',
        child: const Icon(Icons.add),
      ),
    );
  }
}
