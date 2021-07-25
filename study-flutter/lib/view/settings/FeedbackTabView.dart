import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/AboutAuthorTab.dart';
import 'package:app_lu_lu/model/AppFeedback.dart';
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

AppFeedback _data(int id) => AppFeedback('hello ${id}', 'world ${id}');

class _FeedbackTabView extends StatefulWidget {
  @override
  _FeedbackTabState createState() => new _FeedbackTabState();
}

class _FeedbackTabState extends State<_FeedbackTabView> {
  List<AppFeedback> list = [];

  @override
  void initState() {
    list.add(_data(list.length + 1));
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: _onRefresh,
      child: list.length == 0
          ? Center(
              child: Text(
                Translations.textOf(context, "all.list.view.no.data"),
                textAlign: TextAlign.center,
              ),
            )
          : ListView.separated(
              itemCount: list.length,
              itemBuilder: (context, index) {
                return new GestureDetector(
                  onTap: () {},
                  child: Card(
                    margin: EdgeInsets.all(8.0),
                    child: Text(
                      'hello',
                      style: DefaultTextStyle.of(context).style.copyWith(
                            fontSize: 14.0,
                          ),
                    ),
                  ),
                );
              },
              separatorBuilder: (context, index) => Divider(height: .0),
            ),
    );
  }

  Future<Null> _onRefresh() async {
    setState(() {
      list.add(_data(list.length + 1));
    });
  }
}
