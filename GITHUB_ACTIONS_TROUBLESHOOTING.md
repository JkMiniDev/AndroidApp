# 🔧 GitHub Actions Troubleshooting Guide

## ❌ Common Issue: "Check Running" Stuck

If your GitHub Actions workflow is stuck on "check running", here are the most likely causes and solutions:

### 1. 🚩 **Gradle Wrapper Issues** (Most Common)
**Problem:** Missing or corrupted `gradle-wrapper.jar`

**✅ Solution:**
- I've already fixed this by downloading the correct `gradle-wrapper.jar`
- The file is now present at: `ClashBerry/gradle/wrapper/gradle-wrapper.jar` (61KB)

### 2. 📝 **Permission Issues**
**Problem:** `gradlew` script not executable

**✅ Solution:**
- I've made the script executable with `chmod +x ClashBerry/gradlew`
- This is also handled in the GitHub Actions workflow

### 3. 🔍 **Build Debugging**
**Problem:** Silent build failures

**✅ Solution:**
- Added debugging steps to the workflow:
  - List directory contents
  - Check gradle wrapper files
  - Added `--info` flag for verbose output

### 4. 📱 **Icon Issues**
**Problem:** Missing app icons

**✅ Solution:**
- Created vector drawable icons instead of PNG files
- Added adaptive icons for modern Android versions
- No more missing icon errors

## 🚀 How to Test the Fix

1. **Commit these changes:**
   ```bash
   git add .
   git commit -m "Fix: Add proper gradle wrapper and vector icons"
   git push origin main
   ```

2. **Check the workflow:**
   - Go to your GitHub repository
   - Click on "Actions" tab
   - Watch the workflow run with debugging output

3. **If it still fails:**
   - Click on the failed run
   - Expand the failing step
   - Look for specific error messages

## 🛠️ Manual Build Test (Local)

You can test the build locally before pushing:

```bash
cd ClashBerry
./gradlew assembleDebug --stacktrace
```

If this works locally, the GitHub Actions should work too.

## 📋 What I Fixed

| Issue | Status | Solution |
|-------|--------|----------|
| Missing gradle-wrapper.jar | ✅ Fixed | Downloaded proper 61KB JAR file |
| gradlew not executable | ✅ Fixed | Added `chmod +x` |
| Missing app icons | ✅ Fixed | Created vector drawable icons |
| Silent build failures | ✅ Fixed | Added debugging steps |
| Workflow in wrong location | ✅ Fixed | Moved to repository root |

## 🔍 Debugging Steps Added to Workflow

The workflow now includes these debugging steps:
- List ClashBerry directory contents
- Check gradle wrapper files
- Verbose build output with `--info` flag

## 🎯 Expected Workflow Steps

Your workflow should now:
1. ✅ Checkout code
2. ✅ Setup JDK 17
3. ✅ Setup Android SDK
4. ✅ Cache Gradle packages
5. ✅ Grant execute permission for gradlew
6. ✅ List directory (debugging)
7. ✅ Check gradle wrapper (debugging)
8. ✅ Build Debug APK
9. ✅ Build Release APK
10. ✅ Upload artifacts
11. ✅ Create GitHub release

## 🆘 If It Still Fails

If you're still having issues, check:

1. **Repository Settings:**
   - Actions are enabled
   - Workflow permissions are correct

2. **Branch Protection:**
   - No branch protection rules blocking the workflow

3. **Secrets/Tokens:**
   - `GITHUB_TOKEN` has proper permissions (usually automatic)

4. **File Structure:**
   - Ensure all files are committed and pushed
   - Check that `.github/workflows/build-apk.yml` is in the repository root

## 💡 Quick Fix Commands

If you need to apply the fixes manually:

```bash
# Make gradlew executable
chmod +x ClashBerry/gradlew

# Download proper gradle wrapper (if needed)
cd ClashBerry
curl -L -o gradle/wrapper/gradle-wrapper.jar "https://github.com/gradle/gradle/raw/v8.0.0/gradle/wrapper/gradle-wrapper.jar"

# Test build
./gradlew assembleDebug
```

The project should now build successfully! 🎉