import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/origin/school_card_query_result.dart';
import 'package:tutorials/request/school_card_request.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:tutorials/utils/date_time_utils.dart';
import 'package:tutorials/request/origin/school_meta_data_query_result.dart'
    as SchoolResult;
import 'package:tutorials/request/origin/subject_meta_data_query_result.dart'
    as SubjectResult;

class SchoolCardEdit extends StatefulWidget {
  const SchoolCardEdit({Key? key}) : super(key: key);

  @override
  _SchoolCardEditState createState() => _SchoolCardEditState();
}

class _SchoolCardEditState extends State<SchoolCardEdit> {
  String url = "assets/images/user_null.png";
  int select_mode_start = 1;
  int select_mode_end = 2;
  Data? arg = null;
  TextEditingController subjectController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context)?.settings?.arguments;
    if (args is Data) {
      arg = args;
      Logs.info('SchoolCardEdit Data=${arg?.toJson()}');
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "school.card.edit.title")),
      ),
      body: SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            const SizedBox(height: 16),
            Container(
              alignment: Alignment.center,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  const SizedBox(width: 28),
                  GestureDetector(
                    onTap: () async {
                      selectDate(select_mode_start);
                    },
                    child: Text(
                      "${DateTimeUtils.subYear(arg?.timeStart)}",
                      style: const TextStyle(color: Colors.blue, fontSize: 18),
                    ),
                  ),
                  const SizedBox(width: 4),
                  const Text("-"),
                  const SizedBox(width: 4),
                  GestureDetector(
                    onTap: () async {
                      selectDate(select_mode_end);
                    },
                    child: Text(
                      "${DateTimeUtils.subYear(arg?.timeEnd)}",
                      style: const TextStyle(color: Colors.blue, fontSize: 18),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            ListTile(
              leading: ClipOval(
                  child: CachedNetworkImage(
                    imageUrl: arg?.imageUrl ?? url,
                    placeholder: (context, url) => const SizedBox(
                      child: Center(
                          child: CircularProgressIndicator(
                        strokeWidth: 2,
                      )),
                    ),
                    errorWidget: (context, url, error) => Image.asset(
                      url,
                      width: 86,
                      height: 86,
                    ),
                    height: 86,
                    width: 86,
                  ),
              ),
              title: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  GestureDetector(
                    onTap: () async {
                      Logs.info("onTap111");
                      SchoolResult.Data? selectSchool =
                          await AppUtils.toPageWithResult(
                              context,
                              RouteNameConstant
                                  .route_name_school_card_school_select,
                              args: arg);
                      Logs.info("selectSchool ${selectSchool}");
                      if (selectSchool != null) {
                        setState(() {
                          arg?.id = selectSchool.id;
                          arg?.name = selectSchool.name;
                          arg?.imageUrl = selectSchool.imageUrl;
                        });
                      }
                    },
                    child: Text(
                      arg?.name ?? '',
                      style: const TextStyle(color: Colors.blue, fontSize: 20),
                    ),
                  ),
                  GestureDetector(
                    onTap: () async {
                      Logs.info("onTap222 ： ${arg?.id}");
                      SubjectResult.Data? selectSubject =
                          await AppUtils.toPageWithResult(
                              context,
                              RouteNameConstant
                                  .route_name_school_card_subject_select,
                              args: arg);
                      Logs.info("selectSubject ${selectSubject?.name}");
                      if (selectSubject != null) {
                        setState(() {
                          arg?.subject = selectSubject.name;
                        });
                      }
                    },
                    child: Text(
                      arg?.subject ?? "计算机科学预计技术",
                      style: const TextStyle(color: Colors.blue, fontSize: 14),
                    ),
                  ),
                ],
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                TextButton(
                  child: Text(Translations.textOf(context, "all.btn.save")),
                  onPressed: () {
                    SchoolCardRequests.save(arg ?? Data()).then((result) {
                      Logs.info('result.common.message = ${result.msg}');
                      Toasts.show(result.msg ?? '成功');
                    });
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

  Future<void> selectDate(select_mode) async {
    DateTime? picked = await showDatePicker(
      context: context,
      initialEntryMode: DatePickerEntryMode.input,
      initialDate: DateTime(2010),
      firstDate: DateTime(2010),
      lastDate: DateTime(2050),
    );
    if (picked != null) {
      Logs.info("picked = $picked");
      setState(() {
        if (select_mode == select_mode_start) {
          arg?.timeStart = DateTimeUtils.format(picked);
          Logs.info(arg?.timeStart);
        } else {
          arg?.timeEnd = DateTimeUtils.format(picked);
          Logs.info(arg?.timeEnd);
        }
      });
    }
  }
}
