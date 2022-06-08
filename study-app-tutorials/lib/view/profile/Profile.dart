import 'package:flutter/material.dart';
import 'package:tutorials/component/log/Logs.dart';
// import 'package:google_fonts/google_fonts.dart';


class Profile extends StatefulWidget {
  const Profile({Key? key}) : super(key: key);

  @override
  _ProfileState createState() => _ProfileState();
}

class _ProfileState extends State<Profile> {
  List<String> images = [

  ];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: ListView(
          // mainAxisAlignment: MainAxisAlignment.start,
          // crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Padding(
              padding: EdgeInsets.only(
                  left: 53, right: 56, top: 35, bottom: 57),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    "John Doe",
                    style: TextStyle(
                        fontSize: 20,
                        color: Colors.black,
                        fontStyle: FontStyle.normal,
                        fontWeight: FontWeight.w600,
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      Logs.info("GetIt.I.get<NavigationService>().back();");
                    },
                    child: Container(
                      width: 96,
                      height: 96,
                      margin: EdgeInsets.only(top: 35, bottom: 19),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(96),
                        color:  Color(0XFFC4C4C4),
                      ),
                    ),
                  ),
                  Text(
                    "@johnamazingdoe",
                    style: TextStyle(
                        fontSize: 14,
                        color: Colors.black,
                        fontStyle: FontStyle.normal,
                        fontWeight: FontWeight.w400,
                    ),
                  ),
                  SizedBox(height: 54),
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
                              "Following",
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
                              "Followers",
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
                              "views",
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
                  SizedBox(height: 36),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      TextButton(
                        child: Padding(
                          padding: EdgeInsets.symmetric(
                              horizontal: 8, vertical: 6),
                          child: Text(
                            'Follow',
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
                      TextButton(
                        child: Padding(
                          padding: EdgeInsets.symmetric(
                              horizontal: 8, vertical: 6),
                          child: Text(
                            'Message',
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
                      )
                    ],
                  ),
                ],
              ),
            ),
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
                                "Photos",
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
                  Expanded(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            const Icon(
                              Icons.video_camera_back_rounded,
                              color: Colors.grey,
                            ),
                            Padding(
                              padding: const EdgeInsets.only(left: 2),
                              child: Text(
                                "Videos",
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
                        SizedBox(height: 2, width: 76
                          // color: Colors.black,
                        )
                      ],
                    ),
                  ),
                  Expanded(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: [
                            const Icon(
                              Icons.emoji_emotions,
                              color: Colors.grey,
                            ),
                            Padding(
                              padding: const EdgeInsets.only(left: 2),
                              child: Text(
                                "Tagged",
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
                        SizedBox(
                          height: 2,
                          width: 76,
                          // color: Colors.black,
                        )
                      ],
                    ),
                  )
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
