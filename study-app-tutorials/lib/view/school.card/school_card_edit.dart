
import 'package:cached_network_image/cached_network_image.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/image.crop/image_cropper.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/picker/image_picker.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/file_upload_request.dart';
import 'package:tutorials/request/model/upload/file_upload_param.dart';
import 'package:tutorials/request/origin/school_card_query_result.dart';
import 'package:tutorials/utils/date_time_utils.dart';

class SchoolCardEdit extends StatefulWidget {
  const SchoolCardEdit({Key? key}) : super(key: key);

  @override
  _SchoolCardEditState createState() => _SchoolCardEditState();
}

class _SchoolCardEditState extends State<SchoolCardEdit> {
  String url = "assets/images/user_null.png";
  Data? item;

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    item = ModalRoute.of(context)?.settings?.arguments as Data?;
    print('SchoolCardEdit args=${item}');

    var start = DateTimeUtils.subYear(item?.timeStart);
    var end = DateTimeUtils.subYear(item?.timeEnd);
    String title =  "$start年 - $end年";

    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "school.card.edit.title")),
      ),
      body: SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              title: Text(title),
              onTap: () async {
                final picked = await showDateRangePicker(
                  context: context,
                  initialDateRange: DateTimeRange(start: DateTime(2014),end: DateTime(2024)),
                  lastDate:  DateTime(2024),
                  firstDate: DateTime(2014),
                  initialEntryMode: DatePickerEntryMode.inputOnly,
                );
                if (picked != null && picked != null) {
                  print(picked);
                  setState(() {

                  });
                }
              },
            ),
            ListTile(
              leading: GestureDetector(
                onTap: () {
                  ImagePicker.pickImage().then((value) => {pickBack(value)});
                },
                child: ClipOval(
                  child: CachedNetworkImage(
                    imageUrl: url,
                    placeholder: (context, url) =>
                        const CircularProgressIndicator(),
                    errorWidget: (context, url, error) => Image.asset(
                      url,
                      width: 86,
                      height: 86,
                    ),
                    height: 86,
                    width: 86,
                  ),
                ),
              ),
              title: Text(item?.name??''),
              subtitle: Text(item?.subject??''),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                TextButton(
                  child: const Text('保存'),
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
    );
  }

  pickBack(String? value) async {
    Logs.info('image picked:  $value');
    if (value != null) {
      String valueCrop = await _cropImage(value);
      setState(() {
        Logs.info('image picked:  $value');
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
