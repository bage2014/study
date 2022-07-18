import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/utils/app_utils.dart';

class AboutUser extends StatefulWidget {
  const AboutUser({Key? key}) : super(key: key);

  @override
  _ProfileState createState() => _ProfileState();
}

class _ProfileState extends State<AboutUser> {
  List<String> images = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: ListView(
          children: [
            Padding(
              padding:
                  EdgeInsets.only(left: 24, right: 24, top: 24, bottom: 24),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  GestureDetector(
                    onTap: () {
                      AppUtils.back(context);
                    },
                    child: const Icon(Icons.arrow_back_outlined),
                  ),
                ],
              ),
            ),
            SizedBox(height: 8),
            Padding(
              padding: EdgeInsets.only(left: 24, right: 24, top: 16, bottom: 8),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  GestureDetector(
                    onTap: () {
                      Logs.info("GetIt.I.get<NavigationService>().back();");
                    },
                    child: ClipOval(
                      child: Image(
                          image: AssetImage("assets/images/user_null.png")),
                    ),
                  ),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "刘亦菲",
                        style: TextStyle(
                          fontSize: 20,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      Text(
                        "中国，上海",
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                    ],
                  ),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      TextButton(
                        child: Padding(
                          padding:
                              EdgeInsets.symmetric(horizontal: 24, vertical: 6),
                          child: Text(
                            '已关注',
                            style: TextStyle(
                              fontSize: 14,
                              color: Colors.black,
                              fontStyle: FontStyle.normal,
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                        ),
                        style: TextButton.styleFrom(
                          primary: Colors.white,
                          backgroundColor: const Color(0xffD0D0D0),
                        ),
                        onPressed: () {},
                      ),
                    ],
                  ),
                ],
              ),
            ),
            SizedBox(height: 16),
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(
                        "9",
                        style: TextStyle(
                          fontSize: 20,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      Text(
                        "关注",
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(
                        "129K",
                        style: TextStyle(
                          fontSize: 20,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      Text(
                        "粉丝",
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(
                        "7.2M",
                        style: TextStyle(
                          fontSize: 20,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      Text(
                        "留言",
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.black,
                          fontStyle: FontStyle.normal,
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                    ],
                  ),
                )
              ],
            ),
            SizedBox(height: 32),
            Container(
              padding: EdgeInsets.only(left: 24, right: 24),
              child: Text(
                "关于",
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
            Container(
              padding: EdgeInsets.only(left: 24, right: 25, bottom: 18),
              child: Row(
                children: [
                  Expanded(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.start,
                          children: [
                            const Icon(Icons.photo),
                            Padding(
                              padding: const EdgeInsets.only(left: 2),
                              child: Text(
                                "最新动态",
                                style: TextStyle(
                                  fontSize: 14,
                                  color: Colors.black,
                                  fontStyle: FontStyle.normal,
                                  fontWeight: FontWeight.w400,
                                ),
                              ),
                            ),
                          ],
                        ),
                        SizedBox(
                          height: 14,
                        ),
                        Container(
                          height: 2,
                          width: 76,
                          color: Colors.black,
                        )
                      ],
                    ),
                  ),
                ],
              ),
            ),
            Padding(
              padding: EdgeInsets.only(left: 13, right: 11),
              child: GridView.count(
                shrinkWrap: true,
                crossAxisCount: 3,
                crossAxisSpacing: 0.0,
                mainAxisSpacing: 0.0,
                children: List.generate(
                  images.length,
                  (index) {
                    return Container(
                      height: 122,
                      decoration: BoxDecoration(
                        image: DecorationImage(
                          image: AssetImage(images[index]),
                          fit: BoxFit.cover,
                        ),
                      ),
                    );
                  },
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
