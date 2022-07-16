import 'package:flutter_easyloading/flutter_easyloading.dart';

/// EasyLoading.show(status: 'loading...');
// EasyLoading.showProgress(0.3, status: 'downloading...');
// EasyLoading.showSuccess('Great Success!');
// EasyLoading.showError('Failed with Error');
// EasyLoading.showInfo('Useful Information.');
// EasyLoading.showToast('Toast');
// EasyLoading.dismiss();
class Loading {
  static void showLoading(String title, double value) {
    EasyLoading.showProgress(value, status: title);
  }

  static void dismiss() {
    EasyLoading.dismiss();
  }
}
