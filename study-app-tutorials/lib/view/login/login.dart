import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/cache/token_caches.dart';
import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/request/login_requests.dart';
import 'package:tutorials/request/model/user.dart';
import 'package:tutorials/request/model/login/login_request_param.dart';
import 'package:tutorials/request/model/security_code_request_param.dart';
import 'package:tutorials/request/security_code_requests.dart';
import 'package:tutorials/utils/app_utils.dart';

class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  _LoginView createState() => _LoginView();
}

class _LoginView extends State<Login> {
  bool _obscureText = true;
  bool _isLoading = false;
  bool _loginSecurityCodeRequired = false;

  TextEditingController userNameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController securityCodeController = TextEditingController();

  @override
  void initState() {
    super.initState();
    userNameController.text = ('bage@qq.com');
    passwordController.text = ('123456');

    TokenCaches.getAccessToken().then((token) {
      if (token?.isNotEmpty ?? false) {
        Logs.info(' try login ');
        // 直接尝试访问
        LoginRequests.tryLogin(token).then((result) {
          if (result.common.code == ErrorCodeConstant.success) {
            UserCaches.cacheUser(User.from(result));
            AppUtils.toPage(context, RouteNameConstant.route_name_home);
          }
        });
      } else {
        Logs.info('need to login ');
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    String _imageUrl = SecurityCodeRequests.url(SecurityCodeRequestParam());
    return Stack(
      alignment: Alignment.center, //指定未定位或部分定位widget的对齐方式
      children: <Widget>[
        Scaffold(
          backgroundColor: const Color(0xFFFCFCFC),
          body: SafeArea(
            child: SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const SizedBox(height: 40),
                    GestureDetector(
                      child: const Image(
                          image: AssetImage("assets/images/logo128.png")),
                      onDoubleTap: () {
                        envSetting();
                      },
                    ),
                    const SizedBox(height: 62),
                    Text(
                      Translations.textOf(context, 'login.title'),
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.w600,
                        color: Color(0xFF262626),
                      ),
                    ),
                    const SizedBox(height: 24),
                    TextField(
                      controller: userNameController,
                      decoration: InputDecoration(
                        hintText:
                            Translations.textOf(context, 'login.account.hint'),
                        hintStyle: const TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w400,
                          color: Color(0xFFA8A8A8),
                        ),
                        prefixIcon:
                            const Icon(Icons.person, color: Color(0xFFA8A8A8)),
                        contentPadding: const EdgeInsets.symmetric(
                            horizontal: 17, vertical: 22),
                        border: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                        focusedBorder: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                        enabledBorder: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFFD0D0D0))),
                      ),
                    ),
                    const SizedBox(height: 14),
                    Row(
                      children: [
                        Expanded(
                          child: TextField(
                            controller: passwordController,
                            obscureText: _obscureText,
                            decoration: InputDecoration(
                              hintText: Translations.textOf(
                                  context, 'login.password.hint'),
                              hintStyle: const TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.w400,
                                color: Color(0xFFA8A8A8),
                              ),
                              suffixIcon: IconButton(
                                  icon: Icon(!_obscureText
                                      ? Icons.visibility
                                      : Icons.visibility_off),
                                  onPressed: () {
                                    setState(() {
                                      _obscureText = !_obscureText;
                                    });
                                  }),
                              prefixIcon: const Icon(Icons.vpn_key,
                                  color: Color(0xFFA8A8A8)),
                              contentPadding: const EdgeInsets.symmetric(
                                  horizontal: 17, vertical: 22),
                              border: const OutlineInputBorder(
                                  borderSide:
                                      BorderSide(color: Color(0xFFD0D0D0))),
                              focusedBorder: const OutlineInputBorder(
                                  borderSide:
                                      BorderSide(color: Color(0xFFD0D0D0))),
                              enabledBorder: const OutlineInputBorder(
                                  borderSide:
                                      BorderSide(color: Color(0xFFD0D0D0))),
                            ),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 14),
                    !_loginSecurityCodeRequired
                        ? const SizedBox(height: 0)
                        : Row(
                            children: [
                              Expanded(
                                child: TextField(
                                  controller: securityCodeController,
                                  decoration: InputDecoration(
                                    hintText: Translations.textOf(
                                        context, 'login.security.hint'),
                                    hintStyle: const TextStyle(
                                      fontSize: 14,
                                      fontWeight: FontWeight.w400,
                                      color: Color(0xFFA8A8A8),
                                    ),
                                    prefixIcon: const Icon(
                                        Icons.security_rounded,
                                        color: Color(0xFFA8A8A8)),
                                    contentPadding: const EdgeInsets.symmetric(
                                        horizontal: 17, vertical: 22),
                                    border: const OutlineInputBorder(
                                        borderSide: BorderSide(
                                            color: Color(0xFFD0D0D0))),
                                    focusedBorder: const OutlineInputBorder(
                                        borderSide: BorderSide(
                                            color: Color(0xFFD0D0D0))),
                                    enabledBorder: const OutlineInputBorder(
                                        borderSide: BorderSide(
                                            color: Color(0xFFD0D0D0))),
                                  ),
                                ),
                              ),
                              const SizedBox(width: 10),
                              GestureDetector(
                                onTap: () {
                                  _imageUrl = SecurityCodeRequests.url(
                                      SecurityCodeRequestParam());
                                },
                                child: Container(
                                  width: 120,
                                  height: 64,
                                  decoration: BoxDecoration(
                                    border: Border.all(
                                      color: const Color(0xFFD0D0D0),
                                    ),
                                  ),
                                  child: Center(
                                    child: CachedNetworkImage(
                                      imageUrl: _imageUrl,
                                      placeholder: (context, url) =>
                                          const CircularProgressIndicator(),
                                      errorWidget: (context, url, error) =>
                                          const Image(
                                              image: AssetImage(
                                                  "assets/images/user_null.png")),
                                      height: 64,
                                      width: 120,
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          ),
                    const SizedBox(height: 24),
                    ElevatedButton(
                      onPressed: () {
                        login();
                      },
                      style: ElevatedButton.styleFrom(
                        primary: const Color(0xFF0043CE),
                        elevation: 0,
                        shadowColor: Colors.transparent,
                        fixedSize: const Size(342, 64),
                      ),
                      child: Text(
                        Translations.textOf(context, 'login.button'),
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w600,
                          color: Color(0xFFF4F4F4),
                        ),
                      ),
                    ),
                    const SizedBox(height: 16),
                    _button(
                        text: Translations.textOf(context, 'login.register'),
                        route: RouteNameConstant.route_name_register,
                        isTransparent: true),
                    _button(
                        text: Translations.textOf(
                            context, 'login.password.reset'),
                        route: RouteNameConstant.route_name_forget_password,
                        isTransparent: true),
                  ],
                ),
              ),
            ),
          ),
        ),
        Container(
          child: _isLoading
              ? Container(
                  color: Colors.black54.withOpacity(0.5),
                  width: double.infinity,
                )
              : null,
        ),
        Container(
          child: _isLoading
              ? CircularProgressIndicator(
                  backgroundColor: Colors.grey[200],
                  valueColor: const AlwaysStoppedAnimation(Colors.blue),
                )
              : null,
        ),
      ],
    );
  }

