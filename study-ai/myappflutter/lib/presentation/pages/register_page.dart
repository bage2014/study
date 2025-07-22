import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../../core/config/app_routes.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({super.key});

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final HttpClient _httpClient = HttpClient();
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  final _captchaController = TextEditingController();
  bool _isLoading = false;
  String _captchaUrl = '/captcha';
  Map<String, dynamic>? queryParameters;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('用户注册')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // 账号输入
              TextFormField(
                controller: _usernameController,
                decoration: const InputDecoration(
                  labelText: '账号',
                  hintText: '请输入账号',
                  prefixIcon: Icon(Icons.person),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '请输入账号';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20.0),

              // 密码输入
              TextFormField(
                controller: _passwordController,
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: '密码',
                  hintText: '请输入密码',
                  prefixIcon: Icon(Icons.lock),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '请输入密码';
                  }
                  if (value.length < 6) {
                    return '密码长度至少为6位';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20.0),

              // 确认密码输入
              TextFormField(
                controller: _confirmPasswordController,
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: '确认密码',
                  hintText: '请再次输入密码',
                  prefixIcon: Icon(Icons.lock_outline),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '请输入确认密码';
                  }
                  if (value != _passwordController.text) {
                    return '两次输入的密码不一致';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20.0),

              // 验证码输入
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _captchaController,
                      decoration: const InputDecoration(
                        labelText: '验证码',
                        hintText: '请输入验证码',
                        prefixIcon: Icon(Icons.security),
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return '请输入验证码';
                        }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(width: 10.0),
                  // 验证码图片
                  GestureDetector(
                    onTap: () {
                      // 刷新验证码
                      setState(() {
                        _captchaUrl =
                            '/captcha?timestamp=${DateTime.now().millisecondsSinceEpoch}';
                      });
                    },
                    child: Container(
                      width: 100,
                      height: 48,
                      decoration: BoxDecoration(
                        border: Border.all(color: Colors.grey),
                        borderRadius: BorderRadius.circular(4.0),
                      ),
                      alignment: Alignment.center,
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
                  ),
                ],
              ),
              const SizedBox(height: 30.0),

              // 注册按钮
              ElevatedButton(
                onPressed: _isLoading ? null : _handleRegister,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 15.0),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                ),
                child: _isLoading
                    ? const CircularProgressIndicator(color: Colors.white)
                    : const Text('立即注册', style: TextStyle(fontSize: 16.0)),
              ),
              const SizedBox(height: 20.0),

              // 已有账号，去登录
              TextButton(
                onPressed: () {
                  Get.offNamed(AppRoutes.LOGIN);
                },
                child: const Text(
                  '已有账号？去登录',
                  style: TextStyle(color: Colors.blue),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _handleRegister() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });

      try {
        final response = await _httpClient.post(
          '/register',
          body: {
            'username': _usernameController.text,
            'password': _passwordController.text,
            'captcha': _captchaController.text,
          },
        );

        if (response['code'] == 200) {
          Get.snackbar('注册成功', '恭喜您，注册成功！');
          // 延迟跳转到登录页面
          Future.delayed(const Duration(seconds: 2), () {
            Get.offNamed(AppRoutes.LOGIN);
          });
        } else {
          Get.snackbar('注册失败', response['message'] ?? '未知错误');
          // 刷新验证码
          setState(() {
            _captchaUrl =
                '/captcha?timestamp=${DateTime.now().millisecondsSinceEpoch}';
          });
        }
      } catch (e) {
        LogUtil.error(e.toString(), error: e);
        Get.snackbar('注册失败', '网络异常，请稍后重试');
      } finally {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }
}
