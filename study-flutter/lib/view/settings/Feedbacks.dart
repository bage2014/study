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

  @override
  void initState() {
    tabs = FeedbackTabView.init();
    _tabController = TabController(length: tabs.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Expanded(
            child: Container(
              child: TabBarView(
                controller: _tabController,
                children: tabs.map((e) {
                  //创建3个Tab页
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
    );
  }
}
