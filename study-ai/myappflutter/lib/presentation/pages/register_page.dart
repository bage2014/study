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
    // 设置邮箱默认值
    _emailController.text = '321502897@qq.com';
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
      appBar: AppBar(title: Text('user_registration'.tr)),
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
                    child: Text(
                      'step_1'.tr,
                      style: const TextStyle(color: Colors.white),
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
                    child: Text(
                      'step_2'.tr,
                      style: const TextStyle(color: Colors.white),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 30.0),

              // 第一步：邮箱和验证码
              if (_step == 1) ...[
                TextFormField(
                  controller: _emailController,
                  decoration: InputDecoration(
                    labelText: 'email'.tr,
                    hintText: 'enter_email'.tr,
                    prefixIcon: const Icon(Icons.email),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'enter_email'.tr;
                    }
                    // 简单的邮箱格式验证
                    if (!value.contains('@')) {
                      LogUtil.info('value: $value');
                      return 'valid_email_required'.tr;
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
                        decoration: InputDecoration(
                          labelText: 'captcha'.tr,
                          hintText: 'enter_captcha'.tr,
                          prefixIcon: const Icon(Icons.security),
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'enter_captcha'.tr;
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
                      : Text(
                          'next_step'.tr,
                          style: const TextStyle(fontSize: 16.0),
                        ),
                ),
              ] else if (_step == 2) ...[
                // 第二步：显示邮箱(不可编辑)、邮箱验证码、密码、确认密码
                TextFormField(
                  controller: _emailController,
                  enabled: false, // 禁用编辑
                  decoration: InputDecoration(
                    labelText: 'email'.tr,
                    hintText: 'verified_email'.tr,
                    prefixIcon: const Icon(Icons.email),
                  ),
                ),
                const SizedBox(height: 20.0),

                // 原有密码输入框
                TextFormField(
                  controller: _passwordController,
                  obscureText: true,
                  decoration: InputDecoration(
                    labelText: 'password'.tr,
                    hintText: 'enter_password'.tr,
                    prefixIcon: const Icon(Icons.lock),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'enter_password'.tr;
                    }
                    if (value.length < 6) {
                      return 'password_min_length'.tr;
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 20.0),

                // 原有确认密码输入框
                TextFormField(
                  controller: _confirmPasswordController,
                  obscureText: true,
                  decoration: InputDecoration(
                    labelText: 'confirm_password'.tr,
                    hintText: 'enter_confirm_password'.tr,
                    prefixIcon: const Icon(Icons.lock_outline),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'enter_confirm_password'.tr;
                    }
                    if (value != _passwordController.text) {
                      return 'passwords_not_match'.tr;
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 30.0),

                // 新增邮箱验证码输入框
                TextFormField(
                  controller: _captchaController,
                  decoration: InputDecoration(
                    labelText: 'email_captcha'.tr,
                    hintText: 'enter_email_captcha'.tr,
                    prefixIcon: const Icon(Icons.security),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'enter_email_captcha'.tr;
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
                      : Text(
                          'register_now'.tr,
                          style: const TextStyle(fontSize: 16.0),
                        ),
                ),
              ],
              const SizedBox(height: 20.0),

              // 已有账号，去登录
              TextButton(
                onPressed: () {
                  Get.offNamed(AppRoutes.LOGIN);
                },
                child: Text(
                  'already_have_account'.tr,
                  style: const TextStyle(color: Colors.blue),
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
          Get.snackbar(
            'captcha_sent'.tr,
            response['message'] ?? 'captcha_send_success'.tr,
          );
          setState(() {
            _step = 2;
            _emailVerified = true;
            _captchaController.clear();
          });
        } else {
          Get.snackbar(
            'captcha_send_failed'.tr,
            response['message'] ?? 'captcha_send_failed'.tr,
          );
          // 刷新验证码
          _refreshCaptcha();
        }
      } catch (e) {
        LogUtil.error(e.toString(), error: e);
        Get.snackbar('captcha_send_failed'.tr, 'network_exception'.tr);
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
          Get.snackbar('registration_success'.tr, 'registration_successful'.tr);
          // 延迟跳转到登录页面
          Future.delayed(const Duration(seconds: 2), () {
            Get.offNamed(AppRoutes.LOGIN);
          });
        } else {
          Get.snackbar(
            'registration_failed'.tr,
            response['message'] ?? 'unknown_error'.tr,
          );
        }
      } catch (e) {
        LogUtil.error(e.toString(), error: e);
        Get.snackbar('registration_failed'.tr, 'network_exception'.tr);
      } finally {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }
}
