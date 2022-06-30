import 'package:tutorials/locale/translations.dart';
import 'package:flutter/material.dart';

class Dialogs {
  static Future<void> showProgress(
      BuildContext context, String title, double? value, onWillPop) {
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
                    CircularProgressIndicator(value: value,),
                    Padding(
                      padding: const EdgeInsets.only(top: 26.0),
                      child: Text(title),
                    )
                  ],
                ),
              ));
        });
  }

  static Future<void> dismiss(BuildContext context) async {
    Navigator.of(context).pop();
  }

  static Future<String?> showInfoDialog(
      BuildContext context, String? title, String content) {
    TextEditingController _controller = TextEditingController();
    return showDialog<String>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(
              title ?? Translations.textOf(context, "all.dialog.info.title")),
          content: Text(content),
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

  static Future<String?> showConfirmDialog(
      BuildContext context, String title, String? content) {
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

  // ref: https://stackoverflow.com/questions/53311553/how-to-set-showmodalbottomsheet-to-full-height
  static Future<int?> showButtonSelectDialog(
      BuildContext context, List<String> contents, List<Icon>? icons) {
    return showModalBottomSheet<int>(
      isScrollControlled: true,
      context: context,
      isDismissible: true,
      builder: (context) => Wrap(children: <Widget>[
        ListView.builder(
            itemCount: contents.length,
            shrinkWrap: true,
            itemBuilder: (BuildContext context, int index) {
              return icons == null
                  ? ListTile(
                      title: Text(contents[index]),
                      onTap: () => Navigator.of(context).pop(index),
                    )
                  : ListTile(
                      title: Text(contents[index]),
                      trailing: icons[index],
                      onTap: () => Navigator.of(context).pop(index),
                    );
            })
      ]),
    );
  }

  static Future<String?> showInputDialog(
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

  static Future<int?> showListBottomSheet(
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

  static Future<String?> showInputBottomSheet(
      BuildContext context, String helperText, String defaultValue) {
    TextEditingController _controller = TextEditingController.fromValue(
        TextEditingValue(
            text: defaultValue,
            selection: TextSelection.fromPosition(TextPosition(
                affinity: TextAffinity.downstream,
                offset: defaultValue.length))));
    return showModalBottomSheet(
        context: context,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.horizontal(
          left: Radius.circular(8),
          right: Radius.circular(8),
        )),
        builder: (BuildContext context) {
          return Container(
            child: Row(
              children: <Widget>[
                Expanded(
                    child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    TextField(
                      controller: _controller,
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.all(10.0),
                        helperText: helperText,
                      ),
                      autofocus: true, //自动获取焦点，设置false
                    ),
                  ],
                )),
                Column(
                    crossAxisAlignment: CrossAxisAlignment.end,
                    children: <Widget>[
                      IconButton(
                          icon: Icon(
                            Icons.send,
                            color: Colors.blue,
                            size: 20.0,
                          ),
                          onPressed: () {
                            Navigator.of(context)
                                .pop(_controller.text.toString());
                          }),
                    ])
              ],
            ),
          );
        });
  }
}
