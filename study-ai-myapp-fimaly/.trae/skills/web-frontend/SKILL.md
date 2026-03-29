---
name: "web-frontend"
description: "Provides guidance for web frontend development using Vue 3+, including component design, state management, and responsive UI implementation. Invoke when working on web frontend-related tasks or when needing frontend architecture advice."
---

# Web Frontend Development

This skill provides comprehensive guidance for developing the web frontend of the Family Tree App using Vue 3+.

## Core Technologies

- **Vue 3+**
- **Vue Router**
- **Pinia** (State Management)
- **Axios** (HTTP Requests)
- **Tailwind CSS** (Styling)
- **Vite** (Build Tool)

## Project Structure

```
frontend/web/
├── src/
│   ├── components/        # Reusable components
│   │   ├── layout/        # Layout components
│   │   ├── common/        # Common components
│   │   └── features/      # Feature-specific components
│   ├── views/             # Page components
│   ├── composables/       # Custom composable functions
│   ├── services/          # API services
│   ├── stores/            # Pinia stores
│   ├── utils/             # Utility functions
│   ├── styles/            # Global styles
│   ├── router/            # Vue Router configuration
│   └── App.vue            # Application entry
├── public/                # Static assets
├── index.html             # HTML template
├── package.json           # NPM configuration
└── vite.config.js         # Vite configuration
```

## Key Features

### User Interface
- Responsive design with Tailwind CSS
- Clean, modern UI components
- Consistent design system
- Accessibility support

### Navigation
- Vue Router for client-side routing
- Protected routes for authenticated users
- Nested routes for complex views

### State Management
- Pinia for global state management
- Reactive data handling
- Persistent state with localStorage

### API Integration
- Axios for HTTP requests
- Interceptors for authentication
- Error handling and loading states

### Core Pages

#### Login/Register
- Authentication forms
- Password reset functionality
- Social login options

#### Dashboard
- Family overview
- Recent activities
- Quick access to key features

#### Family Tree
- Interactive family tree visualization
- Zoom and pan functionality
- Member node interactions

#### Member Management
- Member list with search and filter
- Member detail view
- Add/edit member forms

#### History Records
- Timeline-based event display
- Event creation and editing
- Event filtering

#### Media Library
- Photo grid display
- Upload functionality
- Media categorization

## Component Design

### Reusable Components
- Button variants
- Form inputs
- Cards
- Modals
- Alerts
- Loading indicators

### Feature Components
- FamilyTreeVisualizer
- MemberCard
- EventTimeline
- MediaGrid
- InvitationForm

## Styling

### Tailwind CSS Configuration
- Custom color palette
- Responsive breakpoints
- Typography settings
- Component-specific utilities

### Design System
- Consistent spacing
- Typography hierarchy
- Color usage guidelines
- Component sizing

## Performance Optimization
- Code splitting
- Lazy loading
- Image optimization
- Caching strategies
- Minification and bundling

## Testing
- Unit tests with Vitest
- Component tests with Vue Test Utils
- End-to-end tests with Cypress

## Deployment
- Build optimization
- Static site hosting
- CI/CD pipeline
- Environment-specific configurations
