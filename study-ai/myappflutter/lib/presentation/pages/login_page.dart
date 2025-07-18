import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/constants/prefs_constants.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/core/utils/prefs_util.dart';
import '../../data/api/http_client.dart'; // 确保已添加此导入
import '../../core/config/app_routes.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final HttpClient _httpClient = HttpClient();
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _captchaController = TextEditingController();
  int _loginAttempts = 0;
  bool _showCaptcha = false;
  bool _accountLocked = false;
  int _avatarTapCount = 0;
  DateTime? _lastAvatarTapTime;
  String _captchaUrl = '/captcha';
  Map<String, dynamic>? queryParameters;

  @override
  void initState() {
    super.initState();
    _usernameController.text = 'zhangsan';
    _passwordController.text = 'zhangsan123';
    _initPreferences();
  }

  Future<void> _initPreferences() async {
    _checkAutoLogin();
  }

  void _checkAutoLogin() async {
    final token = await PrefsUtil.getString(PrefsConstants.token);
    final expireTime = await PrefsUtil.getString(
      PrefsConstants.tokenExpireTime,
    );
    LogUtil.info(
      'LoginPage _checkAutoLogin token = $token, expireTime = $expireTime',
    );
    if (token != null &&
        expireTime != null &&
        DateTime.now().millisecondsSinceEpoch <
            DateTime.parse(expireTime).millisecondsSinceEpoch) {
      Get.offNamed(AppRoutes.HOME);
    }
  }

  void _handleLogin() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        if (_accountLocked) return;
        _loginAttempts++;
        _showCaptcha = _loginAttempts > 0; // 修改为只要尝试次数>0就显示验证码
        if (_loginAttempts >= 5) {
          _accountLocked = true;
          _loginAttempts = 0;
        }
      });

      try {
        final response = await _httpClient.post(
          '/login',
          body: {
            'username': _usernameController.text,
            'password': _passwordController.text,
            if (_showCaptcha) 'captcha': _captchaController.text,
          },
        );

        if (response['code'] == 200) {
          // 保存token和用户信息
          await PrefsUtil.setString(
            PrefsConstants.token,
            response['data']['token'],
          );
          await PrefsUtil.setString(
            PrefsConstants.username,
            response['data']['username'],
          );
          await PrefsUtil.setString(
            PrefsConstants.tokenExpireTime,
            response['data']['tokenExpireTime'],
          );

          Get.offNamed(AppRoutes.HOME);
        } else {
          Get.snackbar('登录失败', response['message'] ?? '未知错误');
        }

        setState(() {
          _loginAttempts++;
          if (_loginAttempts >= 3) {
            _showCaptcha = true;
            _accountLocked = true;
            _loginAttempts = 0;
          }
        });
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(
            context,
          ).showSnackBar(SnackBar(content: Text('数据解析失败: ${e.toString()}')));
        }
      }
    }
  }

  // 新增：处理头像点击事件
  void _handleAvatarTap() {
    const int requiredTaps = 3;
    const Duration tapWindow = Duration(milliseconds: 500);
    final DateTime now = DateTime.now();

    if (_lastAvatarTapTime == null ||
        now.difference(_lastAvatarTapTime!) > tapWindow) {
      _avatarTapCount = 1;
    } else {
      _avatarTapCount++;
      if (_avatarTapCount >= requiredTaps) {
        // 连续点击三次，导航到设置页面
        Get.toNamed(AppRoutes.SETTINGS);
        _avatarTapCount = 0;
      }
    }
    _lastAvatarTapTime = now;
  }

  Future<void> _refreshCaptcha() async {
    setState(() {
      _captchaUrl = '/captcha?t=${DateTime.now().millisecondsSinceEpoch}';
    });
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
                  // 修改：添加InkWell包裹头像图标，实现点击事件
                  InkWell(
                    onTap: _handleAvatarTap,
                    borderRadius: BorderRadius.circular(40), // 圆形点击区域
                    child: const Icon(
                      Icons.lock,
                      size: 80,
                      color: Colors.blueAccent,
                    ),
                  ),
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

                  if (_showCaptcha && !_accountLocked)
                    Column(
                      children: [
                        const SizedBox(height: 16),
                        Row(
                          children: [
                            Expanded(
                              child: TextFormField(
                                controller: _captchaController,
                                decoration: InputDecoration(
                                  labelText: '验证码',
                                  prefixIcon: const Icon(Icons.verified_user),
                                  border: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(12),
                                  ),
                                ),
                                validator: (value) =>
                                    value!.isEmpty ? '请输入验证码' : null,
                              ),
                            ),
                            const SizedBox(width: 8),
                            GestureDetector(
                              onTap: _refreshCaptcha,
                              child: Image.network(
                                _httpClient
                                    .buildUri(_captchaUrl, queryParameters)
                                    .toString(),
                                height: 50,
                                width: 100,
                                errorBuilder: (context, error, stackTrace) =>
                                    const Icon(Icons.refresh, size: 50),
                              ),
                            ),
                          ],
                        ),
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
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
