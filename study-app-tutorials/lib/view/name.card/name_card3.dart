import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/image.crop/image_cropper.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/picker/image_picker.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/file_upload_request.dart';
import 'package:tutorials/request/model/upload/file_upload_param.dart';
import 'package:tutorials/widgets/profile_icon_basic.dart';

class NameCard3 extends StatefulWidget {
  const NameCard3({Key? key}) : super(key: key);

  @override
  _NameCardState createState() => _NameCardState();
}


class _NameCardState extends State<NameCard3> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: Column(
          children: [
            SizedBox(
              height: 249,
              child: Stack(
                children: [
                  Container(
                    color: Color(0xffC4C4C4),
                    height: 186,
                  ),
                  Positioned(
                    right: 121,
                    bottom: 0,
                    child: Container(
                      width: 150,
                      height: 150,
                      // margin: EdgeInsets.only(
                      //   top: 99,
                      // ),
                      decoration: BoxDecoration(
                        color: Color(0xff525252),
                        borderRadius: BorderRadius.circular(150),
                      ),
                    ),
                  )
                ],
              ),
            ),
            SizedBox(
              height: 24,
            ),
            Text(
              "John Doe",
              style: TextStyle(
                  fontSize: 24,
                  color: Colors.black,
                  fontStyle: FontStyle.normal,
                  fontWeight: FontWeight.w600,
              ),
            ),
            SizedBox(
              height: 12,
            ),
            Text(
              "Designer",
              style: TextStyle(
                  fontSize: 12,
                  color: Colors.black,
                  fontStyle: FontStyle.normal,
                  fontWeight: FontWeight.w400,
              ),
            ),
            SizedBox(
              height: 25,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                GestureDetector(
                  onTap: () {
                    Logs.info('GetIt.I.get<NavigationService>().back()');
                  },
                  child: Container(
                    width: 90,
                    height: 30,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(30),
                      border: Border.all(color: Colors.black, width: 1),
                    ),
                    child: Center(
                      child: Text(
                        "ADD FRIEND",
                        style: TextStyle(
                            fontSize: 12,
                            color: Colors.black,
                            fontStyle: FontStyle.normal,
                            fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                  ),
                ),
                SizedBox(width: 8),
                GestureDetector(
                  onTap: () {
                    Logs.info('GetIt.I.get<NavigationService>().back();');
                  },
                  child: Container(
                    width: 90,
                    height: 30,
                    decoration: BoxDecoration(
                      color: Color(0xff262626),
                      borderRadius: BorderRadius.circular(30),
                    ),
                    child: Center(
                      child: Text(
                        "FOLLOW",
                        style:
                        TextStyle(
                            fontSize: 12,
                            color: Colors.white,
                            fontStyle: FontStyle.normal,
                            fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
            SizedBox(height: 74),
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 24),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "ABOUT",
                    style: TextStyle(
                        fontSize: 14,
                        color: Color(0xffA8A8A8),
                        fontStyle: FontStyle.normal,
                        fontWeight: FontWeight.w400,
                    ),
                  ),
                  SizedBox(
                    height: 14,
                  ),
                  Text(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Viverra rutrum elementum nunc velit dui dui, penatibus.",
                    style: TextStyle(
                        fontSize: 12,
                        color: Color(0xffA8A8A8),
                        fontStyle: FontStyle.normal,
                        fontWeight: FontWeight.w400,
                    ),
                  )
                ],
              ),
            ),
            SizedBox(
              height: 30,
            ),
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 24),
              child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  "PHOTOS",
                  style: TextStyle(
                      fontSize: 14,
                      color: Color(0xffA8A8A8),
                      fontStyle: FontStyle.normal,
                      fontWeight: FontWeight.w400,
                    ),
                ),
              ),
            ),
            GridView.builder(
              shrinkWrap: true,
              itemCount: 30,
              physics: NeverScrollableScrollPhysics(),
              padding: EdgeInsets.symmetric(horizontal: 24, vertical: 12),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                mainAxisSpacing: 8,
                crossAxisSpacing: 6,
              ),
              itemBuilder: (c, i) {
                return Container(
                  height: 110,
                  color: Color(0xffC4C4C4),
                );
              },
            )
          ],
        ),
      ),
    );
  }
}

