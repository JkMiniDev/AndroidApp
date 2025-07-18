# ClashBerry - Clash of Clans War Search App 🍓⚔️

A complete Android application and backend API for searching and analyzing Clash of Clans clan wars with a beautiful ClashBerry-themed UI.

## 🚀 Features

- **Android App**: Native Android application with Material Design 3
- **War Search**: Search clan wars by clan tag
- **Real-time Data**: Live war statistics and member attacks
- **Beautiful UI**: ClashBerry-themed dark design with neon green accents
- **Detailed Analytics**: View attacks, defenses, roster, and war overview
- **Backend API**: Flask-based REST API for war data processing

## 📱 Android App

### Package Details
- **App Name**: ClashBerry
- **Package**: `com.jkminidev.clashberry`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Building the APK

#### Automatic Build (GitHub Actions)
1. Push code to your GitHub repository
2. GitHub Actions will automatically build the APK
3. Download the APK from the Actions artifacts or Releases

#### Manual Build
```bash
cd ClashBerry
./gradlew assembleDebug    # Debug APK
./gradlew assembleRelease  # Release APK
```

### App Icon Setup
To replace the default app icon:

**Option 1 (Recommended)**: Single icon file
- Rename your icon to `ic_launcher.png`
- Place it in any of these folders:
  - `ClashBerry/app/src/main/res/mipmap-hdpi/`
  - `ClashBerry/app/src/main/res/mipmap-mdpi/`
  - `ClashBerry/app/src/main/res/mipmap-xhdpi/`
  - `ClashBerry/app/src/main/res/mipmap-xxhdpi/`
  - `ClashBerry/app/src/main/res/mipmap-xxxhdpi/`

**Option 2**: Multiple sizes for different densities
- `mipmap-mdpi/ic_launcher.png` (48x48)
- `mipmap-hdpi/ic_launcher.png` (72x72)
- `mipmap-xhdpi/ic_launcher.png` (96x96)
- `mipmap-xxhdpi/ic_launcher.png` (144x144)
- `mipmap-xxxhdpi/ic_launcher.png` (192x192)

## 🔧 Backend API

### Setup Instructions

1. **Install Dependencies**:
   ```bash
   pip install -r requirements.txt
   ```

2. **Get Clash of Clans API Token**:
   - Visit [Clash of Clans Developer Portal](https://developer.clashofclans.com/)
   - Create an account and get your API token
   - Set environment variable:
     ```bash
     export COC_API_TOKEN="your_token_here"
     ```

3. **Run Locally**:
   ```bash
   python app.py
   ```

4. **Deploy to Web Service**:
   - Deploy `app.py` and `requirements.txt` to your preferred hosting service
   - Set the `COC_API_TOKEN` environment variable
   - Update the Android app's API URL

### API Endpoints

- `GET /` - Health check
- `GET /api/health` - API health check
- `GET /api/war/{clan_tag}` - Get war data for a clan
- `GET /api/clan/{clan_tag}` - Get basic clan information

## 📲 Android App Configuration

### Updating Backend URL

After deploying your backend, update the API URL in the Android app:

1. Open `ClashBerry/app/src/main/java/com/jkminidev/clashberry/api/ApiClient.java`
2. Replace `"https://your-backend-url.com/"` with your actual backend URL
3. Rebuild the APK

Alternatively, you can call `ApiClient.updateBaseUrl("https://your-backend-url.com/")` from MainActivity after hosting.

## 🛠️ Development Setup

### Prerequisites
- Android Studio or Android SDK command line tools
- Java 17 or higher
- Python 3.8+ (for backend)
- Git

### Project Structure
```
ClashBerry/
├── app/                          # Android app source
│   ├── src/main/
│   │   ├── java/com/jkminidev/clashberry/
│   │   │   ├── MainActivity.java
│   │   │   ├── api/             # API client classes
│   │   │   ├── models/          # Data models
│   │   │   ├── adapters/        # RecyclerView adapters
│   │   │   └── fragments/       # Tab fragments
│   │   └── res/                 # Android resources
│   └── build.gradle
├── gradle/                      # Gradle wrapper
├── app.py                       # Backend Flask API
├── requirements.txt             # Python dependencies
└── README.md
```

### GitHub Actions Workflow

The project includes a GitHub Actions workflow (`.github/workflows/build-apk.yml`) that:
- Automatically builds APKs on push to main/master
- Creates releases with downloadable APKs
- Caches Gradle dependencies for faster builds

## 🎨 Design Theme

The app uses a dark theme with ClashBerry branding:
- **Primary Color**: Neon Green (#00FF88)
- **Background**: Deep Black (#000000)
- **Surface**: Dark Gray (#1A1A1A)
- **Text**: White (#FFFFFF)
- **Accent**: ClashBerry Green variations

## 🔑 Environment Variables

### Backend
- `COC_API_TOKEN`: Your Clash of Clans API token (required)
- `PORT`: Server port (default: 5000)

## 📋 TODO / Future Enhancements

- [ ] Add clan war league (CWL) detection and round tracking
- [ ] Implement defense tracking and analysis
- [ ] Add war history tracking
- [ ] Implement push notifications for war updates
- [ ] Add clan member statistics over time
- [ ] Create web dashboard interface
- [ ] Add war attack optimization suggestions

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## ⚠️ Important Notes

1. **API Token Security**: Never commit your COC_API_TOKEN to version control
2. **Rate Limits**: Be aware of Clash of Clans API rate limits
3. **Testing**: Test the app with various clan tags and war states
4. **Updates**: Keep dependencies updated for security

## 🔗 Links

- [Clash of Clans API Documentation](https://developer.clashofclans.com/)
- [Android Developer Documentation](https://developer.android.com/)
- [Flask Documentation](https://flask.palletsprojects.com/)

---

**Happy Clashing! 🍓⚔️**