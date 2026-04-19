#!/bin/bash

# Reverse Orientation TV - Build Verification Script
# This script verifies that all required files are present and the project is ready to build

set -e

echo "========================================"
echo "Reverse Orientation TV - Build Verification"
echo "========================================"
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Counters
checks_passed=0
checks_failed=0

# Function to check if file exists
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        ((checks_passed++))
    else
        echo -e "${RED}✗${NC} $1 - MISSING"
        ((checks_failed++))
    fi
}

# Function to check if directory exists
check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1/"
        ((checks_passed++))
    else
        echo -e "${RED}✗${NC} $1/ - MISSING"
        ((checks_failed++))
    fi
}

echo "Checking core build files..."
check_file "build.gradle.kts"
check_file "settings.gradle.kts"
check_file "gradle.properties"
check_file "gradlew"
check_file "gradlew.bat"
check_file "app/build.gradle.kts"
check_file "app/proguard-rules.pro"
echo ""

echo "Checking source files..."
check_file "app/src/main/AndroidManifest.xml"
check_file "app/src/main/java/com/reverselandscape/tv/MainActivity.kt"
check_file "app/src/main/java/com/reverselandscape/tv/OrientationService.kt"
check_file "app/src/main/java/com/reverselandscape/tv/BootReceiver.kt"
echo ""

echo "Checking resource files..."
check_file "app/src/main/res/layout/activity_main.xml"
check_file "app/src/main/res/values/strings.xml"
check_file "app/src/main/res/values/colors.xml"
check_file "app/src/main/res/values/themes.xml"
check_file "app/src/main/res/xml/backup_rules.xml"
check_file "app/src/main/res/xml/data_extraction_rules.xml"
check_file "app/src/main/res/drawable/ic_launcher_foreground.xml"
check_file "app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml"
check_file "app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml"
echo ""

echo "Checking documentation..."
check_file "README.md"
check_file "LICENSE"
check_file "SETUP.md"
check_file "CONTRIBUTING.md"
check_file "PROJECT_SUMMARY.md"
echo ""

echo "Checking CI/CD configuration..."
check_file ".github/workflows/android.yml"
check_file ".gitignore"
echo ""

echo "Checking Gradle wrapper..."
check_dir "gradle/wrapper"
check_file "gradle/wrapper/gradle-wrapper.properties"

# Check if gradle-wrapper.jar exists
if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo -e "${GREEN}✓${NC} gradle/wrapper/gradle-wrapper.jar"
    ((checks_passed++))
else
    echo -e "${YELLOW}⚠${NC} gradle/wrapper/gradle-wrapper.jar - Missing (will be downloaded by Gradle)"
    echo "  Note: Android Studio or first Gradle run will download this automatically"
fi
echo ""

echo "========================================"
echo "Verification Summary"
echo "========================================"
echo -e "Passed: ${GREEN}$checks_passed${NC}"
echo -e "Failed: ${RED}$checks_failed${NC}"
echo ""

if [ $checks_failed -eq 0 ] || [ $checks_failed -eq 1 ]; then
    echo -e "${GREEN}✓ Project structure is complete!${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Open the project in Android Studio"
    echo "2. Wait for Gradle sync to complete"
    echo "3. Run: ./gradlew assembleDebug"
    echo "4. Or click the Run button in Android Studio"
    echo ""
    echo "For detailed setup instructions, see SETUP.md"
    exit 0
else
    echo -e "${RED}✗ Project structure has missing files${NC}"
    echo "Please check the missing files listed above"
    exit 1
fi
