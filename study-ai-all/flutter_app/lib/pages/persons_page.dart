import 'package:flutter/material.dart';
import 'package:flutter_app/api/api_service.dart';
import 'package:flutter_app/models/person.dart';

class PersonsPage extends StatefulWidget {
  const PersonsPage({Key? key}) : super(key: key);

  @override
  State<PersonsPage> createState() => _PersonsPageState();
}

class _PersonsPageState extends State<PersonsPage> {
  late Future<List<Person>> _futurePersons;
  final _formKey = GlobalKey<FormState>();
  
  // 表单控制器
  final _nameController = TextEditingController();
  final _genderController = TextEditingController();
  final _birthDateController = TextEditingController();
  final _deathDateController = TextEditingController();
  final _descriptionController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _refreshData();
  }

  void _refreshData() {
    _futurePersons = ApiService.getAllPersons();
  }

  @override
  void dispose() {
    // 清理控制器
    _nameController.dispose();
    _genderController.dispose();
    _birthDateController.dispose();
    _deathDateController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  // 添加人员
  void _addPerson() async {
    if (_formKey.currentState!.validate()) {
      try {
        Person newPerson = Person(
          id: 0, // 后端会自动生成ID
          name: _nameController.text,
          gender: _genderController.text,
          birthDate: _birthDateController.text,
          deathDate: _deathDateController.text.isNotEmpty ? _deathDateController.text : null,
          description: _descriptionController.text,
        );

        await ApiService.addPerson(newPerson);
        
        // 刷新数据
        setState(() {
          _refreshData();
        });
        
        // 清空表单
        _formKey.currentState!.reset();
        
        // 显示成功消息
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('添加人员成功')),
        );
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('添加人员失败: $e')),
        );
      }
    }
  }

  // 删除人员
  void _deletePerson(int id) async {
    try {
      await ApiService.deletePerson(id);
      
      setState(() {
        _refreshData();
      });
      
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('删除人员成功')),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('删除人员失败: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              '人员管理',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            
            // 添加人员表单
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        '添加新人员',
                        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 16),
                      
                      TextFormField(
                        controller: _nameController,
                        decoration: const InputDecoration(
                          labelText: '姓名',
                          border: OutlineInputBorder(),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return '请输入姓名';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 12),
                      
                      TextFormField(
                        controller: _genderController,
                        decoration: const InputDecoration(
                          labelText: '性别 (男/女)',
                          border: OutlineInputBorder(),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return '请输入性别';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 12),
                      
                      TextFormField(
                        controller: _birthDateController,
                        decoration: const InputDecoration(
                          labelText: '出生日期 (YYYY-MM-DD)',
                          border: OutlineInputBorder(),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return '请输入出生日期';
                          }
                          // 简单的日期格式验证
                          if (!RegExp(r'\d{4}-\d{2}-\d{2}').hasMatch(value)) {
                            return '请输入有效的日期格式 (YYYY-MM-DD)';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 12),
                      
                      TextFormField(
                        controller: _deathDateController,
                        decoration: const InputDecoration(
                          labelText: '去世日期 (可选, YYYY-MM-DD)',
                          border: OutlineInputBorder(),
                        ),
                        validator: (value) {
                          if (value != null && value.isNotEmpty) {
                            // 简单的日期格式验证
                            if (!RegExp(r'\d{4}-\d{2}-\d{2}').hasMatch(value)) {
                              return '请输入有效的日期格式 (YYYY-MM-DD)';
                            }
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 12),
                      
                      TextFormField(
                        controller: _descriptionController,
                        decoration: const InputDecoration(
                          labelText: '描述',
                          border: OutlineInputBorder(),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return '请输入描述';
                          }
                          return null;
                        },
                        maxLines: 3,
                      ),
                      const SizedBox(height: 16),
                      
                      ElevatedButton(
                        onPressed: _addPerson,
                        child: const Text('添加人员'),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            
            const SizedBox(height: 24),
            const Text(
              '人员列表',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            
            // 人员列表
            Expanded(
              child: FutureBuilder<List<Person>>(
                future: _futurePersons,
                builder: (context, snapshot) {
                  if (snapshot.hasData) {
                    List<Person> persons = snapshot.data!;
                    
                    return ListView.builder(
                      itemCount: persons.length,
                      itemBuilder: (context, index) {
                        Person person = persons[index];
                        return Card(
                          child: Padding(
                            padding: const EdgeInsets.all(12.0),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        '${person.name} (${person.id})',
                                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                                      ),
                                      const SizedBox(height: 4),
                                      Text('性别: ${person.gender}'),
                                      Text('出生日期: ${person.birthDate}'),
                                      if (person.deathDate != null) Text('去世日期: ${person.deathDate}'),
                                    ],
                                  ),
                                ),
                                IconButton(
                                  icon: const Icon(Icons.delete, color: Colors.red),
                                  onPressed: () {
                                    _deletePerson(person.id);
                                  },
                                ),
                              ],
                            ),
                          ),
                        );
                      },
                    );
                  } else if (snapshot.hasError) {
                    return Center(
                      child: Text('加载失败: ${snapshot.error}'),
                    );
                  } else {
                    return const Center(
                      child: CircularProgressIndicator(),
                    );
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}