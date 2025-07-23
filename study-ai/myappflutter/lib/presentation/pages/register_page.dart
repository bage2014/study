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
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  final _captchaController = TextEditingController();
  bool _isLoading = false;
  String _captchaUrl = '/captcha';
  String _requestId = ''; // 新增requestId字段
  int _step = 1; // 1表示第一步，2表示第二步
  bool _emailVerified = false; // 邮箱是否已验证
  Map<String, dynamic>? queryParameters;

  @override
  void initState() {
    super.initState();
    // 生成初始requestId
    _generateRequestId();
    // 初始化验证码URL，包含requestId
    _refreshCaptcha();
  }

  // 生成requestId的方法
  void _generateRequestId() {
    _requestId = DateTime.now().millisecondsSinceEpoch.toString();
  }

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
              // 步骤指示器
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: _step == 1 ? Colors.blue : Colors.grey,
                    ),
                    alignment: Alignment.center,
                    child: const Text(
                      '1',
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                  Container(
                    width: 40,
                    height: 2,
                    color: _step == 1 ? Colors.blue : Colors.grey,
                  ),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: _step == 2 ? Colors.blue : Colors.grey,
                    ),
                    alignment: Alignment.center,
                    child: const Text(
                      '2',
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 30.0),

              // 第一步：邮箱和验证码
              if (_step == 1) ...[
                TextFormField(
                  controller: _emailController,
                  decoration: const InputDecoration(
                    labelText: '邮箱',
                    hintText: '请输入邮箱',
                    prefixIcon: Icon(Icons.email),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return '请输入邮箱';
                    }
                    // 简单的邮箱格式验证
                    if (!value.contains('@')) {
                      return '请输入有效的邮箱地址';
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
                      onTap: _refreshCaptcha, // 修改为调用_refreshCaptcha方法
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

                // 下一步按钮
                ElevatedButton(
                  onPressed: _isLoading ? null : _verifyEmail,
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 15.0),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                  ),
                  child: _isLoading
                      ? const CircularProgressIndicator(color: Colors.white)
                      : const Text('下一步', style: TextStyle(fontSize: 16.0)),
                ),
              ] else if (_step == 2) ...[
                // 第二步：显示邮箱(不可编辑)、邮箱验证码、密码、确认密码
                TextFormField(
                  controller: _emailController,
                  enabled: false, // 禁用编辑
                  decoration: const InputDecoration(
                    labelText: '邮箱',
                    hintText: '已验证的邮箱',
                    prefixIcon: Icon(Icons.email),
                  ),
                ),
                const SizedBox(height: 20.0),

                // 原有密码输入框
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

                // 原有确认密码输入框
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
                const SizedBox(height: 30.0),

                // 新增邮箱验证码输入框
                TextFormField(
                  controller: _captchaController,
                  decoration: const InputDecoration(
                    labelText: '邮箱验证码',
                    hintText: '请输入邮箱收到的验证码',
                    prefixIcon: Icon(Icons.security),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return '请输入邮箱验证码';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 20.0),

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
              ],
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

  // 刷新验证码的方法
  void _refreshCaptcha() {
    _generateRequestId(); // 生成新的requestId
    setState(() {
      _captchaUrl =
          '/captcha?timestamp=${DateTime.now().millisecondsSinceEpoch}&requestId=$_requestId';
    });
  }

  // 验证邮箱和验证码
  void _verifyEmail() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });

      try {
        final response = await _httpClient.post(
          '/sendEmailCaptcha', // 更改为/sendEmailCaptcha
          body: {
            'email': _emailController.text,
            'captcha': _captchaController.text,
            'requestId': _requestId, // 添加requestId参数
          },
        );

        if (response['code'] == 200) {
          Get.snackbar('发送成功', response['message'] ?? '验证码发送成功');
          setState(() {
            _step = 2;
            _emailVerified = true;
            _captchaController.clear();
          });
        } else {
          Get.snackbar('发送失败', response['message'] ?? '发送失败，请重试');
          // 刷新验证码
          _refreshCaptcha();
        }
      } catch (e) {
        LogUtil.error(e.toString(), error: e);
        Get.snackbar('发送失败', '网络异常，请稍后重试');
      } finally {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  void _handleRegister() async {
    if (_formKey.currentState!.validate() && _emailVerified) {
      setState(() {
        _isLoading = true;
      });

      try {
        final response = await _httpClient.post(
          '/register',
          body: {
            'email': _emailController.text,
            'password': _passwordController.text,
            'captcha': _captchaController.text, // 添加邮箱验证码
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
