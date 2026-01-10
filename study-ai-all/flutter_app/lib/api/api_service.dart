import 'dart:convert';
import 'package:flutter_app/models/user.dart';
import 'package:http/http.dart' as http;

class ApiService {
  static const String baseUrl = 'http://localhost:8080';

  // 获取用户列表
  static Future<List<User>> getUsers() async {
    final response = await http.get(Uri.parse('$baseUrl/api/users'));

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => User.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load users');
    }
  }
}
