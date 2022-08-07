import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/image.crop/image_cropper.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/picker/image_picker.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/file_upload_request.dart';
import 'package:tutorials/request/model/upload/file_upload_param.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:tutorials/view/name.card/name_card_basic.dart';
import 'package:tutorials/view/name.card/name_card_school.dart';
import 'package:tutorials/widgets/profile_icon_basic.dart';

class NameCard extends StatefulWidget {
  const NameCard({Key? key}) : super(key: key);

  @override
  _NameCardState createState() => _NameCardState();
}

class _NameCardState extends State<NameCard> {
  List<String> list = [];
  bool _isLoading = false;
  String url = "";

  @override
  void initState() {
    super.initState();
    _onRefresh();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "name.card.title")),
      ),
      body: SafeArea(
          child: RefreshIndicator(
        onRefresh: _onRefresh,
        child: Container(
          child: _isLoading
              ? Center(
                  child: CircularProgressIndicator(
                  backgroundColor: Colors.grey[200],
                  valueColor: const AlwaysStoppedAnimation(Colors.blue),
                ))
              : (list.isEmpty
                  ? Center(
                      child: Text(
                        Translations.textOf(context, "all.list.view.no.data"),
                        textAlign: TextAlign.center,
                      ),
                    )
                  : ListView.separated(
                      itemCount: list.length,
                      shrinkWrap: true,
                      itemBuilder: (context, index) {
                        return GestureDetector(
                            onTap: () {},
                            child: NameCardBasic(
                              title: '学校信息',
                              subTitle: '河海大学',
                              desc: '2013年-2017年 计算机科学与技术专业！',
                              url:
                                  'https://avatars.githubusercontent.com/u/18094768?v=4',
                              onTap: () {
                                AppUtils.toPage(context,
                                    RouteNameConstant.route_name_name_card_edit,
                                    args: list[index]);
                              },
                            ));
                      },
                      separatorBuilder: (context, index) => Divider(height: .0),
                    )),
        ),
      )),
    );
  }

  Future<Null> _onRefresh() async {
    setState(() {
      list.add('value1');
      list.add('value2');
      list.add('value3');
      list.add('value4');
      list.add('value5');
      list.add('value6');
      list.add('value7');
    });
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
