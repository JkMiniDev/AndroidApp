# ClashBerry

A Clash of Clans war search application with both Android app and backend API.

## Project Structure

```
├── ClashBerry/                 # Android App Project
│   ├── app/                    # Android application
│   ├── gradle/                 # Gradle wrapper
│   ├── build.gradle           # Root build configuration
│   ├── settings.gradle        # Project settings
│   └── gradlew               # Gradle wrapper script
├── backend/                   # Flask API Backend
│   ├── app.py                # Main Flask application
│   └── requirements.txt      # Python dependencies
├── .github/workflows/         # GitHub Actions
│   └── build-apk.yml         # APK build workflow
└── README.md                 # This file
```

## Android App

**App Name:** ClashBerry  
**Package:** `com.jkminidev.clashberry`  
**Features:**
- Search clan war information by clan tag
- Display war statistics, attacks, defenses, and rosters
- Dark theme with ClashBerry branding
- WebView-based war results display
- Swipe-to-refresh functionality

### App Icon Setup

To add your app icon before running the GitHub workflow:

1. Prepare your app icon as a **single PNG file**
2. Rename it to `ic_launcher.png`
3. Place it in the following directories in the ClashBerry project:
   - `ClashBerry/app/src/main/res/mipmap-hdpi/ic_launcher.png` (72x72px)
   - `ClashBerry/app/src/main/res/mipmap-mdpi/ic_launcher.png` (48x48px)
   - `ClashBerry/app/src/main/res/mipmap-xhdpi/ic_launcher.png` (96x96px)
   - `ClashBerry/app/src/main/res/mipmap-xxhdpi/ic_launcher.png` (144x144px)
   - `ClashBerry/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png` (192x192px)

> **Note:** Use the same PNG file for all directories. Android will automatically scale it as needed.

### Building APK

The project includes a GitHub Actions workflow that automatically builds APK files when you push to the main branch.

#### Manual Build (if needed)
```bash
cd ClashBerry
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK
```

## Backend API

**Technology:** Flask + Python  
**Purpose:** Web service for Clash of Clans API integration

### API Endpoints

- `GET /` - API status and available endpoints
- `GET /health` - Health check endpoint
- `GET /api/war/<clan_tag>` - Get war data for a clan
- `GET /api/clan/<clan_tag>` - Get basic clan information

### Backend Setup

1. **Install Dependencies:**
   ```bash
   cd backend
   pip install -r requirements.txt
   ```

2. **Set Environment Variables:**
   ```bash
   export COC_API_TOKEN="your_clash_of_clans_api_token"
   export PORT=5000  # Optional, defaults to 5000
   ```

3. **Get API Token:**
   - Visit [Clash of Clans Developer Portal](https://developer.clashofclans.com/)
   - Create an account and generate an API token
   - Set the token as an environment variable

4. **Run Backend:**
   ```bash
   python app.py
   ```

### Deploying to Web Service

The backend is designed to be deployed to any Python hosting service:

- **Heroku:** Use the included `requirements.txt`
- **Railway:** Supports automatic Flask deployment
- **Google Cloud Run:** Container-based deployment
- **AWS Elastic Beanstalk:** Python application deployment

#### Environment Variables for Production:
```
COC_API_TOKEN=your_actual_api_token
PORT=5000
FLASK_ENV=production
```

## Android App Configuration

After deploying the backend, update the Android app with your backend URL:

1. Open `ClashBerry/app/src/main/java/com/jkminidev/clashberry/MainActivity.kt`
2. Find the line: `private val API_BASE_URL = "https://your-backend-url.com"`
3. Replace `"https://your-backend-url.com"` with your actual backend URL

## GitHub Actions Workflow

The repository includes an automated workflow that:

1. **Triggers on:**
   - Push to main/master branch
   - Pull requests to main/master branch
   - Manual workflow dispatch

2. **Build Process:**
   - Sets up Android build environment
   - Builds both debug and release APKs
   - Uploads APKs as artifacts
   - Creates GitHub releases with APK files

3. **Artifacts:**
   - `ClashBerry-debug.apk` - Debug version for testing
   - `ClashBerry-release-unsigned.apk` - Release version (unsigned)

## Development

### Android App
- **Language:** Kotlin
- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Architecture:** Single Activity with WebView

### Backend API
- **Language:** Python 3.8+
- **Framework:** Flask with CORS enabled
- **External API:** Clash of Clans API via cocproxy.royaleapi.dev

## Features

- 🔍 **War Search:** Search any clan's current war by clan tag
- 📊 **War Statistics:** View complete war stats including stars, destruction percentage
- ⚔️ **Attack Tracking:** See all attacks with stars and destruction percentage
- 🛡️ **Defense Tracking:** Monitor incoming attacks and defenses
- 👥 **Roster View:** View complete war rosters for both clans
- 🕒 **Real-time Updates:** War time remaining and status updates
- 🌙 **Dark Theme:** ClashBerry's signature black and green theme
- 📱 **Mobile Optimized:** Responsive design for mobile devices

## API Response Example

```json
{
  "state": "inWar",
  "teamSize": 15,
  "warType": "regular",
  "timeRemaining": "2h 15m",
  "clan": {
    "tag": "#2Q82LRL",
    "name": "Example Clan",
    "stars": 42,
    "attacks": 28,
    "members": [...]
  },
  "opponent": {
    "tag": "#ABC123",
    "name": "Enemy Clan",
    "stars": 38,
    "attacks": 25,
    "members": [...]
  }
}
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.