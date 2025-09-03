import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../widgets/base_page.dart';

class FamilyEditPage extends StatefulWidget {
  const FamilyEditPage({super.key});

  @override
  State<FamilyEditPage> createState() => _FamilyEditPageState();
}

class _FamilyEditPageState extends State<FamilyEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _relationshipController = TextEditingController();
  final TextEditingController _avatarController = TextEditingController();

  @override
  void dispose() {
    _nameController.dispose();
    _relationshipController.dispose();
    _avatarController.dispose();
    super.dispose();
  }

  void _saveFamilyMember() {
    if (_formKey.currentState!.validate()) {
      // 保存逻辑
      Get.snackbar('success', 'family_member_saved');
      Get.back();
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'edit_family_member',
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              TextFormField(
                controller: _nameController,
                decoration: InputDecoration(
                  labelText: 'name'.tr,
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'name_required'.tr;
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
                controller: _relationshipController,
                decoration: InputDecoration(
                  labelText: 'relationship'.tr,
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'relationship_required'.tr;
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              TextFormField(
                controller: _avatarController,
                decoration: InputDecoration(
                  labelText: 'avatar_url'.tr,
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 24),
              ElevatedButton(
                onPressed: _saveFamilyMember,
                child: Text('save'.tr),
              ),
            ],
          ),
        ),
      ),
    );
  }
}