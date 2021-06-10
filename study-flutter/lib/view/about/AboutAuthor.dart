import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/locale/Translations.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor> {
  List<String> list = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            padding: const EdgeInsets.fromLTRB(24.0, 8.0, 0.0, 0.0),
            child: Row(
              children: <Widget>[
                Expanded(
                  child:
                  CachedNetworkImage(
                    imageUrl: "https://avatars.githubusercontent.com/u/18094768?v=4",
                    placeholder: (context, url) => CircularProgressIndicator(),
                    errorWidget: (context, url, error) => Icon(Icons.error),
                    width: 64, //图片的宽
                    height: 64,
                  ),
                ),
                Container(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center, //垂直方向居中对齐
                    children: <Widget>[
                      Text('陆瑞华', textScaleFactor: 1.2),
                      Text("893542907@qq.com")
                    ],
                  ),
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(16.0, 16.0, 16.0, 16.0),
            child: Text('上海某互联网，Java 研发工程师，5年研发服务端经验'),
//            child: Text(Translations.textOf(context, "about.author.name")),
          ),
          Container(
            alignment: Alignment.centerLeft,
            child: RefreshIndicator(
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
                            onTap: () {
                              Logs.info('onTap index = $index');
                            },
                            child: ListTile(
                                title: Text(list[index]),
                                trailing: new GestureDetector(
                                    child: Icon(
                                  Icons.favorite,
                                  color: Colors.red,
                                ))));
                      },
                      separatorBuilder: (context, index) => Divider(height: .0),
                    ),
            ),
//            child: Text(Translations.textOf(context, "about.author.name")),
          ),
        ]),
      ),
    );
  }

  Future<Null> _onRefresh() async {
//    Map<String, dynamic> paramJson = new HashMap();
//    paramJson.putIfAbsent("targetPage", () => 1);
//    paramJson.putIfAbsent("pageSize", () => 100);
//    Map<String, String> param = new HashMap();
//    param.putIfAbsent("param", () => json.encode(paramJson));
//    print(json.encode(paramJson));
    list.add("hello");
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
}
