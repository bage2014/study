import 'dart:io';
import 'dart:math';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/model/common/page_query_request_param.dart';
import 'package:tutorials/request/origin/school_meta_data_query_result.dart';
import 'package:tutorials/request/school_subject_select_request.dart';
import 'package:tutorials/utils/app_utils.dart';

class SchoolCardSchoolSelect extends StatefulWidget {
  const SchoolCardSchoolSelect({Key? key}) : super(key: key);

  @override
  State<SchoolCardSchoolSelect> createState() => _SchoolCardSchoolSelectState();
}

class _SchoolCardSchoolSelectState extends State<SchoolCardSchoolSelect> {
  TextEditingController _searchQueryController = TextEditingController();
  bool _isSearching = false;
  List<Data> list = [];
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _onRefresh();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: const BackButton(),
        title: _isSearching
            ? _buildSearchField()
            : Text(Translations.textOf(
                context, "school.card.select.school.title")),
        actions: _buildActions(),
      ),
      body: Container(
        child: _isLoading
            ? Center(
                child: CircularProgressIndicator(
                backgroundColor: Colors.grey[200],
                valueColor: const AlwaysStoppedAnimation(Colors.blue),
              ))
            : LayoutBuilder(builder: (context, constraints) {
                return SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Column(
                        children: List.generate(list.length, (index) {
                          return ListItem(elementAt: list.elementAt(index));
                        }),
                      )
                    ],
                  ),
                );
              }),
      ),
    );
  }

  Widget _buildSearchField() {
    return TextField(
      controller: _searchQueryController,
      autofocus: true,
      decoration: InputDecoration(
        hintText: Translations.textOf(context, "all.search.hint"),
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

  void _onRefresh() {
    showLoading();
    CommonPageQueryRequestParam param = CommonPageQueryRequestParam();
    SchoolSubjectSelectRequests.querySchool(param).then((result) {
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
}

class ListItem extends StatelessWidget {
  const ListItem({Key? key, this.elementAt}) : super(key: key);
  final Data? elementAt;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        AppUtils.pop(context, elementAt?.name??'');
      },
      child: ListTile(
        leading: CachedNetworkImage(
          imageUrl: elementAt?.imageUrl ?? '',
          placeholder: (context, url) => const SizedBox(
            child: Center(
                child: CircularProgressIndicator(
              strokeWidth: 2,
            )),
          ),
          errorWidget: (context, url, error) => Image.file(
            File(url),
            width: 86,
            height: 86,
          ),
          height: 86,
          width: 86,
        ),
        title: Text(elementAt?.name ?? ''),
      ),
    );
  }
}
