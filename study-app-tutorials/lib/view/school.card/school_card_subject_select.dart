
import 'package:flutter/material.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/model/common/default_page_query_request_param.dart';
import 'package:tutorials/request/model/common/page_query_request_param.dart';
import 'package:tutorials/request/model/school/subject_page_query_request_param.dart';
import 'package:tutorials/request/origin/subject_meta_data_query_result.dart';
import 'package:tutorials/request/school_subject_select_request.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:tutorials/request/origin/school_meta_data_query_result.dart' as SchoolResult;

class SchoolCardSubjectSelect extends StatefulWidget {
  const SchoolCardSubjectSelect({Key? key}) : super(key: key);

  @override
  State<SchoolCardSubjectSelect> createState() =>
      _SchoolCardSubjectSelectState();
}

class _SchoolCardSubjectSelectState extends State<SchoolCardSubjectSelect> {
  TextEditingController _searchQueryController = TextEditingController();
  bool _isSearching = false;
  List<Data> list = [];
  bool _isLoading = false;
  SchoolResult.Data? arg;

  @override
  void initState() {
    super.initState();
    _onRefresh(null);
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context)?.settings?.arguments;
    Logs.info('SchoolCardSubjectSelect Data1=${arg?.toJson()}');
    if(args is SchoolResult.Data) {
      arg = args;
      Logs.info('SchoolCardSubjectSelect Data=${arg?.toJson()}');
    }

    return Scaffold(
      appBar: AppBar(
        leading: const BackButton(),
        title: _isSearching
            ? _buildSearchField()
            : Text(Translations.textOf(
                context, "school.card.select.subject.title")),
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
        _onRefresh(value);
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
              _onRefresh('');
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

  void _onRefresh(String? keyword) {
    showLoading();
    SubjectPageQueryRequestParam param = SubjectPageQueryRequestParam();
    param.keyword = keyword;
    param.schoolId = arg?.id;
    SchoolSubjectSelectRequests.querySubject(param).then((result) {
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
        AppUtils.pop(context, elementAt);
      },
      child: Column(
        children: [
          ListTile(
            title: Text(elementAt?.name ?? ''),
          )
        ],
      ),
    );
  }
}
