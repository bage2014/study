import 'package:flutter/material.dart';

class Dialogs {
  static Future<void> showProgress(
      BuildContext context, String title, onWillPop) {
    return showDialog<String>(
        context: context,
        barrierDismissible: false, //点击遮罩不关闭对话框
        builder: (context) {
          return new WillPopScope(
              onWillPop: onWillPop, //重点此举
              child: AlertDialog(
                content: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: <Widget>[
                    CircularProgressIndicator(),
                    Padding(
                      padding: const EdgeInsets.only(top: 26.0),
                      child: Text(title),
                    )
                  ],
                ),
              ));
        });
//    return showDialog<String>(
//        context: context,
//        barrierDismissible: false,
//        builder: (context) {
//          return Container(
//              alignment: Alignment.center,
//              child: CircularProgressIndicator(
//                strokeWidth: 2,
//                valueColor: AlwaysStoppedAnimation(Colors.blue),
//              )
//          );
//        });
  }

  static Future<void> dismiss(BuildContext context) {
    Navigator.of(context).pop();
  }

  static Future<String> showInfoDialog(BuildContext context, String title) {
    TextEditingController _controller = TextEditingController();
    return showDialog<String>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(title),
          actions: <Widget>[
            FlatButton(
              child: Text("确定"),
              onPressed: () {
                Navigator.of(context).pop(_controller.text.toString());
              },
            ),
          ],
        );
      },
    );
  }

  static Future<String> showConfirmDialog(
      BuildContext context, String title, String content) {
    TextEditingController _controller = TextEditingController();
    List<Widget> actions = <Widget>[
      FlatButton(
        child: Text("取消"),
        onPressed: () => Navigator.of(context).pop(), // 关闭对话框
      ),
      FlatButton(
        child: Text("确定"),
        onPressed: () {
          Navigator.of(context).pop("true");
        },
      ),
    ];
    if (content == null) {
      return showDialog<String>(
        context: context,
        builder: (context) {
          return AlertDialog(title: Text(title), actions: actions);
        },
      );
    }
    return showDialog<String>(
      context: context,
      builder: (context) {
        return AlertDialog(
            title: Text(title), content: Text(content), actions: actions);
      },
    );
  }

  static Future<String> showInputDialog(
      BuildContext context, String title, String defaultValue) {
    TextEditingController _controller = TextEditingController.fromValue(
        TextEditingValue(
            text: defaultValue,
            selection: TextSelection.fromPosition(TextPosition(
                affinity: TextAffinity.downstream,
                offset: defaultValue.length))));
    return showDialog<String>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(title),
          content: TextField(
            controller: _controller,
            decoration: InputDecoration(
              contentPadding: EdgeInsets.all(10.0), //内容内边距
            ),
            autofocus: true, //自动获取焦点，设置false
          ),
          actions: <Widget>[
            FlatButton(
              child: Text("取消"),
              onPressed: () => Navigator.of(context).pop(), // 关闭对话框
            ),
            FlatButton(
              child: Text("确定"),
              onPressed: () {
                Navigator.of(context).pop(_controller.text.toString());
              },
            ),
          ],
        );
      },
    );
  }

  static Future<int> showListBottomSheet(
      BuildContext context, List<String> list) {
    return showModalBottomSheet<int>(
      context: context,
      builder: (BuildContext context) {
        return ListView.builder(
          itemCount: list.length,
          itemBuilder: (BuildContext context, int index) {
            return ListTile(
              title: Text(list[index]),
              onTap: () => Navigator.of(context).pop(index),
            );
          },
        );
      },
    );
  }
}
