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
                    List<Relationship> relationships =
                        snapshot.data![1] as List<Relationship>;

                    return SingleChildScrollView(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          // 家庭成员信息卡片
                          Card(
                            elevation: 4,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            child: Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const Text(
                                    '家庭成员信息',
                                    style: TextStyle(
                                      fontSize: 18,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                  const SizedBox(height: 12),
                                  Text('共有 ${persons.length} 位家庭成员'),
                                  Text('共有 ${relationships.length} 条关系记录'),
                                ],
                              ),
                            ),
                          ),
                          const SizedBox(height: 24),

                          // 关系图可视化
                          const Center(
                            child: Text(
                              '家庭关系',
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                          const SizedBox(height: 16),
                          Container(
                            padding: const EdgeInsets.all(24),
                            decoration: BoxDecoration(
                              color: const Color(0xFFF0F4F8),
                              borderRadius: BorderRadius.circular(12),
                            ),
                            child: Center(
                              child: Wrap(
                                spacing: 24,
                                runSpacing: 24,
                                children: persons
                                    .map((person) => _buildPersonNode(person))
                                    .toList(),
                              ),
                            ),
                          ),
                          const SizedBox(height: 24),

                          // 关系列表
                          const Text(
                            '关系列表',
                            style: TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          const SizedBox(height: 12),
                          GridView.builder(
                            shrinkWrap: true,
                            physics: const NeverScrollableScrollPhysics(),
                            gridDelegate:
                                const SliverGridDelegateWithFixedCrossAxisCount(
                                  crossAxisCount: 1,
                                  childAspectRatio: 2.5,
                                  crossAxisSpacing: 16,
                                  mainAxisSpacing: 16,
                                ),
                            itemCount: relationships.length,
                            itemBuilder: (context, index) {
                              return _buildRelationshipCard(
                                relationships[index],
                                persons,
                              );
                            },
                          ),
                        ],
                      ),
                    );
                  } else if (snapshot.hasError) {
                    return Center(child: Text('加载失败: ${snapshot.error}'));
                  } else {
                    return const Center(child: CircularProgressIndicator());
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  // 构建人员节点
  Widget _buildPersonNode(Person person) {
    bool isMale = person.gender == '男';
    bool isAlive = person.deathDate == null;

    return Container(
      width: 150,
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 4,
            offset: const Offset(0, 2),
          ),
        ],
        border: Border(
          top: BorderSide(color: isMale ? Colors.blue : Colors.pink, width: 4),
        ),
      ),
      child: Column(
        children: [
          // 头像
          Container(
            width: 60,
            height: 60,
            decoration: BoxDecoration(
              color: isMale ? Colors.blue : Colors.pink,
              shape: BoxShape.circle,
            ),
            child: Center(
              child: Text(
                person.name.isNotEmpty ? person.name[0] : '',
                style: const TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: Colors.white,
                ),
              ),
            ),
          ),
          const SizedBox(height: 12),

          // 姓名
          Text(
            person.name,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 8),

          // 出生日期
          Text(
            person.birthDate,
            style: TextStyle(fontSize: 12, color: Colors.grey[600]),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 4),

          // 状态
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
            decoration: BoxDecoration(
              color: isAlive
                  ? Colors.lightBlueAccent.withOpacity(0.2)
                  : Colors.grey.withOpacity(0.2),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Text(
              isAlive ? '在世' : '已故',
              style: TextStyle(
                fontSize: 10,
                color: isAlive ? Colors.blue : Colors.grey[600],
              ),
            ),
          ),
        ],
      ),
    );
  }

  // 构建关系卡片
  Widget _buildRelationshipCard(
    Relationship relationship,
    List<Person> persons,
  ) {
    String person1Name = persons
        .firstWhere(
          (p) => p.id == relationship.person1Id,
          orElse: () => Person(
            id: 0,
            name: '未知',
            gender: '',
            birthDate: '',
            description: '',
          ),
        )
        .name;
    String person2Name = persons
        .firstWhere(
          (p) => p.id == relationship.person2Id,
          orElse: () => Person(
            id: 0,
            name: '未知',
            gender: '',
            birthDate: '',
            description: '',
          ),
        )
        .name;

    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Container(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // 人员关系行
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                // 人员1
                _buildPersonInfo(person1Name),

                // 关系线和类型
                Expanded(
                  child: Column(
                    children: [
                      Container(
                        height: 2,
                        color: Colors.grey[300],
                        margin: const EdgeInsets.symmetric(horizontal: 12),
                      ),
                      const SizedBox(height: 4),
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 12,
                          vertical: 4,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.blue,
                          borderRadius: BorderRadius.circular(16),
                        ),
                        child: Text(
                          relationship.type,
                          style: const TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.bold,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),

                // 人员2
                _buildPersonInfo(person2Name),
              ],
            ),

            // 描述
            if (relationship.description.isNotEmpty) ...[
              const SizedBox(height: 12),
              Text(
                relationship.description,
                style: TextStyle(
                  fontSize: 14,
                  color: Colors.grey[600],
                  fontStyle: FontStyle.italic,
                ),
                textAlign: TextAlign.center,
              ),
            ],
          ],
        ),
      ),
    );
  }

  // 构建人员信息小部件
  Widget _buildPersonInfo(String name) {
    return Column(
      children: [
        Container(
          width: 40,
          height: 40,
          decoration: BoxDecoration(color: Colors.blue, shape: BoxShape.circle),
          child: Center(
            child: Text(
              name.isNotEmpty ? name[0] : '',
              style: const TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: Colors.white,
              ),
            ),
          ),
        ),
        const SizedBox(height: 8),
        SizedBox(
          width: 80,
          child: Text(
            name,
            style: const TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
            textAlign: TextAlign.center,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    );
  }
}
