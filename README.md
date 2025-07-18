# ClashBerry

Clash of Clans war search application with Android app and Flask backend.

## Structure

- `ClashBerry/` - Android app project
- `backend/` - Flask API backend  
- `.github/workflows/` - GitHub Actions for APK building

## Quick Setup

### Backend
```bash
cd backend
pip install -r requirements.txt
export COC_API_TOKEN="your_token"
python app.py
```

### Android App
1. Add your app icon to `ClashBerry/app/src/main/res/mipmap-*/ic_launcher.png`
2. Update backend URL in `MainActivity.kt`
3. Push to GitHub to build APK automatically

## App Details

- **Name:** ClashBerry
- **Package:** com.jkminidev.clashberry
- **Features:** War search, dark theme, WebView display

## API Endpoints

- `GET /api/war/<clan_tag>` - Get war data
- `GET /api/clan/<clan_tag>` - Get clan info
- `GET /health` - Health check

## GitHub Actions

Workflow automatically builds APK on push to main branch.

## License

MIT License