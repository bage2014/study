import 'package:flutter/material.dart';

class FamilyResponse {
  final int code;
  final String message;
  final FamilyData? data;

  FamilyResponse({required this.code, required this.message, this.data});

  factory FamilyResponse.fromJson(Map<String, dynamic> json) {
    return FamilyResponse(
      code: json['code'],
      message: json['message'],
      data: json['data'] != null ? FamilyData.fromJson(json['data']) : null,
    );
  }
}

class FamilyData {
  final int? id;
  final String? name;
  final String? avatar;
  final int? generation;
  final String? relationship;
  final int? relatedId;
  final List<FamilyData>? children;

  FamilyData({
    this.id,
    this.name,
    this.avatar,
    this.generation,
    this.relationship,
    this.relatedId,
    this.children,
  });

  factory FamilyData.fromJson(Map<String, dynamic> json) {
    return FamilyData(
      id: json['id'],
      name: json['name'],
      avatar: json['avatar'],
      generation: json['generation'],
      relationship: json['relationship'],
      relatedId: json['relatedId'],
      children: json['children'] != null
          ? (json['children'] as List)
                .map((e) => FamilyData.fromJson(e))
                .toList()
          : null,
    );
  }
}
