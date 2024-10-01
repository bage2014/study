import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';

/// An example of the elevated card type.
///
/// The default settings for [Card] will provide an elevated
/// card matching the spec:
///
/// https://m3.material.io/components/cards/specs#a012d40d-7a5c-4b07-8740-491dec79d58b
class SchoolCardBasic extends StatefulWidget {
  const SchoolCardBasic(
      {Key? key,
      this.title = "",
      this.subTitle = "",
      this.desc = "",
      this.url = "assets/images/user_null.png",
      this.onTap})
      : super(key: key);
  final String title;
  final String subTitle;
  final String desc;
  final String url;
  final GestureTapCallback? onTap;

  @override
  _SchoolCardBasicState createState() => _SchoolCardBasicState();
}

class _SchoolCardBasicState extends State<SchoolCardBasic> {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
          child: Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                ListTile(
                  title: Text(widget.title),
                ),
                ListTile(
                  leading: CachedNetworkImage(
                    imageUrl: widget.url,
                    placeholder: (context, url) => const SizedBox(
                      child: Center(
                          child: CircularProgressIndicator(strokeWidth: 2,)
                      ),
                    ),
                    errorWidget: (context, url, error) => Image.file(
                      File(url),
                      width: 86,
                      height: 86,
                    ),
                    height: 86,
                    width: 86,
                  ),
                  title: Text(widget.subTitle),
                  subtitle:
                      Text(widget.desc),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: <Widget>[
                    TextButton(
                      child: const Text('编辑'),
                      onPressed: widget.onTap,
                    ),
                    const SizedBox(width: 8),
                    TextButton(
                      child: const Text('分享'),
                      onPressed: () {
                        /* ... */
                      },
                    ),
                    const SizedBox(width: 8),
                  ],
                ),
              ],
            ),
          ),
      ),
    );
  }
}
