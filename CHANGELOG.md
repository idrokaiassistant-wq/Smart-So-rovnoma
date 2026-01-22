# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased] - v1.1.0 Planning
### Added
- **Feedback System:** Users can send anonymous feedback via Settings.
- **Admin Export:** Export survey results and feedback to CSV.
- **Role-Based Access:** Admin Panel support for `admin`, `editor`, and `viewer` roles.

### Security
- **Firestore Rules:** Updated to support role-based access control.
- **Rate Limiting:** Server-side timestamp checks for feedback submission.

---

## [1.0.0] - 2026-01-22
### Added
- Initial Release.
- Anonymous survey participation.
- Multi-language support (UZ, RU, EN).
- Dark Mode support.
- Persistent Anti-Spam cooldown (60s).
- Privacy Policy link in Settings.
