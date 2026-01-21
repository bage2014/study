import 'dart:convert';
import 'package:flutter_app/models/user.dart';
import 'package:flutter_app/models/person.dart';
import 'package:flutter_app/models/relationship.dart';
import 'package:http/http.dart' as http;
import '../services/environment_service.dart';

class ApiService {
  static late String baseUrl;

  // 初始化ApiService，设置baseUrl
  static Future<void> initialize() async {
    final environment = await EnvironmentService.getSelectedEnvironment();
    baseUrl = environment.url;
  }

  // 手动设置baseUrl
  static void setBaseUrl(String url) {
    baseUrl = url;
  }

  // 获取用户列表（保留原有功能）
  static Future<List<User>> getUsers() async {
    final response = await http.get(Uri.parse('$baseUrl/api/users'));

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => User.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load users');
    }
  }

  // 家庭族谱API

  // 获取所有人员
  static Future<List<Person>> getAllPersons() async {
    final response = await http.get(
      Uri.parse('$baseUrl/api/family-tree/persons'),
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => Person.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load persons');
    }
  }

  // 获取所有关系
  static Future<List<Relationship>> getAllRelationships() async {
    final response = await http.get(
      Uri.parse('$baseUrl/api/family-tree/relationships'),
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => Relationship.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load relationships');
    }
  }

  // 添加人员
  static Future<Person> addPerson(Person person) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/family-tree/persons'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(person.toJson()),
    );

    if (response.statusCode == 200) {
      return Person.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to add person');
    }
  }

  // 添加关系
  static Future<Relationship> addRelationship(Relationship relationship) async {
    final response = await http.post(
      Uri.parse('$baseUrl/api/family-tree/relationships'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(relationship.toJson()),
    );

    if (response.statusCode == 200) {
      return Relationship.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to add relationship');
    }
  }

  // 删除人员
  static Future<void> deletePerson(int id) async {
    final response = await http.delete(
      Uri.parse('$baseUrl/api/family-tree/persons/$id'),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to delete person');
    }
  }

  // 删除关系
  static Future<void> deleteRelationship(int id) async {
    final response = await http.delete(
      Uri.parse('$baseUrl/api/family-tree/relationships/$id'),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to delete relationship');
    }
  }
}
