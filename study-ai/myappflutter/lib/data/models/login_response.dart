class LoginResponse {
  final int? code;
  final String? message;
  final Data? data;

  LoginResponse({
    this.code,
    this.message,
    this.data,
  });

  factory LoginResponse.fromJson(Map<String, dynamic>? json) {
    if (json == null) {
      return LoginResponse();
    }
    return LoginResponse(
      code: json['code'],
      message: json['message'],
      data: json['data'] != null ? Data.fromJson(json['data']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'code': code,
      'message': message,
      'data': data?.toJson(),
    };
  }
}

class Data {
  final User? user;
  final UserToken? userToken;

  Data({
    this.user,
    this.userToken,
  });

  factory Data.fromJson(Map<String, dynamic>? json) {
    if (json == null) {
      return Data();
    }
    return Data(
      user: json['user'] != null ? User.fromJson(json['user']) : null,
      userToken: json['userToken'] != null ? UserToken.fromJson(json['userToken']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'user': user?.toJson(),
      'userToken': userToken?.toJson(),
    };
  }
}

class User {
  final int? id;
  final String? username;
  final String? password;
  final int? loginAttempts;
  final String? lockTime;
  final String? email;
  final String? birthDate;

  User({
    this.id,
    this.username,
    this.password,
    this.loginAttempts,
    this.lockTime,
    this.email,
    this.birthDate,
  });

  factory User.fromJson(Map<String, dynamic>? json) {
    if (json == null) {
      return User();
    }
    return User(
      id: json['id'],
      username: json['username'],
      password: json['password'],
      loginAttempts: json['loginAttempts'],
      lockTime: json['lockTime'],
      email: json['email'],
      birthDate: json['birthDate'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'username': username,
      'password': password,
      'loginAttempts': loginAttempts,
      'lockTime': lockTime,
      'email': email,
      'birthDate': birthDate,
    };
  }
}

class UserToken {
  final int? id;
  final int? userId;
  final String? token;
  final String? tokenExpireTime;

  UserToken({
    this.id,
    this.userId,
    this.token,
    this.tokenExpireTime,
  });

  factory UserToken.fromJson(Map<String, dynamic>? json) {
    if (json == null) {
      return UserToken();
    }
    return UserToken(
      id: json['id'],
      userId: json['userId'],
      token: json['token'],
      tokenExpireTime: json['tokenExpireTime'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'userId': userId,
      'token': token,
      'tokenExpireTime': tokenExpireTime,
    };
  }
}