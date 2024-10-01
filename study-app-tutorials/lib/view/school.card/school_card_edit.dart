
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
  SchoolCardData item = SchoolCardData();
  Data? arg = null;
  TextEditingController subjectController = TextEditingController();


  @override
  Widget build(BuildContext context) {
    //获取路由参数
    arg = ModalRoute.of(context)?.settings?.arguments as Data?;
    print('SchoolCardEdit args=${arg?.toJson()}');

    var start = DateTimeUtils.subYear(arg?.timeStart);
    var end = DateTimeUtils.subYear(arg?.timeEnd);
    item?.timeBetween =  "$start年 - $end年";
    print('SchoolCardEdit timeBetween=${item?.timeBetween}');

    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "school.card.edit.title")),
      ),
      body: SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            // TextField(
            //   controller: subjectController,
            //   decoration: InputDecoration(
            //     hintText:
            //     Translations.textOf(context, 'login.account.hint'),
            //     hintStyle: const TextStyle(
            //       fontSize: 14,
            //       fontWeight: FontWeight.w400,
            //       color: Color(0xFFA8A8A8),
            //     ),
            //     prefixIcon:
            //     const Icon(Icons.person, color: Color(0xFFA8A8A8)),
            //     contentPadding: const EdgeInsets.symmetric(
            //         horizontal: 17, vertical: 22),
            //     border: const OutlineInputBorder(
            //         borderSide: BorderSide(color: Color(0xFFD0D0D0))),
            //     focusedBorder: const OutlineInputBorder(
            //         borderSide: BorderSide(color: Color(0xFFD0D0D0))),
            //     enabledBorder: const OutlineInputBorder(
            //         borderSide: BorderSide(color: Color(0xFFD0D0D0))),
            //   ),
            // ),
            // const SizedBox(height: 14),

            ListTile(
              title: Text(item?.timeBetween??'未知'),
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

                  var start = picked.start.year;
                  var end = picked.end.year;
                  arg?.timeStart = DateTimeUtils.format(picked.start);
                  arg?.timeEnd = DateTimeUtils.format(picked.end);
                  var newTitle =  "$start年 - $end年";
                  print(newTitle);
                  print(arg?.toJson().toString());
                  setState(() {
                    item?.timeBetween =  newTitle;
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
                    imageUrl: arg?.imageUrl??url,
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
              title: Text(arg?.name??''),
              subtitle: Text(arg?.subject??''),
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

class SchoolCardData {
  String? timeBetween;
}
