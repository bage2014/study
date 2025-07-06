import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert'; // 添加此行导入
import 'package:myappflutter/utils/constants.dart';
import 'package:myappflutter/pages/current_location_page.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _captchaController = TextEditingController();
  int _loginAttempts = 0;
  bool _showCaptcha = false;
  bool _accountLocked = false;
  String _generatedCaptcha = '';

  @override
  void initState() {
    super.initState();
    _generateCaptcha();
  }

  // 生成随机验证码
  void _generateCaptcha() {
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789';
    _generatedCaptcha = List.generate(
      4,
      (index) => chars[DateTime.now().microsecond % chars.length],
    ).join();
  }

  void _submitForm() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        // 检查账号是否已锁定
        if (_accountLocked) return;
      });

      try {
        final url = Uri.parse('$baseUrl/login');
        final response = await http.post(
          url,
          body: {
            'username': _usernameController.text,
            'password': _passwordController.text,
            if (_showCaptcha) 'captcha': _captchaController.text,
          },
        );

        final responseBody = utf8.decode(response.bodyBytes);
        final data = json.decode(responseBody);

        setState(() {
          _loginAttempts++;

          if (data['code'] == 200) {
            // 登录成功，重置尝试次数并导航到主菜单
            _loginAttempts = 0;
            _showCaptcha = false;
            Navigator.pushReplacement(
              context,
              MaterialPageRoute(
                builder: (context) => const CurrentLocationPage(),
              ),
            );
          } else {
            // 登录失败
            final errorMessage = data['message'] ?? '请求后台异常';
            if (_loginAttempts >= 5) {
              _accountLocked = true;
            }

            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text(errorMessage)),
            );

            // 三次失败后显示验证码
            if (_loginAttempts >= 3) {
              _showCaptcha = true;
              _generateCaptcha();
            }
          }
        });
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('数据解析失败: ${e.toString()}')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFFE3F2FD), Color(0xFFBBDEFB)],
          ),
        ),
        padding: const EdgeInsets.all(24),
        child: Center(
          child: SingleChildScrollView(
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.lock, size: 80, color: Colors.blueAccent),
                  const SizedBox(height: 32),
                  const Text(
                    '用户登录',
                    style: TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1A237E),
                    ),
                  ),
                  const SizedBox(height: 32),

                  // 账号输入框
                  TextFormField(
                    controller: _usernameController,
                    decoration: InputDecoration(
                      labelText: '账号',
                      prefixIcon: const Icon(Icons.person),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      enabled: !_accountLocked,
                    ),
                    validator: (value) => value!.isEmpty ? '请输入账号' : null,
                    enabled: !_accountLocked,
                  ),
                  const SizedBox(height: 16),

                  // 密码输入框
                  TextFormField(
                    controller: _passwordController,
                    decoration: InputDecoration(
                      labelText: '密码',
                      prefixIcon: const Icon(Icons.lock),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      enabled: !_accountLocked,
                    ),
                    obscureText: true,
                    validator: (value) => value!.isEmpty ? '请输入密码' : null,
                    enabled: !_accountLocked,
                  ),
                  const SizedBox(height: 16),

                  // 验证码输入框 (错误3次后显示)
                  if (_showCaptcha && !_accountLocked)
                    Column(
                      children: [
                        TextFormField(
                          controller: _captchaController,
                          decoration: InputDecoration(
                            labelText: '验证码',
                            prefixIcon: const Icon(Icons.security),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            suffix: Container(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 8,
                                vertical: 4,
                              ),
                              decoration: BoxDecoration(
                                color: Colors.grey[200],
                                borderRadius: BorderRadius.circular(4),
                              ),
                              child: Text(
                                _generatedCaptcha,
                                style: const TextStyle(
                                  fontSize: 18,
                                  letterSpacing: 4,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.blueGrey,
                                ),
                              ),
                            ),
                          ),
                          validator: (value) =>
                              value!.isEmpty ? '请输入验证码' : null,
                          maxLength: 4,
                        ),
                        const SizedBox(height: 16),
                      ],
                    ),

                  // 账号锁定提示
                  if (_accountLocked)
                    const Padding(
                      padding: EdgeInsets.symmetric(vertical: 16),
                      child: Text(
                        '账号已锁定，请10分钟后再试',
                        style: TextStyle(color: Colors.red, fontSize: 16),
                      ),
                    ),

                  // 登录按钮
                  SizedBox(
                    width: double.infinity,
                    height: 56,
                    child: ElevatedButton(
                      onPressed: _accountLocked ? null : _submitForm,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFF1976D2),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                      ),
                      child: const Text(
                        '登录',
                        style: TextStyle(fontSize: 18, color: Colors.white),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
