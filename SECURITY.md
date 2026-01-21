# Security Policy

## Xavfsizlik Ma'lumotlari

### Implemented Security Measures

#### ✅ Network Security
- HTTPS only komunikatsiya (Firebase)
- Certificate pinning (Firebase SDK orqali)
- Network security config (Android)

#### ✅ Data Protection
- Firestore Security Rules implemented
- Anonymous response submission
- No PII (Personally Identifiable Information) collection
- Response encryption ready (AnonymousVoteService)

#### ✅ Authentication
- Firebase Authentication ready
- Admin role-based access control
- Email/Password authentication

#### ✅ Code Security
- ProGuard/R8 code obfuscation (release builds)
- No hardcoded secrets in code
- Environment variables for configuration
- `.gitignore` configured for sensitive files

### ⚠️ Production Checklist

Before deploying to production, complete these security tasks:

1. **API Keys Rotation**
   - Create new Firebase project for production
   - Generate new API keys
   - Update `.env.local` with production keys
   - Never commit `.env.local` to git

2. **Firebase App Check**
   ```
   Enable in Firebase Console → App Check
   - SafetyNet for Android
   - reCAPTCHA v3 for Web (admin panel)
   ```

3. **Firestore Security Rules**
   - Test rules thoroughly
   - Enable `isAdmin()` function check
   - Add rate limiting rules
   - Monitor security dashboard

4. **SSL/TLS**
   - Ensure HTTPS for all Firebase connections
   - Use Firebase Hosting for admin panel (auto HTTPS)

5. **Code Obfuscation**
   - Enable ProGuard in release builds
   - Test release build thoroughly
   - Keep mapping files for crash reports

## Reporting Security Issues

If you discover a security vulnerability, please email:

📧 **security@smartsorovnoma.uz**

**Please DO NOT create a public GitHub issue for security vulnerabilities.**

### What to Include:
- Type of issue (e.g., SQL injection, XSS, etc.)
- Full paths of affected files
- Step-by-step instructions to reproduce
- Proof-of-concept or exploit code (if possible)
- Impact of the issue

### Response Time
- **Critical**: 24 hours
- **High**: 48 hours
- **Medium**: 7 days
- **Low**: 14 days

## Security Best Practices

### For Developers

1. **Never commit sensitive data**
   ```bash
   # Always check before committing
   git diff --cached
   
   # If accidentally committed
   git filter-branch --force --index-filter \
     "git rm --cached --ignore-unmatch path/to/sensitive/file" \
     --prune-empty --tag-name-filter cat -- --all
   ```

2. **Keep dependencies updated**
   ```bash
   # Check for updates
   ./gradlew dependencyUpdates
   
   # For npm (admin panel)
   npm audit
   npm update
   ```

3. **Use environment variables**
   - Never hardcode API keys
   - Use `.env.local` for development
   - Use hosting platform env vars for production

4. **Code review**
   - Review all PRs for security issues
   - Use static analysis tools
   - Run security scans regularly

### For Administrators

1. **Firebase Console Access**
   - Enable 2FA for all admin accounts
   - Use strong passwords (min 16 characters)
   - Limit number of admin users
   - Audit admin actions regularly

2. **Database Security**
   - Review Firestore rules monthly
   - Monitor suspicious activity
   - Set up alerts for unusual patterns
   - Regular security audits

3. **API Key Management**
   - Restrict API keys by app (Android, Web)
   - Restrict by HTTP referrer (for web)
   - Rotate keys quarterly
   - Monitor API usage

4. **Backup Strategy**
   - Daily Firestore backups
   - Secure backup storage
   - Test restore procedures
   - Keep backups for 30 days

## Compliance

### GDPR Compliance (if applicable)
- [ ] Privacy Policy published
- [ ] Cookie consent (admin panel)
- [ ] Data export functionality
- [ ] Data deletion on request
- [ ] Data processing agreement

### Data Collection
**What we collect:**
- Survey responses (anonymous)
- Survey metadata (title, questions, timestamps)
- Response counts (aggregated)

**What we DON'T collect:**
- User personal information (unless explicitly added to survey)
- Location data
- Device information (beyond basic Android version)
- Browsing history

## Security Audit Log

| Date | Version | Issue | Severity | Status |
|------|---------|-------|----------|--------|
| 2026-01-21 | v1.0 | Initial security setup | - | Completed |
| - | - | - | - | - |

## Third-Party Dependencies

### Android App
- Firebase SDK (Google) - vetted, secure
- Jetpack Compose (Google) - official
- Kotlin Coroutines (JetBrains) - official

### Admin Panel
- Next.js (Vercel) - trusted
- Firebase SDK (Google) - vetted
- React (Meta) - trusted

## Security Contacts

- **Security Team**: security@smartsorovnoma.uz
- **General Support**: support@smartsorovnoma.uz
- **Emergency**: +998 XX XXX XXXX

## Acknowledgments

We appreciate security researchers who responsibly disclose vulnerabilities. Contributors will be acknowledged (with permission) in our security hall of fame.

---

Last Updated: 2026-01-21
