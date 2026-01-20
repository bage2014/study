// 环境模型类
class Environment {
  final String name;
  final String url;
  final String description;

  const Environment({
    required this.name,
    required this.url,
    required this.description,
  });

  // 预定义的环境配置
  static const Environment web = Environment(
    name: 'Web环境',
    url: 'http://localhost:8080',
    description: '用于Web开发和测试',
  );

  static const Environment app = Environment(
    name: 'App环境',
    url: 'http://1.0.3.2:8080',
    description: '用于App开发和测试',
  );

  static const Environment production = Environment(
    name: '生产环境',
    url: 'http://127.0.0.1:8080',
    description: '正式生产环境',
  );

  // 所有环境列表
  static List<Environment> get allEnvironments => [web, app, production];
}
