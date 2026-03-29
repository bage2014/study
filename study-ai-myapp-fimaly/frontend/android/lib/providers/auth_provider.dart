import 'package:flutter/material.dart';
import 'package:family_tree_app/models/user.dart';
import 'package:family_tree_app/services/auth_service.dart';
import 'package:hive/hive.dart';

class AuthProvider extends ChangeNotifier {
  User? _user;
  String? _token;
  bool _isLoading = false;
  String? _error;

  User? get user => _user;
  String? get token => _token;
  bool get isLoading => _isLoading;
  String? get error => _error;
  bool get isLoggedIn => _token != null;

  Future<void> login(String token) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      // Save token to Hive
      await Hive.box('auth').put('token', token);
      _token = token;

      // Get user info
      final authService = AuthService();
      authService.setAuthToken(token);
      _user = await authService.getCurrentUser(token);

      // Save user info to Hive
      await Hive.box('auth').put('user', _user!.toJson());
    } catch (e) {
      _error = e.toString();
      _token = null;
      _user = null;
      await Hive.box('auth').delete('token');
      await Hive.box('auth').delete('user');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> logout() async {
    _isLoading = true;
    notifyListeners();

    try {
      // Clear token and user info
      await Hive.box('auth').delete('token');
      await Hive.box('auth').delete('user');
      _token = null;
      _user = null;
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> checkAuthStatus() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      // Check if token exists in Hive
      final token = Hive.box('auth').get('token');
      if (token != null) {
        _token = token;
        
        // Get user info from Hive or API
        final userJson = Hive.box('auth').get('user');
        if (userJson != null) {
          _user = User.fromJson(userJson);
        } else {
          final authService = AuthService();
          authService.setAuthToken(token);
          _user = await authService.getCurrentUser(token);
          await Hive.box('auth').put('user', _user!.toJson());
        }
      }
    } catch (e) {
      _error = e.toString();
      _token = null;
      _user = null;
      await Hive.box('auth').delete('token');
      await Hive.box('auth').delete('user');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}