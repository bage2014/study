import 'package:flutter/material.dart';
import 'package:sn_progress_dialog/sn_progress_dialog.dart' as sn_progress_dialog;

class ProgressDialog {

  late sn_progress_dialog.ProgressDialog _pd;

  ProgressDialog(BuildContext context){
    _pd = sn_progress_dialog.ProgressDialog(context: context);
  }


  void showProgress(String title, int max) {
    _pd.show(max: max, msg: title);
  }

  void updateProgress(int value) {
    _pd.update(value: value);
  }

  void dismiss() {
    _pd.close();
  }

}
