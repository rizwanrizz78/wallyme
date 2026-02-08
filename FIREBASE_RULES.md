# Firebase Storage Rules

For testing purposes (so the Admin App can upload images), your Firebase Storage rules should look like this:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;
    }
  }
}
```

**WARNING:** These rules are insecure and allow anyone to upload/delete files. For production, you must implement authentication.
