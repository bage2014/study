import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/model/about_author_tab.dart';
import 'package:tutorials/request/auther_query_request.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/request/model/common/default_query_request_param.dart';
import 'package:tutorials/request/origin/author_query_result.dart';

import 'about_author_tab_view.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor>
    with SingleTickerProviderStateMixin {
  late TabController _tabController; //需要定义一个Controller
  List<TabTitle> tabs = [];
  Data authorInfo = Data();
  bool _isLoading = true;

  @override
  void initState() {
    tabs = AboutAuthorTabView.init();
    _tabController = TabController(length: tabs.length, vsync: this);
    _refresh();
  }

  @override
  Widget build(BuildContext context) {
    const double edgeLeft = 16.0;
    const double edgeRight = 16.0;
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Stack(
        alignment: Alignment.center, //指定未定位或部分定位widget的对齐方式
        children: <Widget>[
          Container(
              child: !_isLoading
                  ? Container(
                      alignment: Alignment.center,
                      child: Column(children: <Widget>[
                        Container(
                          padding: const EdgeInsets.fromLTRB(
                              edgeLeft, 16.0, edgeRight, 0.0),
                          child: ClipOval(
                            child: CachedNetworkImage(
                              imageUrl: authorInfo.iconUrl ?? '',
                              placeholder: (context, url) =>
                                  CircularProgressIndicator(),
                              errorWidget: (context, url, error) => Image(
                                  image: AssetImage(
                                      "assets/images/user_null.png")),
                              height: 128,
                              width: 128,
                            ),
                          ),
                        ),
                        Container(
                          padding: const EdgeInsets.fromLTRB(
                              edgeLeft, 0.0, edgeRight, 0.0),
                          child: Text(
                              '${authorInfo.firstName ?? ""}${authorInfo.lastName ?? ""}',
                              textScaleFactor: 1.4),
                        ),
                        Container(
                          padding: const EdgeInsets.fromLTRB(
                              edgeLeft, 0.0, edgeRight, 0.0),
                          child: TabBar(
                              //生成Tab菜单
                              controller: _tabController,
                              indicatorColor: Color(0xff66c97f),
                              //选中时下划线颜色,如果使用了indicator这里设置无效
                              labelColor: Color(0xff66c97f),
                              unselectedLabelColor: Colors.black,
                              tabs: tabs
                                  .map((e) => Tab(
                                        text: e.text,
                                      ))
                                  .toList()),
                        ),
                        Expanded(
                          child: Container(
                            padding: const EdgeInsets.fromLTRB(
                                edgeLeft, 0.0, edgeRight, 0.0),
                            child: TabBarView(
                              controller: _tabController,
                              children: tabs.map((e) {
                                //创建3个Tab页
                                return Container(
                                  child: AboutAuthorTabView(
                                      tab: e, authorInfo: authorInfo),
                                );
                              }).toList(),
                            ),
                          ),
                        ),
                      ]),
                    )
                  : null),
          Container(
            child: _isLoading
                ? CircularProgressIndicator(
                    backgroundColor: Colors.grey[200],
                    valueColor: const AlwaysStoppedAnimation(Colors.blue),
                  )
                : null,
          ),
        ],
      ),
    );
  }

  void _refresh() {
    showLoading();

    AuthorQueryRequests.query(DefaultQueryRequestParam()).then((result) {
      Logs.info('login result=' + (result.toString() ?? ""));
      hideLoading();
      if (result.code == ErrorCodeConstant.success) {
        setState(() {
          authorInfo = result.data ?? Data();
        });
      } else {
        Toasts.show(result.msg ?? '');
      }
    }).catchError((error) {
      Toasts.show(error.toString());
      Logs.error(error.toString());
      hideLoading();
    });
  }

  showLoading() {
    setState(() {
      _isLoading = true;
    });
  }

  hideLoading() {
    setState(() {
      _isLoading = false;
    });
  }
}
