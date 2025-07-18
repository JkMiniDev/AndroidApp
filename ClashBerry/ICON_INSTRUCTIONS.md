# App Icon Instructions

## Quick Setup

To add your ClashBerry app icon, follow these simple steps:

### 1. Prepare Your Icon
- Create a PNG image of your ClashBerry logo/icon
- Recommended size: **192x192 pixels** (this will work for all screen densities)
- Make sure it's a square image with transparent background if needed

### 2. Replace the Placeholder
- Rename your icon file to `ic_launcher.png`
- Replace the placeholder file at: `ClashBerry/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png`

### 3. That's It!
- The Android build system will automatically scale your icon for all screen densities
- No need to create multiple sizes manually

## File Locations

Your icon file should be placed here:
```
ClashBerry/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png
```

## What Happens During Build

When you build the APK:
1. Android will use your xxxhdpi icon as the master
2. It will automatically generate smaller versions for other densities:
   - hdpi (72x72)
   - mdpi (48x48) 
   - xhdpi (96x96)
   - xxhdpi (144x144)
   - xxxhdpi (192x192) - your original

## Icon Design Tips

- Use your ClashBerry brand colors (especially the signature green #00FF88)
- Keep the design simple and recognizable at small sizes
- Consider a dark background since the app uses a dark theme
- Avoid text in the icon (it won't be readable at small sizes)
- Use high contrast elements

## Example Icon Ideas

- A stylized "C" for ClashBerry
- Berry-themed icon with clash elements
- Sword or shield with berry elements
- Abstract geometric design in ClashBerry colors

## Troubleshooting

If your icon doesn't appear correctly:
1. Make sure the file is exactly named `ic_launcher.png`
2. Ensure it's a valid PNG file
3. Check that the file size isn't too large (keep under 1MB)
4. Verify the image is square (same width and height)

## After Adding Your Icon

1. Commit and push your changes to GitHub
2. The GitHub Action will automatically build a new APK with your icon
3. Download the APK from the Actions artifacts or Releases page
4. Install and enjoy your custom ClashBerry app!