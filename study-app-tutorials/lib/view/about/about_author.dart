import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/model/about_author_tab.dart';
import 'package:tutorials/request/model/AuthorInfo.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

import 'about_author_tab_view.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor>
    with SingleTickerProviderStateMixin {
  late TabController _tabController; //需要定义一个Controller
  List<TabTitle> tabs = [];
  late AuthorInfo authorInfo;
  bool isLoading = true;

  @override
  void initState() {
    tabs = AboutAuthorTabView.init();
    _tabController = TabController(length: tabs.length, vsync: this);
    authorInfo = new AuthorInfo();
    authorInfo.iconUrl = "https://avatars.githubusercontent.com/u/18094768?v=4";
    authorInfo.homePageUrl = "https://github.com/bage2014";
    authorInfo.firstName = "陆";
    authorInfo.lastName = "瑞华";
    authorInfo.email = "893542907@qq.com";
    authorInfo.description = "上海某互联网，Java 研发工程师，5年研发服务端经验";
    isLoading = false;
  }

  @override
  Widget build(BuildContext context) {
    const double edgeLeft = 16.0;
    const double edgeRight = 16.0;
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            padding: const EdgeInsets.fromLTRB(edgeLeft, 16.0, edgeRight, 0.0),
            child: ClipOval(
              child: CachedNetworkImage(
                imageUrl: authorInfo.iconUrl ?? '',
                placeholder: (context, url) => CircularProgressIndicator(),
                errorWidget: (context, url, error) =>
                    Image(image: AssetImage("assets/images/user_null.png")),
                height: 128,
                width: 128,
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(edgeLeft, 0.0, edgeRight, 0.0),
            child: Text('${authorInfo.firstName ?? ""}${authorInfo.lastName ?? ""}', textScaleFactor: 1.4),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(edgeLeft, 0.0, edgeRight, 0.0),
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
              padding: const EdgeInsets.fromLTRB(edgeLeft, 0.0, edgeRight, 0.0),
              child: TabBarView(
                controller: _tabController,
                children: tabs.map((e) {
                  //创建3个Tab页
                  return isLoading ? LinearProgressIndicator(
                    backgroundColor: Colors.grey[200],
                    valueColor: AlwaysStoppedAnimation(Colors.blue),
                  ) : Container(
                    child: AboutAuthorTabView(tab: e,authorInfo: authorInfo),
                  );
                }).toList(),
              ),
            ),
          ),
        ]),
      ),
    );
  }

//    Map<String, dynamic> paramJson = new HashMap();
//    paramJson.putIfAbsent("targetPage", () => 1);
//    paramJson.putIfAbsent("pageSize", () => 100);
//    Map<String, String> param = new HashMap();
//    param.putIfAbsent("param", () => json.encode(paramJson));
//    Logs.info(json.encode(paramJson));
//     list.add("hello");
//    HttpRequests.get(HttpConstant.url_tv_query_page, param, null)
//        .then((result) {
//      Logs.info('_onRefresh responseBody=' + result?.responseBody);
//      setState(() {
//        QueryTvResult tvResult =
//        QueryTvResult.fromJson(json.decode(result?.responseBody));
//        if (tvResult.code == 200) {
//          list = tvResult.data;
//        }
//      });
//    }).catchError((error) {
//      Logs.info(error.toString());
//    });

}
