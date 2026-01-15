import 'package:flutter/material.dart';
import 'package:flutter_app/api/api_service.dart';
import 'package:flutter_app/models/person.dart';
import 'package:flutter_app/models/relationship.dart';

class GraphPage extends StatefulWidget {
  const GraphPage({Key? key}) : super(key: key);

  @override
  State<GraphPage> createState() => _GraphPageState();
}

class _GraphPageState extends State<GraphPage> {
  late Future<List<Person>> _futurePersons;
  late Future<List<Relationship>> _futureRelationships;

  @override
  void initState() {
    super.initState();
    _refreshData();
  }

  void _refreshData() {
    _futurePersons = ApiService.getAllPersons();
    _futureRelationships = ApiService.getAllRelationships();
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
              '家庭关系图',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: FutureBuilder(
                future: Future.wait([_futurePersons, _futureRelationships]),
                builder: (context, AsyncSnapshot<List<Object>> snapshot) {
                  if (snapshot.hasData) {
                    List<Person> persons = snapshot.data![0] as List<Person>;
                    List<Relationship> relationships = snapshot.data![1] as List<Relationship>;

                    return SingleChildScrollView(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Card(
                            child: Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const Text(
                                    '家庭成员信息',
                                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                                  ),
                                  const SizedBox(height: 12),
                                  Text('共有 ${persons.length} 位家庭成员'),
                                  Text('共有 ${relationships.length} 条关系记录'),
                                ],
                              ),
                            ),
                          ),
                          const SizedBox(height: 16),
                          const Text(
                            '人员列表',
                            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                          ),
                          const SizedBox(height: 12),
                          ...persons.map((person) => _buildPersonCard(person)).toList(),
                          const SizedBox(height: 16),
                          const Text(
                            '关系列表',
                            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                          ),
                          const SizedBox(height: 12),
                          ...relationships.map((relationship) => _buildRelationshipCard(relationship, persons)).toList(),
                        ],
                      ),
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

  Widget _buildPersonCard(Person person) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12.0),
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
            if (person.description.isNotEmpty) Text('描述: ${person.description}'),
          ],
        ),
      ),
    );
  }

  Widget _buildRelationshipCard(Relationship relationship, List<Person> persons) {
    String person1Name = persons.firstWhere((p) => p.id == relationship.person1Id, orElse: () => Person(id: 0, name: '未知', gender: '', birthDate: '', description: '')).name;
    String person2Name = persons.firstWhere((p) => p.id == relationship.person2Id, orElse: () => Person(id: 0, name: '未知', gender: '', birthDate: '', description: '')).name;

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              '${person1Name} <-> ${relationship.type} <-> ${person2Name}',
              style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 4),
            if (relationship.description.isNotEmpty) Text('描述: ${relationship.description}'),
          ],
        ),
      ),
    );
  }
}