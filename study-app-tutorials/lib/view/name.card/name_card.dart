import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/image.crop/image_cropper.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/picker/image_picker.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/file_upload_request.dart';
import 'package:tutorials/request/model/upload/file_upload_param.dart';
import 'package:tutorials/widgets/profile_icon_basic.dart';

class NameCard extends StatefulWidget {
  const NameCard({Key? key}) : super(key: key);

  @override
  _NameCardState createState() => _NameCardState();
}

class _NameCardState extends State<NameCard> {
  List<String> images = [];

  String url = "assets/images/user_null.png";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "name.card.title")),
      ),
      body: SafeArea(
        child: ListView(
          children: [
            const SizedBox(height: 8),
            url.startsWith('assets')
                ? ProfileIconBasic(
              url: url,
              onTap: () {
                ImagePicker.pickImage()
                    .then((value) => {pickBack(value)});
              },
            )
                : GestureDetector(
              onTap: () {
                ImagePicker.pickImage()
                    .then((value) => {pickBack(value)});
              },
              child:
              ClipOval(
                child: CachedNetworkImage(
                  imageUrl: url,
                  placeholder: (context, url) =>
                  const CircularProgressIndicator(),
                  errorWidget: (context, url, error) => Image.file(
                    File(url),
                    width: 150,
                    height: 150,
                  ),
                  height: 86,
                  width: 86,
                ),
              ),
              // Image.file(
              //   File(url),
              //   width: 150,
              //   height: 150,
              // ),
            ),
            const SizedBox(height: 16),
            Container(
              padding: EdgeInsets.only(left: 24, right: 24),
              child: Text(
                "简介",
                style: TextStyle(
                  fontSize: 16,
                  color: Color(0xffA8A8A8),
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            SizedBox(
              height: 4,
            ),
            Container(
              padding: EdgeInsets.only(left: 24, right: 25, bottom: 18),
              child: Text(
                "看了这组照片,网友们也明白了,为什么很多大牌商品喜欢请神仙姐姐去做代言了,因为她只要站在那里,隔着屏幕都能感受到满满的高级感。 别看这次出游,刘亦菲表现得十分欢脱,吃了很多美食...",
                style: TextStyle(
                  fontSize: 14,
                  height: 1.57,
                  color: Color(0XFF000000),
                  fontWeight: FontWeight.w400,
                ),
              ),
            ),
            SizedBox(height: 16),

          ],
        ),
      ),
    );
  }

  pickBack(String? value) async {
    Logs.info('image picked:  $value');
    if (value != null) {
      String valueCrop = await _cropImage(value);
      setState(() {
        url = valueCrop;
      });

      FileUploadParam param = FileUploadParam();
      param.files = [MultipartFile.fromFileSync(url)];
      FileUploadRequests.upload(param,(count, total) { }).then((value) => {

      });

    } else {
      setState(() {
        url = url == "assets/images/logo128.png"
            ? "assets/images/user_null.png"
            : "assets/images/logo128.png";
      });
    }
  }

  Future<String> _cropImage(String valueCrop) async {
    return await ImageCropper.cropImage(context, valueCrop);
  }
}