  Widget _button(
          {required String text,
          required String route,
          bool isTransparent = false}) =>
      ElevatedButton(
        onPressed: () {
          AppUtils.toPage(context, route);
        },
        style: ElevatedButton.styleFrom(
          primary: isTransparent ? Colors.transparent : const Color(0xFF0043CE),
          elevation: 0,
          shadowColor: Colors.transparent,
          fixedSize: const Size(342, 64),
        ),
        child: Text(
          text,
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.w600,
            color: isTransparent
                ? const Color(0xFF0043CE)
                : const Color(0xFFF4F4F4),
          ),
        ),
      );

  showLoading() {
    setState(() {
      _isLoading = true;
    });
  }

  hideLoading() {
    setState(() {
      _isLoading = false;
    });
  }

  void login() {
    LoginRequestParam param = buildParam();

    // 输入框校验
    bool ok = isOk(param);
    if (!ok) {
      return;
    }

    showLoading();

    LoginRequests.login(param).then((result) {
      Logs.info('login result=' + (result.toString() ?? ""));
      hideLoading();
      if (result.common.code == ErrorCodeConstant.success) {
        _loginSecurityCodeRequired = false;
        Toasts.show(Translations.textOf(context, "login.success.toast"));
        UserCaches.cacheUser(User.from(result));
        AppUtils.toPage(context, RouteNameConstant.route_name_home);
      } else if (result.common.code ==
          ErrorCodeConstant.loginSecurityCodeRequired) {
        Toasts.show(result.common.message);
        _loginSecurityCodeRequired = true;
      } else {
        Toasts.show(result.common.message);
      }
    }).catchError((error) {
      Toasts.show(error.toString());
      Logs.info(error.toString());
      hideLoading();
    });
  }

  LoginRequestParam buildParam() {
    LoginRequestParam param = LoginRequestParam();
    param.userName = userNameController.text;
    param.password = passwordController.text;
    param.securityCode = securityCodeController.text;
    return param;
  }

  void envSetting() async {
    Navigator.of(context).pushNamed(RouteNameConstant.route_name_env);
  }

  bool isOk(LoginRequestParam param) {
    if (param.userName == null || (param.userName?.isEmpty ?? true)) {
      Toasts.show(Translations.textOf(context, 'login.validation.username'));
      return false;
    }

    if (param.password == null || (param.password?.isEmpty ?? true)) {
      Toasts.show(Translations.textOf(context, 'login.validation.password'));
      return false;
    }

    if (_loginSecurityCodeRequired &&
        (param.securityCode == null || (param.securityCode?.isEmpty ?? true))) {
      Toasts.show(Translations.textOf(context, 'login.validation.security'));
      return false;
    }

    return true;
  }
}
