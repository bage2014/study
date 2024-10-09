import 'dart:math';

import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/utils/app_utils.dart';


class SchoolCardSchoolSelect extends StatefulWidget {
  const SchoolCardSchoolSelect({Key? key}) : super(key: key);


  @override
  State<SchoolCardSchoolSelect> createState() => _SchoolCardSchoolSelectState();
}

class _SchoolCardSchoolSelectState extends State<SchoolCardSchoolSelect> {

  TextEditingController _searchQueryController = TextEditingController();
  bool _isSearching = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: const BackButton(),
        title: _isSearching ? _buildSearchField() : Text(Translations.textOf(context, "school.card.edit.title")),
        actions: _buildActions(),
      ),
      body: LayoutBuilder(builder: (context, constraints) {
        return SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Column(
                children: List.generate(20, (index) {
                  return ListItem();
                }),
              )
            ],
          ),
        );
      }),
    );

  }


  Widget _buildSearchField() {
    return
    TextField(
      controller: _searchQueryController,
      autofocus: true,
      decoration: InputDecoration(
        hintText: "Search...",
        border: InputBorder.none,
        hintStyle: TextStyle(color: Colors.white30),
      ),
      style: TextStyle(color: Colors.white, fontSize: 16.0),
      onChanged: (value) {
        // place submit function here
        Logs.info("onFieldSubmitted=$value");
      },
    );
  }

  List<Widget> _buildActions() {
    if (_isSearching) {
      return <Widget>[
        IconButton(
          icon: const Icon(Icons.clear),
          onPressed: () {
            if (_searchQueryController == null ||
                _searchQueryController.text.isEmpty) {
              Navigator.pop(context);
              return;
            }
            setState(() {
              _searchQueryController.clear();
              _isSearching = false;
            });
          },
        ),
      ];
    }

    return <Widget>[
      IconButton(
        icon: const Icon(Icons.search),
        onPressed: () {
          setState(() {
            _isSearching = true;
          });
        },
      ),
    ];
  }
}

class ListItem extends StatelessWidget {
  const ListItem({Key? key,}) : super(key: key);


  @override
  Widget build(BuildContext context) {
    var random = Random().nextInt(100).toString();
    return GestureDetector(
      onTap: () {
        AppUtils.pop(context,random);
        Logs.info("random=$random");
      },
      child: ListTile(
        leading: const CircleAvatar(
          backgroundImage: AssetImage('assets/images/school-logo.png'),
        ),
        title: Text("河海大学${random}" ),
      ),
    );


  }
}