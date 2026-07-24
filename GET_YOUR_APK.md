# Get Your Built APK — Free, 5 Minutes, No Installs

This gets you a real, working `.apk` file without installing anything on your computer.
GitHub's servers build it for you automatically. You just download the result.

## What This App Does

Blocks incoming calls on your **Itel, Android 9** starting with:
**0700, 0201, 070806, 0209, 0723**

(No "default caller ID app" step needed — that's only required on Android 10+.
On Android 9 this app works right after you grant permissions.)

---

## Step 1: Create a free GitHub account (skip if you have one)

1. Go to https://github.com/signup
2. Sign up with your email (free, 1 minute)

## Step 2: Create a new repository

1. Go to https://github.com/new
2. Repository name: `call-blocker` (or anything)
3. Set to **Public**
4. Do NOT check "Add a README"
5. Click **Create repository**

## Step 3: Upload the project files

1. On the new repo page, click **"uploading an existing file"**
2. Open the `CallBlockerApp9` folder on your computer
3. Drag and drop **the entire contents** (all folders/files: `.github`, `app`, `build.gradle`, `settings.gradle`, `gradle.properties`) into the upload box
   - Important: drag the folders themselves so paths like `app/src/main/...` are preserved
4. Scroll down, click **"Commit changes"**

## Step 4: Let it build automatically

1. Click the **"Actions"** tab at the top of your repo
2. You'll see a workflow run called "Build APK" already running (it starts automatically on upload)
3. Wait 2–4 minutes for the green ✅ checkmark

## Step 5: Download your APK

1. Click on the finished workflow run (the one with ✅)
2. Scroll down to **"Artifacts"**
3. Click **"CallBlocker-APK"** — this downloads a ZIP
4. Unzip it — inside is `app-debug.apk`

## Step 6: Install on your Itel

1. Transfer `app-debug.apk` to your phone (USB cable, Bluetooth, WhatsApp to yourself, Google Drive — any method)
2. On your phone, tap the file
3. If asked, allow "Install from this source" / "Unknown apps"
4. Tap **Install**
5. Open the app, tap **"Enable Call Blocking"**, allow all permissions
6. Done — status should show "✓ Call Blocker is ACTIVE"

---

## Why this works better than building it yourself

- GitHub's cloud machines have full internet access to Google's Android servers (my sandbox here doesn't)
- No Android Studio, no Java setup, no Gradle wrapper issues on your end
- 100% free, no time limit
- You can re-run the "Actions" build anytime after editing code (e.g., to add more blocked prefixes) by just uploading changed files again

## If you want to add more blocked numbers later

Edit `app/src/main/java/com/example/callblocker/CallBlockerReceiver.java` right in GitHub's web editor (click the file → pencil icon), add your prefix to the `BLOCKED_PREFIXES` array, commit — it rebuilds automatically.
