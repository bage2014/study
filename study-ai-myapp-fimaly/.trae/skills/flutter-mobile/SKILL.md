---
name: "flutter-mobile"
description: "Provides guidance for Flutter mobile app development, including cross-platform UI implementation, state management, and device-specific features. Invoke when working on mobile app-related tasks or when needing Flutter architecture advice."
---

# Flutter Mobile Development

This skill provides comprehensive guidance for developing the mobile app of the Family Tree App using Flutter.

## Core Technologies

- **Flutter 3.0+**
- **Dart**
- **Provider/Riverpod** (State Management)
- **Dio** (HTTP Requests)
- **Hive** (Local Storage)
- **Flutter Bloc** (State Management)

## Project Structure

```
frontend/flutter/
├── lib/
│   ├── widgets/           # Reusable widgets
│   ├── screens/           # Page screens
│   ├── navigation/        # Navigation configuration
│   ├── theme/             # App theme
│   ├── data/              # Data layer
│   │   ├── api/           # API services
│   │   ├── database/      # Local database
│   │   └── repository/    # Data repository
│   ├── domain/            # Business logic
│   │   ├── models/        # Data models
│   │   └── usecases/      # Use cases
│   ├── utils/             # Utility functions
│   └── main.dart          # Application entry
├── assets/                # Static assets
├── pubspec.yaml           # Flutter dependencies
└── analysis_options.yaml  # Linting configuration
```

## Key Features

### User Interface
- Material Design and Cupertino widgets
- Adaptive layout for different screen sizes
- Dark mode support
- Responsive design

### Navigation
- Flutter Navigator 2.0
- Named routes
- Deep linking support
- Bottom navigation bar

### State Management
- Provider for simple state
- Riverpod for complex state
- Flutter Bloc for business logic
- StatefulWidget for local state

### API Integration
- Dio for HTTP requests
- Interceptors for authentication
- Offline support with Hive
- Error handling and retry logic

### Core Screens

#### Login/Register
- Authentication forms
- Biometric authentication
- Social login options

#### Home
- Family overview
- Quick access to features
- Recent activities

#### Family Tree
- Interactive family tree visualization
- Touch gestures (zoom, pan)
- Member node interactions

#### Member Management
- Member list with search
- Member detail view
- Add/edit member forms

#### History Records
- Timeline-based event display
- Event creation and editing
- Event filtering

#### Media Library
- Photo grid display
- Camera integration
- Media categorization

## Widget Design

### Reusable Widgets
- Custom buttons
- Form fields
- Cards
- Dialogs
- Loading indicators
- Empty states

### Feature Widgets
- FamilyTreeView
- MemberCard
- EventTimeline
- MediaGrid
- InvitationForm

## Theme and Styling

### App Theme
- Custom color scheme
- Typography settings
- Spacing guidelines
- Iconography

### Platform Adaptation
- Material Design for Android
- Cupertino for iOS
- Platform-specific features

## Performance Optimization
- Widget rebuilding optimization
- Image caching
- Network request optimization
- Memory management
- Code splitting

## Device Integration
- Camera access
- Gallery access
- Location services
- Push notifications
- Biometric authentication

## Testing
- Unit tests
- Widget tests
- Integration tests
- E2E tests with Flutter Driver

## Deployment
- iOS App Store submission
- Google Play Store submission
- CI/CD pipeline
- Version management
