

import 'package:flutter/material.dart';

class VerificationTextField extends StatefulWidget {
  late TextEditingController _codeController;

  VerificationTextField(TextEditingController codeController, {Key? key})
      : super(key: key) {
    _codeController = codeController;
  }

  @override
  State<VerificationTextField> createState() => _VerificationTextFieldState();
}

class _VerificationTextFieldState extends State<VerificationTextField> {
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 64,
      height: 64,
      padding: const EdgeInsets.symmetric(horizontal: 5),
      decoration: BoxDecoration(
        color: const Color(0xFFF4F4F4),
        borderRadius: BorderRadius.circular(4),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.6),
            spreadRadius: 2,
            blurRadius: 2,
            offset: Offset.zero,
          ),
        ],
      ),
      child: TextField(
        controller: widget._codeController,
        textAlign: TextAlign.center,
        maxLength: 1,
        style: const TextStyle(
          fontSize: 28,
        ),
        decoration: const InputDecoration(
          counterText: "",
        ),
      ),
    );
  }
}