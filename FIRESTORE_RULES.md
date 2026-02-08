# Firestore Security Rules (For Development)

To fix "PERMISSION_DENIED" errors during development, replace your Firestore Rules with this permissive configuration.

**Steps:**
1.  Go to [Firebase Console](https://console.firebase.google.com/).
2.  Select your project "WallyMe".
3.  Click **Firestore Database** in the left menu.
4.  Click the **Rules** tab.
5.  Delete everything and paste the following:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

6.  Click **Publish**.
7.  Wait ~1 minute for changes to propagate.
8.  Try the "Test DB" button again in the Admin app.

**Note:** These rules allow anyone with your project ID to read/write data. **Switch to secure rules before releasing to production.**
