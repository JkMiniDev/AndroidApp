# ClashBerry - Android War Search App

ClashBerry is an Android application for searching and analyzing Clash of Clans clan wars. It provides a modern, dark-themed interface to view war statistics, attacks, defenses, and member rosters.

## Features

- 🔍 Search clan wars by clan tag
- 📊 View detailed war statistics
- ⚔️ Track attacks and defenses
- 👥 Browse clan member rosters
- 🎨 Beautiful dark theme with ClashBerry green accents
- 📱 Native Android experience

## Screenshots

*Screenshots will be added after the app is built*

## Installation

### From GitHub Releases

1. Go to the [Releases](../../releases) page
2. Download the latest `ClashBerry-v{version}.apk` file
3. Install the APK on your Android device
4. Ensure you have "Install from unknown sources" enabled

### Building from Source

1. Clone this repository
2. Open the project in Android Studio
3. Build and run the project

## Backend Setup

The app requires a backend API to fetch clan war data. The backend code is included in the root directory:

### Local Development

1. Install Python dependencies:
   ```bash
   pip install -r requirements.txt
   ```

2. Set your Clash of Clans API token:
   ```bash
   export COC_API_TOKEN="your_api_token_here"
   ```
   Get your token from: https://developer.clashofclans.com/

3. Run the backend:
   ```bash
   python app.py
   ```

### Production Deployment

Deploy the backend to any cloud service that supports Python/Flask applications:
- Heroku
- Vercel
- Railway
- Google Cloud Platform
- AWS

After deploying, update the `BASE_URL` in `ClashBerry/app/src/main/java/com/jkminidev/clashberry/network/NetworkModule.kt` with your backend URL.

## App Configuration

### Backend URL

Update the backend URL in the NetworkModule:

```kotlin
// ClashBerry/app/src/main/java/com/jkminidev/clashberry/network/NetworkModule.kt
private const val BASE_URL = "https://your-backend-url.com/"
```

### App Icon

To add your custom app icon:

1. Replace the file `ClashBerry/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png` with your icon
2. The icon should be a PNG file
3. Recommended size: 192x192 pixels for xxxhdpi
4. No need for multiple sizes - the system will scale automatically

## Building APK with GitHub Actions

This project includes a GitHub Actions workflow that automatically builds APK files:

### Trigger Build

1. Push to `main` or `master` branch
2. Create a pull request
3. Manually trigger via "Actions" tab → "Build APK" → "Run workflow"

### Download APK

After the workflow completes:
1. Go to the "Actions" tab
2. Click on the latest workflow run
3. Download the APK from the "Artifacts" section

### Release APK

When you push to the main branch, the workflow will:
1. Build both debug and release APKs
2. Create a new GitHub release
3. Upload the release APK to the release

## Project Structure

```
ClashBerry/
├── app/
│   ├── src/main/
│   │   ├── java/com/jkminidev/clashberry/
│   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   ├── data/              # Data models
│   │   │   ├── network/           # API service and networking
│   │   │   ├── ui/                # UI helpers
│   │   │   ├── utils/             # Utility classes
│   │   │   └── MainActivity.kt    # Main activity
│   │   └── res/                   # Android resources
│   └── build.gradle               # App-level Gradle config
├── .github/workflows/             # GitHub Actions
├── app.py                         # Backend API
├── requirements.txt               # Python dependencies
└── README.md                      # This file
```

## API Endpoints

The backend provides the following endpoints:

- `GET /` - API health check
- `GET /health` - Health status
- `GET /api/war/{clan_tag}` - Get war data for a clan
- `GET /api/clan/{clan_tag}` - Get basic clan information

## Technologies Used

### Android App
- **Kotlin** - Primary programming language
- **Material Design 3** - UI framework
- **Retrofit** - HTTP client for API calls
- **Glide** - Image loading
- **View Binding** - Type-safe view references

### Backend API
- **Python Flask** - Web framework
- **Flask-CORS** - Cross-origin resource sharing
- **Requests** - HTTP library for external API calls

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Disclaimer

This project is not affiliated with, endorsed by, or sponsored by Supercell. Clash of Clans is a trademark of Supercell.

## Support

If you encounter any issues or have questions:
1. Check the [Issues](../../issues) page
2. Create a new issue if your problem isn't already reported
3. Provide detailed information about the problem

## Credits

- Built with ❤️ for the Clash of Clans community
- Uses the official Clash of Clans API
- Inspired by the need for better war tracking tools