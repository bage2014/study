
import 'package:cached_network_image/cached_network_image.dart';
import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/dialog/dialogs.dart';
import 'package:tutorials/component/event/event_bus.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/constant/locale_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/model/AboutAuthorTab.dart';
import 'package:tutorials/request/feedback_request.dart';
import 'package:tutorials/request/model/feedback/message_feedback_delete_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';
import 'package:tutorials/view/settings/feedback_update_event.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class FeedbackTabView extends StatelessWidget {
  FeedbackTabView({Key? key, required this.tab, required this.feedbacks})
      : super(key: key);

  TabTitle tab;
  List<MessageFeedback> feedbacks;
  static const String key_all = 'all';
  static const String key_my = 'my';

  static List<TabTitle> init(String all,String me) {
    List<TabTitle> tabs = [];
    tabs.add(TabTitle(key_all, all));
    tabs.add(TabTitle(key_my, me));
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
        return const Text('');
    }
  }
}

class _FeedbackTabView extends StatefulWidget {
  late bool _isMe;

  _FeedbackTabView(bool isMe) {
    _isMe = isMe;
  }

  @override
  _FeedbackTabState createState() => _FeedbackTabState(_isMe);
}

class _FeedbackTabState extends State<_FeedbackTabView> {
  List<MessageFeedback> list = [];
  bool _isLoading = false;
  bool _isMe = false;

  _FeedbackTabState(bool isMe) {
    _isMe = isMe;
  }

  @override
  void initState() {
    super.initState();
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
                valueColor: const AlwaysStoppedAnimation(Colors.blue),
              ))
            : (list.isEmpty
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
                      MessageFeedback appFeedback = list[index];
                      return GestureDetector(
                          onTap: () {},
                          child: Card(
                            clipBehavior: Clip.antiAlias,
                            elevation: 2,
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(8.0)),
                            margin: const EdgeInsets.symmetric(
                                horizontal: 16, vertical: 6),
                            child: Container(
                              child: Row(
                                children: <Widget>[
                                  Container(
                                    padding: const EdgeInsets.only(
                                        left: 0, right: 0),
                                    width: 64.0,
                                    child: ClipOval(
                                      child: CachedNetworkImage(
                                        imageUrl:
                                            appFeedback.fromUserIconUrl ?? '',
                                        placeholder: (context, url) =>
                                            const CircularProgressIndicator(),
                                        errorWidget: (context, url, error) =>
                                            const Image(
                                                image: AssetImage(
                                                    "assets/images/user_null.png")),
                                        height: 64,
                                        width: 64,
                                      ),
                                    ),
                                  ),
                                  Expanded(
                                    flex: 1,
                                    child: Container(
                                        padding: const EdgeInsets.symmetric(
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
                                                          appFeedback
                                                                  .fromUserName ??
                                                              '佚名',
                                                          overflow: TextOverflow
                                                              .ellipsis,
                                                          style:
                                                              const TextStyle(
                                                                  color: Colors
                                                                      .black,
                                                                  fontSize:
                                                                      15)),
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
                                                              GestureDetector(
                                                                  onTap: () {
                                                                    _delete(
                                                                        appFeedback);
                                                                  },
                                                                  child:
                                                                      const Icon(
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
                                            const SizedBox(
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
                                                        appFeedback
                                                                .msgContent ??
                                                            '',
                                                        maxLines: 3,
                                                        style: const TextStyle(
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
                                                      margin:
                                                          const EdgeInsets.only(
                                                              top: 8.0,
                                                              right: 8.0),
                                                      child: Text(
                                                        '${appFeedback.sendTime}',
                                                        style: const TextStyle(
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
      return;
    }

    MessageFeedbackQueryRequestParam param = MessageFeedbackQueryRequestParam();
    if (_isMe) {
      param.fromUserId = UserCaches.getUserId();
    }
    MessageFeedbackRequests.query(param).then((result) {
      hideLoading();
      Logs.info('_onRefresh responseBody=' + (result.toString() ?? ""));
      if (result.common.code == ErrorCodeConstant.success) {
        Logs.info('_onRefresh length = ${result.feedbacks.length}');
        setState(() {
          list = result.feedbacks ?? [];
        });
      } else {
        Toasts.show(result.common.message);
      }
    }).catchError((error) {
      Logs.info(error.toString());
      hideLoading();
    });
  }

  Future<Null> _delete(MessageFeedback appFeedback) async {
    // 确认框
    String? showConfirmDialog = await Dialogs.showConfirmDialog(context,
        Translations.textOf(context, LocaleConstant.all_delete_confirm), null);
    if ("true" == showConfirmDialog) {
      showLoading();

      MessageFeedbackDeleteRequestParam param =
          MessageFeedbackDeleteRequestParam();
      param.feedbackId = appFeedback.id;
      Logs.info('_delete feedbackId=' + (param.feedbackId.toString() ?? ""));

      MessageFeedbackRequests.delete(param).then((result) {
        hideLoading();
        Logs.info('_delete responseBody=' + (result.toString() ?? ""));
        if (result.common.code == ErrorCodeConstant.success) {
          _onRefresh();
        } else {
          Toasts.show(result.common.message);
        }
      }).catchError((error) {
        Logs.info(error.toString());
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
