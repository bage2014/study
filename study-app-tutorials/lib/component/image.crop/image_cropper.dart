import 'package:flutter/material.dart';
import 'package:image_cropper/image_cropper.dart' as origin_image_cropper;
import 'package:tutorials/locale/translations.dart';

class ImageCropper {
  static Future<String> cropImage(
      BuildContext context, String valueCrop) async {
    String _title = Translations.textOf(context, "component.crop.title");

    final croppedFile = await origin_image_cropper.ImageCropper().cropImage(
      sourcePath: valueCrop,
      compressFormat: origin_image_cropper.ImageCompressFormat.jpg,
      compressQuality: 100,
      androidUiSettings: origin_image_cropper.AndroidUiSettings(
          toolbarTitle: _title,
          toolbarColor: Colors.blue,
          toolbarWidgetColor: Colors.white,
          initAspectRatio: origin_image_cropper.CropAspectRatioPreset.original,
          hideBottomControls: true,
          lockAspectRatio: false),
      iosUiSettings: origin_image_cropper.IOSUiSettings(
        title: _title,
      ),
    );
    if (croppedFile != null) {
      return croppedFile.path;
    }
    return valueCrop;
  }
}
