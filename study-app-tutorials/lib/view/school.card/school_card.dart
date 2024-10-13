import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/image.crop/image_cropper.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/file_upload_request.dart';
import 'package:tutorials/request/model/school/school_card_query_request_param.dart';
import 'package:tutorials/request/model/upload/file_upload_param.dart';
import 'package:tutorials/request/origin/school_card_query_result.dart';
import 'package:tutorials/request/school_card_request.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:tutorials/utils/date_time_utils.dart';
import 'package:tutorials/view/school.card/school_card_basic.dart';

class SchoolCard extends StatefulWidget {
  const SchoolCard({Key? key}) : super(key: key);

  @override
  _SchoolCardState createState() => _SchoolCardState();
}

class _SchoolCardState extends State<SchoolCard> {
  List<Data> list = [];
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
        title: Text(Translations.textOf(context, "school.card.title")),
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
                        Data item = list[index];
                        String title = formatTime(item);
                        return GestureDetector(
                            onTap: () {},
                            child: SchoolCardBasic(
                              title: title,
                              subTitle: item?.name??'',
                              desc: item?.subject??'',
                              url: item?.imageUrl??'',
                              onTap: () {
                                AppUtils.toPage(context,
                                    RouteNameConstant.route_name_school_card_edit,
                                    args: item);
                              },
                            ));
                      },
                      separatorBuilder: (context, index) =>
                          const Divider(height: .0),
                    )),
        ),
      )),
      floatingActionButton: FloatingActionButton(
        onPressed: _insertSchoolRecord,
        tooltip: '+',
        child: const Icon(Icons.add),
      ),
    );
  }

  Future<Null> _onRefresh() async {
    showLoading();
    SchoolCardQueryRequestParam requestParam = new SchoolCardQueryRequestParam();
    SchoolCardRequests.query(requestParam).then((result) {
      Logs.info('_onRefresh responseBody=' + (result?.toString() ?? ""));
      hideLoading();
      setState(() {
        if (result.code == 200) {
          list.clear();
          var dataList = result.data ?? [];
          for (var element in dataList) {
            list.add(element);
          }
        }
      });
    }).catchError((error) {
      Logs.info(error.toString());
      hideLoading();
    });
  }


  Future<void> _insertSchoolRecord() async {
    AppUtils.toPage(context,RouteNameConstant.route_name_school_card_add);
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


  hideLoading() {
    setState(() {
      _isLoading = false;
    });
  }

  showLoading() {
    setState(() {
      _isLoading = true;
    });
  }

  String formatTime(Data item) {
    var start = DateTimeUtils.subYear(item.timeStart);
    var end = DateTimeUtils.subYear(item.timeEnd);
    return "$start - $end";
  }
}
