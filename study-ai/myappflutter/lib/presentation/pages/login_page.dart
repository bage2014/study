import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/constants/prefs_constants.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/core/utils/prefs_util.dart';
import '../../data/api/http_client.dart';
import '../../core/config/app_routes.dart';
import '../../data/models/login_response.dart'; // 导入LoginResponse类

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
  String? _requestId; // 新增requestId字段
  Map<String, String>? queryParameters;

  @override
  void initState() {
    super.initState();
    _usernameController.text = 'zhangsan@qq.com';
    _passwordController.text = 'zhangsan123';
    _requestId = DateTime.now().millisecondsSinceEpoch.toString();
    _captchaUrl =
        '/captcha?t=${DateTime.now().millisecondsSinceEpoch}&requestId=$_requestId';
    _initPreferences();
  }

  Future<void> _initPreferences() async {
    _checkAutoLogin();
  }

  void _checkAutoLogin() async {
    final token = await PrefsUtil.getString(PrefsConstants.token);
    LogUtil.info('LoginPage _checkAutoLogin token = $token');

    if (token != null) {
      try {
        // 使用http client请求后台/checkToken接口
        final response = await _httpClient.post(
          '/checkToken',
          body: {'token': token},
        );

        // 检查响应
        if (response['code'] == 200) {
          Get.offNamed(AppRoutes.HOME);
        } else {
          // 令牌无效，清除本地存储
          await PrefsUtil.remove(PrefsConstants.token);
          await PrefsUtil.remove(PrefsConstants.userInfo);
          await PrefsUtil.remove(PrefsConstants.tokenExpireTime);
        }
      } catch (e) {
        LogUtil.error('检查token失败: $e');
        // 网络错误时，可以选择保留本地token，下次再试
      }
    }
  }

  void _handleLogin() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        if (_accountLocked) return;
      });

      try {
        final response = await _httpClient.post(
          '/login',
          body: {
            'username': _usernameController.text,
            'password': _passwordController.text,
            if (_showCaptcha) 'captcha': _captchaController.text,
            'requestId': _requestId,
          },
        );

        // 使用LoginResponse类解析响应数据
        final loginResponse = LoginResponse.fromJson(response);

        if (loginResponse.code == 200 &&
            loginResponse.data?.userToken?.token != null &&
            loginResponse.data?.user != null &&
            loginResponse.data?.userToken?.tokenExpireTime != null) {
          // 保存token和用户信息
          await PrefsUtil.setString(
            PrefsConstants.token,
            loginResponse.data!.userToken!.token!,
          );
          // 移除单独缓存username的代码
          // 改为缓存整个用户信息
          await PrefsUtil.setString(
            PrefsConstants.userInfo,
            jsonEncode(loginResponse.data!.user!.toJson()),
          );
          await PrefsUtil.setString(
            PrefsConstants.tokenExpireTime,
            loginResponse.data!.userToken!.tokenExpireTime!,
          );

          Get.offNamed(AppRoutes.HOME);
        } else {
          Get.snackbar('登录失败', loginResponse.message ?? '未知错误');
        }

        setState(() {
          _loginAttempts++;
          _showCaptcha = _loginAttempts > 0; // 修改为只要尝试次数>0就显示验证码
          if (_showCaptcha) {
            // 刷新验证码
            _captchaUrl =
                '/captcha?t=${DateTime.now().millisecondsSinceEpoch}&requestId=$_requestId';
          }
          if (_loginAttempts >= 5) {
            _accountLocked = true;
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
    // 生成requestId
    _requestId = DateTime.now().millisecondsSinceEpoch.toString();
    setState(() {
      _captchaUrl =
          '/captcha?t=${DateTime.now().millisecondsSinceEpoch}&requestId=$_requestId';
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
                  const SizedBox(height: 16),
                  // 新增：注册按钮
                  TextButton(
                    onPressed: () {
                      Get.toNamed(AppRoutes.REGISTER);
                    },
                    child: const Text(
                      '还没有账号？立即注册',
                      style: TextStyle(color: Color(0xFF1976D2), fontSize: 16),
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
