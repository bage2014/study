import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:family_tree_app/views/login_view.dart';
import 'package:family_tree_app/views/register_view.dart';
import 'package:family_tree_app/views/home_view.dart';
import 'package:family_tree_app/views/family_tree_view.dart';
import 'package:family_tree_app/views/members_view.dart';
import 'package:family_tree_app/views/member_detail_view.dart';
import 'package:family_tree_app/views/events_view.dart';
import 'package:family_tree_app/views/media_view.dart';
import 'package:family_tree_app/views/family_management_view.dart';
import 'package:family_tree_app/views/settings_view.dart';

final GoRouter appRouter = GoRouter(
  initialLocation: '/login',
  routes: [
    GoRoute(
      path: '/login',
      builder: (context, state) => LoginView(),
    ),
    GoRoute(
      path: '/register',
      builder: (context, state) => RegisterView(),
    ),
    GoRoute(
      path: '/home',
      builder: (context, state) => HomeView(),
    ),
    GoRoute(
      path: '/family-tree',
      builder: (context, state) {
        final familyId = state.queryParameters['familyId'];
        return FamilyTreeView(familyId: familyId);
      },
    ),
    GoRoute(
      path: '/members',
      builder: (context, state) {
        final familyId = state.queryParameters['familyId'];
        return MembersView(familyId: familyId);
      },
    ),
    GoRoute(
      path: '/member/:id',
      builder: (context, state) {
        final memberId = state.pathParameters['id'];
        return MemberDetailView(memberId: memberId);
      },
    ),
    GoRoute(
      path: '/events',
      builder: (context, state) {
        final familyId = state.queryParameters['familyId'];
        return EventsView(familyId: familyId);
      },
    ),
    GoRoute(
      path: '/media',
      builder: (context, state) {
        final familyId = state.queryParameters['familyId'];
        return MediaView(familyId: familyId);
      },
    ),
    GoRoute(
      path: '/family-management',
      builder: (context, state) {
        final familyId = state.queryParameters['familyId'];
        return FamilyManagementView(familyId: familyId);
      },
    ),
    GoRoute(
      path: '/settings',
      builder: (context, state) => SettingsView(),
    ),
  ],
);