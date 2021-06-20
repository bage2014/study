import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/AboutAuthorTab.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

import 'AboutAuthorTabView.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor>
    with SingleTickerProviderStateMixin {
  late TabController _tabController; //需要定义一个Controller
  List<AboutAuthorTab> tabs = [];

  @override
  void initState() {
    tabs = AboutAuthorTabView.init();
    _tabController = TabController(length: tabs.length, vsync: this);
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
                imageUrl:
                    "https://avatars.githubusercontent.com/u/18094768?v=4",
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
            child: Text('陆瑞华', textScaleFactor: 1.4),
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
                  return Container(
                    child: AboutAuthorTabView(tab: e),
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
//    print(json.encode(paramJson));
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
//      print(error.toString());
//    });

}
