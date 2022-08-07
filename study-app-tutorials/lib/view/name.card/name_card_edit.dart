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
import 'package:tutorials/view/name.card/name_card_basic.dart';
import 'package:tutorials/view/name.card/name_card_school.dart';
import 'package:tutorials/widgets/profile_icon_basic.dart';

class NameCardEdit extends StatefulWidget {
  const NameCardEdit({Key? key}) : super(key: key);

  @override
  _NameCardEditState createState() => _NameCardEditState();
}

class _NameCardEditState extends State<NameCardEdit> {
  String url = "assets/images/user_null.png";

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context)?.settings?.arguments;
    print('NameCardEdit args=${args}');

    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "name.card.title")),
      ),
      body: SafeArea(
        child: ListView(
          children: [
            NameCardBasic(
              title: '学校信息',
              subTitle: '河海大学',
              desc: '2013年-2017年 计算机科学与技术专业！',
              url: 'https://avatars.githubusercontent.com/u/18094768?v=4',
              onTap: () {},
            ),
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
                    child: ClipOval(
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
      FileUploadRequests.upload(param, (count, total) {}).then((value) => {});
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
