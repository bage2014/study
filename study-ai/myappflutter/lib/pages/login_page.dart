import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../api/http_client.dart'; // 确保已添加此导入
import 'package:myappflutter/pages/current_location_page.dart';
import '../config/app_routes.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  // Add this declaration for the HttpClient instance
  late final HttpClient _httpClient;

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
    // Initialize the HttpClient
    _httpClient = HttpClient();
  }

  // 生成随机验证码
  void _generateCaptcha() {
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789';
    _generatedCaptcha = List.generate(
      4,
      (index) => chars[DateTime.now().microsecond % chars.length],
    ).join();
  }

  void _handleLogin() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        // 检查账号是否已锁定
        if (_accountLocked) return;
      });

      try {
        final data = await _httpClient.post(
          '/login',
          body: {
            'username': _usernameController.text,
            'password': _passwordController.text,
            if (_showCaptcha) 'captcha': _captchaController.text,
          },
        );

        setState(() {
          _loginAttempts++;

          // 修改正常登录成功后的跳转
          if (data['code'] == 200) {
            // 登录成功，重置尝试次数并导航到主菜单
            _loginAttempts = 0;
            _showCaptcha = false;
            // 使用GetX路由导航到主页面
            Get.offNamed(AppRoutes.HOME);
          } else {
            Get.snackbar('登录失败', data['message'] ?? '未知错误');
          }
          // 登录失败后的处理
          if (_loginAttempts >= 3) {
            _showCaptcha = true;
            _generateCaptcha();
            _accountLocked = true;
            _loginAttempts = 0;
          }
        });
      } catch (e) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('数据解析失败: ${e.toString()}')));
      }
    }
  }

  // 添加Mock登录处理方法
  // 修改Mock登录的跳转
  void _handleMockLogin() {
    // 直接模拟登录成功，无需网络请求
    setState(() {
      _loginAttempts = 0;
      _showCaptcha = false;
      _accountLocked = false;
    });
    // 使用GetX路由导航到主页面
    Get.offNamed(AppRoutes.HOME);
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
                      onPressed: _accountLocked ? null : _handleLogin,
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

                  // 添加Mock登录按钮（新增代码）
                  const SizedBox(height: 16),
                  SizedBox(
                    width: double.infinity,
                    height: 56,
                    child: OutlinedButton(
                      onPressed: _accountLocked ? null : _handleMockLogin,
                      style: OutlinedButton.styleFrom(
                        side: const BorderSide(color: Color(0xFF1976D2)),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                      ),
                      child: const Text(
                        'Mock登录',
                        style: TextStyle(
                          fontSize: 18,
                          color: Color(0xFF1976D2),
                        ),
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
