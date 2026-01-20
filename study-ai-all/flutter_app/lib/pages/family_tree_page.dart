import 'package:flutter/material.dart';
import 'package:flutter_app/pages/persons_page.dart';
import 'package:flutter_app/pages/relationships_page.dart';
import 'package:flutter_app/pages/graph_page.dart';
import 'package:flutter_app/pages/settings_page.dart';

class FamilyTreePage extends StatefulWidget {
  const FamilyTreePage({Key? key}) : super(key: key);

  @override
  State<FamilyTreePage> createState() => _FamilyTreePageState();
}

class _FamilyTreePageState extends State<FamilyTreePage> {
  int _selectedIndex = 0;

  static const List<Widget> _widgetOptions = <Widget>[
    GraphPage(),
    PersonsPage(),
    RelationshipsPage(),
  ];

  static const List<String> _tabTitles = <String>['关系图', '人员管理', '关系管理'];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('家庭族谱'),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const SettingsPage()),
              );
            },
            tooltip: '设置',
          ),
        ],
      ),
      body: Center(child: _widgetOptions.elementAt(_selectedIndex)),
      bottomNavigationBar: BottomNavigationBar(
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.people_outline),
            label: '关系图',
          ),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: '人员管理'),
          BottomNavigationBarItem(
            icon: Icon(Icons.family_restroom),
            label: '关系管理',
          ),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.blue,
        onTap: _onItemTapped,
      ),
    );
  }
}
