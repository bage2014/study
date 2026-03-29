import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:family_tree_app/models/family.dart';
import 'package:family_tree_app/models/member.dart';
import 'package:family_tree_app/models/event.dart';
import 'package:family_tree_app/models/media.dart';

class FamilyService {
  final Dio _dio;

  FamilyService(this._dio);

  Future<List<Family>> getFamilies() async {
    try {
      final response = await _dio.get('/families');
      return (response.data as List).map((json) => Family.fromJson(json)).toList();
    } catch (e) {
      throw Exception('Failed to get families: $e');
    }
  }

  Future<Family> createFamily(String name, {String? description, String? avatar}) async {
    try {
      final response = await _dio.post('/families', data: {
        'name': name,
        'description': description,
        'avatar': avatar,
      });
      return Family.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to create family: $e');
    }
  }

  Future<Family> getFamily(int familyId) async {
    try {
      final response = await _dio.get('/families/$familyId');
      return Family.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to get family: $e');
    }
  }

  Future<Family> updateFamily(int familyId, {String? name, String? description, String? avatar}) async {
    try {
      final response = await _dio.put('/families/$familyId', data: {
        'name': name,
        'description': description,
        'avatar': avatar,
      });
      return Family.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to update family: $e');
    }
  }

  Future<void> deleteFamily(int familyId) async {
    try {
      await _dio.delete('/families/$familyId');
    } catch (e) {
      throw Exception('Failed to delete family: $e');
    }
  }

  // Member-related methods
  Future<List<Member>> getFamilyMembers(int familyId) async {
    try {
      final response = await _dio.get('/families/$familyId/members');
      return (response.data as List).map((json) => Member.fromJson(json)).toList();
    } catch (e) {
      throw Exception('Failed to get family members: $e');
    }
  }

  Future<Member> addFamilyMember(int familyId, Member member) async {
    try {
      final response = await _dio.post('/families/$familyId/members', data: member.toJson());
      return Member.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to add family member: $e');
    }
  }

  Future<Member> getMember(int memberId) async {
    try {
      final response = await _dio.get('/members/$memberId');
      return Member.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to get member: $e');
    }
  }

  Future<Member> updateMember(int memberId, Member member) async {
    try {
      final response = await _dio.put('/members/$memberId', data: member.toJson());
      return Member.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to update member: $e');
    }
  }

  Future<void> deleteMember(int memberId) async {
    try {
      await _dio.delete('/members/$memberId');
    } catch (e) {
      throw Exception('Failed to delete member: $e');
    }
  }

  // Event-related methods
  Future<List<Event>> getFamilyEvents(int familyId) async {
    try {
      final response = await _dio.get('/families/$familyId/events');
      return (response.data as List).map((json) => Event.fromJson(json)).toList();
    } catch (e) {
      throw Exception('Failed to get family events: $e');
    }
  }

  Future<Event> addFamilyEvent(int familyId, Event event) async {
    try {
      final response = await _dio.post('/families/$familyId/events', data: event.toJson());
      return Event.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to add family event: $e');
    }
  }

  Future<Event> getEvent(int eventId) async {
    try {
      final response = await _dio.get('/events/$eventId');
      return Event.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to get event: $e');
    }
  }

  Future<Event> updateEvent(int eventId, Event event) async {
    try {
      final response = await _dio.put('/events/$eventId', data: event.toJson());
      return Event.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to update event: $e');
    }
  }

  Future<void> deleteEvent(int eventId) async {
    try {
      await _dio.delete('/events/$eventId');
    } catch (e) {
      throw Exception('Failed to delete event: $e');
    }
  }

  // Media-related methods
  Future<List<Media>> getFamilyMedia(int familyId) async {
    try {
      final response = await _dio.get('/families/$familyId/media');
      return (response.data as List).map((json) => Media.fromJson(json)).toList();
    } catch (e) {
      throw Exception('Failed to get family media: $e');
    }
  }

  Future<List<Media>> getFamilyMediaByType(int familyId, String type) async {
    try {
      final response = await _dio.get('/families/$familyId/media/type/$type');
      return (response.data as List).map((json) => Media.fromJson(json)).toList();
    } catch (e) {
      throw Exception('Failed to get family media by type: $e');
    }
  }

  Future<Media> uploadMedia(int familyId, Media media) async {
    try {
      final response = await _dio.post('/families/$familyId/media', data: media.toJson());
      return Media.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to upload media: $e');
    }
  }

  Future<void> deleteMedia(int mediaId) async {
    try {
      await _dio.delete('/media/$mediaId');
    } catch (e) {
      throw Exception('Failed to delete media: $e');
    }
  }
}