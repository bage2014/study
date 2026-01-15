import 'package:flutter/material.dart';
import 'package:flutter_app/api/api_service.dart';
import 'package:flutter_app/models/person.dart';
import 'package:flutter_app/models/relationship.dart';

class RelationshipsPage extends StatefulWidget {
  const RelationshipsPage({Key? key}) : super(key: key);

  @override
  State<RelationshipsPage> createState() => _RelationshipsPageState();
}

class _RelationshipsPageState extends State<RelationshipsPage> {
  late Future<List<Relationship>> _futureRelationships;
  late Future<List<Person>> _futurePersons;
  final _formKey = GlobalKey<FormState>();
  
  // 表单控制器
  final _person1IdController = TextEditingController();
  final _person2IdController = TextEditingController();
  final _typeController = TextEditingController();
  final _descriptionController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _refreshData();
  }

  void _refreshData() {
    _futureRelationships = ApiService.getAllRelationships();
    _futurePersons = ApiService.getAllPersons();
  }

  @override
  void dispose() {
    // 清理控制器
    _person1IdController.dispose();
    _person2IdController.dispose();
    _typeController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  // 添加关系
  void _addRelationship() async {
    if (_formKey.currentState!.validate()) {
      try {
        Relationship newRelationship = Relationship(
          id: 0, // 后端会自动生成ID
          person1Id: int.parse(_person1IdController.text),
          person2Id: int.parse(_person2IdController.text),
          type: _typeController.text,
          description: _descriptionController.text,
        );

        await ApiService.addRelationship(newRelationship);
        
        // 刷新数据
        setState(() {
          _refreshData();
        });
        
        // 清空表单
        _formKey.currentState!.reset();
        
        // 显示成功消息
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('添加关系成功')),
        );
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('添加关系失败: $e')),
        );
      }
    }
  }

  // 删除关系
  void _deleteRelationship(int id) async {
    try {
      await ApiService.deleteRelationship(id);
      
      setState(() {
        _refreshData();
      });
      
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('删除关系成功')),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('删除关系失败: $e')),
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
              '关系管理',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            
            // 添加关系表单
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        '添加新关系',
                        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 16),
                      
                      FutureBuilder<List<Person>>(
                        future: _futurePersons,
                        builder: (context, snapshot) {
                          if (snapshot.hasData) {
                            List<Person> persons = snapshot.data!;
                            return Column(
                              children: [
                                DropdownButtonFormField<int>(
                                  value: _person1IdController.text.isNotEmpty ? int.parse(_person1IdController.text) : null,
                                  decoration: const InputDecoration(
                                    labelText: '人员1',
                                    border: OutlineInputBorder(),
                                  ),
                                  items: persons.map((person) {
                                    return DropdownMenuItem<int>(
                                      value: person.id,
                                      child: Text('${person.name} (${person.id})'),
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    _person1IdController.text = value.toString();
                                  },
                                  validator: (value) {
                                    if (value == null) {
                                      return '请选择人员1';
                                    }
                                    return null;
                                  },
                                ),
                                const SizedBox(height: 12),
                                
                                DropdownButtonFormField<int>(
                                  value: _person2IdController.text.isNotEmpty ? int.parse(_person2IdController.text) : null,
                                  decoration: const InputDecoration(
                                    labelText: '人员2',
                                    border: OutlineInputBorder(),
                                  ),
                                  items: persons.map((person) {
                                    return DropdownMenuItem<int>(
                                      value: person.id,
                                      child: Text('${person.name} (${person.id})'),
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    _person2IdController.text = value.toString();
                                  },
                                  validator: (value) {
                                    if (value == null) {
                                      return '请选择人员2';
                                    }
                                    return null;
                                  },
                                ),
                              ],
                            );
                          } else if (snapshot.hasError) {
                            return Text('加载人员列表失败: ${snapshot.error}');
                          } else {
                            return const Center(child: CircularProgressIndicator());
                          }
                        },
                      ),
                      const SizedBox(height: 12),
                      
                      TextFormField(
                        controller: _typeController,
                        decoration: const InputDecoration(
                          labelText: '关系类型 (如：父子、夫妻、兄弟等)',
                          border: OutlineInputBorder(),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return '请输入关系类型';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: 12),
                      
                      TextFormField(
                        controller: _descriptionController,
                        decoration: const InputDecoration(
                          labelText: '关系描述',
                          border: OutlineInputBorder(),
                        ),
                        maxLines: 3,
                      ),
                      const SizedBox(height: 16),
                      
                      ElevatedButton(
                        onPressed: _addRelationship,
                        child: const Text('添加关系'),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            
            const SizedBox(height: 24),
            const Text(
              '关系列表',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            
            // 关系列表
            Expanded(
              child: FutureBuilder<List<Relationship>>(
                future: _futureRelationships,
                builder: (context, snapshot) {
                  if (snapshot.hasData) {
                    List<Relationship> relationships = snapshot.data!;
                    
                    return ListView.builder(
                      itemCount: relationships.length,
                      itemBuilder: (context, index) {
                        Relationship relationship = relationships[index];
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
                                        '${relationship.person1Id} <-> ${relationship.type} <-> ${relationship.person2Id}',
                                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                                      ),
                                      const SizedBox(height: 4),
                                      if (relationship.description.isNotEmpty) Text('描述: ${relationship.description}'),
                                    ],
                                  ),
                                ),
                                IconButton(
                                  icon: const Icon(Icons.delete, color: Colors.red),
                                  onPressed: () {
                                    _deleteRelationship(relationship.id);
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