# Release Notes

## Table of Contents

- [Release Notes v2.0.1](#release-notes-v201)
- [Release Notes v2.0.0](#release-notes-v200)
- [Release Notes v1.7.0](#release-notes-v170)

---

# Release Notes v2.0.1

## Overview
Version 2.0.1 is a patch release that addresses a critical bug in WebDriver log retrieval and includes documentation improvements and build enhancements.

## Bug Fixes
- **WebDriver Log Retrieval**: Fixed augmenter bug when using WebDriver log retrieval functionality (c4406ca)

## Improvements
- **Documentation**: Updated documentation structure for better organization and clarity (45adbe0)
- **Build System**: Added release notes document and Gradle task for automated release note generation (0c417bc)

## Technical Details
- This patch release maintains full compatibility with v2.0.0
- The log retrieval fix ensures proper functionality when using browser logging capabilities when using a grid

---

# Release Notes v2.0.0

## Overview
Version 2.0.0 represents a major architectural overhaul with a focus on lazy WebDriver creation, enhanced mobile support through Appium, unified configuration management, and comprehensive logging capabilities.

## Breaking Changes
- **Lazy WebDriver Creation**: WebDriver instances are now created only on first interaction (682ad9d)
- **Configuration Changes**: Browser and Selenium configs are now passed at startup rather than initialization (b3de223, d67fabf, 32302b2)
- **Actor/Performer API**: runAs methods now return Actor instead of throwing directly (57b6c2f, c6c0ce7)
- **Either Return Type**: runAs now returns Either instead of throwing exceptions (c8ed2dd)
- **Actor Decorator**: New implementation not using Either object but throwing ActivityError (fefa6d9)

## New Features

### Mobile Browser Support
- **Appium Integration**: Complete mobile browser implementation using Appium (b3166f0)
- **File Operations**: Upload files to mobile devices (d621e85) and download files from Android devices (dd1beae)
- **Video Recording**: Added video recording support for Appium (0ce36c3)
- **Appium Packaging**: Implement Appium package with Selenium download option (c6c0b81)

### Enhanced Browser Capabilities
- **ListenToBrowserLogs**: New ability for browser log collection with lazy driver creation (682ad9d)
- **Diagonal Drawing**: Added diagonal drawing functions (ed6db53)
- **Shadow DOM Support**: Locator support for finding elements within shadow roots (b33abc2)
- **DOM Manipulation**: Implement DOM attribute and property activities (ad78553)
- **Advanced Scrolling**:
    - Scroll down by pixel amount (88c6fa6)
    - Scroll to end of area (abc4f5b)
    - Scroll.element() task for Selenium (ff6c429)
- **Geometry Tasks**: Implement Geometry task for web elements in Selenium and Appium (e78d38f)
- **Visibility Tasks**: Create visibility task for elements (130435e)
- **Key Input**: DoKey.press(CharSequence) for entering key sequences (99bee6d, a024577)

### Configuration Management
- **Browser Startup Configuration**: Comprehensive browser startup configuration system (8e85ea4)
- **Configuration Properties**: Browser config and Selenium config properties (32302b2)
- **NONE Config**: Selenium NONE config with defaultConfig name passing (2f0d0a6)
- **Config Names as Options**: Pass config names as option objects (75b7b37)
- **Wait Factor Properties**: Configurable wait factors for browser elements, core retry, and core see (bb75561)

### Core Framework Enhancements
- **Additional runAs Methods**: Implement additional runAs methods on tasks (8880862)
- **runAs$ Method**: Add runAs$ method for activities with log info (48cf91d)
- **Default Temp Directory**: Implement default temporary directory (83e5cfb)
- **Performer Integration**: Return Actor from Performer with runAs method (57b6c2f, c6c0ce7)

### HTTP Module
- **Multipart Upload**: Implement multipart upload for file parts and content parts (cc0e315)

### Assertions
- **Enhanced Assertions**: Equal assertion with description support (b6a1187)

## Improvements

### Logging & Debugging
- **Activity Info Logs**: Add comprehensive activity info logging (097c157)
- **Debug Improvements**:
    - Debug print connection headers for WebSocket (875f4ec)
    - Debug print execution of See activity (27ad018)
    - Improve locator printing in debug mode (5a62aa4)
- **URL Sanitation**: URL info log sanitation for user and password (1e239ed)
- **Null Safety**: Prevent null pointer exceptions in parameter string conversion (1014078)

### File Handling
- **Remote File Path**: Set remote file path for file uploads (be61689)
- **File Separator Fix**: Fix file separator when uploading multiple files (6a2d62b)
- **Startup Config**: Pass startup config to local browser instances (6f4cece)

### Element Handling
- **HTML Attributes**: Fix outer and inner HTML attribute handling (756c1a4)
- **Element State**: Calculate element state after first element locator action (6f4cece)

## Bug Fixes
- **Multiple Issues**: General bug fixes across multiple components (14b4258)
- **Javadoc**: Fix Javadoc parameter names and errors (2c92c5d, b562c7d, 4a8a4d5)

## Build & Infrastructure
- **Gradle 8.13**: Update Gradle to version 8.13 (ecd4f0c)
- **Spotless Integration**: Integrate Gradle Spotless plugin for code formatting (0a35b62)
- **Build Permissions**: Use filePermissions instead of fileMode (2249834)
- **Sonatype Central**: Use Sonatype Central snapshot URL (b2c3bd7)
- **Gradle Group**: Set Gradle group metadata (35e21b3)
- **Code Formatting**: Apply code formatter across codebase (8538693, d0db856)
- **Vavr Update**: Bump Vavr to version 0.10.6 (8e35345)
- **Appium Client**: Update Appium client to version 10.0 (abc0e83)

## Testing & Quality
- **Test Updates**: Update tests and add log4j test files (abc0e83, e14ad15)
- **Build Info**: Add build info to .gitignore (2dfba46)

## Documentation
- **Documentation Improvements**: Add and improve documentation across modules (000f482)
- **Javadoc Fixes**: Fix Javadoc errors in several classes (4a8a4d5)

## Migration Guide (v1.7.0 → v2.0.0)

### Configuration Changes
1. **Browser Configuration**: Update browser initialization to use new startup configuration
   ```java
   // Old approach - direct instantiation
   
   // New approach - configuration-based
   // Use browser startup configuration with config names
   ```

2. **runAs Method Changes**: Update error handling for runAs methods
   ```java
   // Old: runAs throws exceptions directly
   
   // New: runAs returns Either or Actor
   ```

### Mobile Testing
- Enable Appium support for mobile browser testing
- Configure video recording for mobile test sessions
- Use new file upload/download capabilities for mobile devices

### Logging
- Implement ListenToBrowserLogs ability for enhanced log collection
- Update log handling to work with lazy WebDriver creation

## Technical Requirements
- **Gradle**: Minimum version 8.13
- **Java**: Compatible with updated Vavr 0.10.6
- **Appium**: Client version 10.0 for mobile testing
- **Selenium**: Enhanced with lazy driver creation and BiDi support

## Deprecations
- Direct WebDriver instantiation patterns (use lazy creation)
- Old runAs exception handling patterns
- Legacy configuration initialization methods

---

# Release Notes v1.7.0

## Overview
This release focuses on enhanced activity logging, screenshot capabilities, video recording, file upload functionality, and improved BrowserStack integration.

## New Features

### Activity Logging & Reporting
- **Enhanced Activity Log**: Added end time and duration tracking for tasks (32bf422, 0125110)
- **Ability Log Dump**: New feature to dump ability logs with screenshot support (1cf381f, a82477b, f4c37e1)
- **Multi-Actor Reports**: Support for activity log report creation with multiple actors (ef413a8)

### Screenshot & Video Recording
- **TakeScreenshot Activity**: New activity for capturing screenshots of pages and elements (d6e4619)
  - Added `saveTo()` method for saving screenshots to specific locations (1b6ae1a)
  - Enhanced debug and error logging (e981070)
- **Video Recording**: 
  - Selenium config video recording support (3619eca)
  - Video recording detection and checks (222dd0a)
  - Video attachment support in activity logs (b930fcc)

### File Operations
- **File Upload**: Comprehensive file upload functionality (bc4911a, 903f0d1, e1f4cde)
- **Local File System**: Set and manage local file system operations (bc4911a)
- **File Conversion**: URI conversion utilities (2691fbd, ca6d860)

### BrowserStack Integration
- **BrowserStack Executor**: New executor implementation for BrowserStack (6275b7e)
- **Status Script Optimization**: Execute status scripts only when running on BrowserStack (c15f273)
- **Type Safety**: Removed unnecessary casts (a17431a)

### Core Framework Enhancements
- **Task Error Handling**: New `runAs` method for tasks throwing ActivityError exceptions (94c1bd3)
- **Predicate Tasks**: Implementation for retry mechanisms (6385492, 9b4cb74, 4a0567a)
- **Frame Element Finder**: New utility for finding elements within frames (ccab668)
- **Runner Class**: New runner implementation for test execution (8ce9723)

### Cucumber Integration
- **Data Generator**: Added Cucumber data generator library (7a16497)

### Utilities
- **Formatted Output**: New utility class for formatted console output (b715302)

## Improvements

### Browser Functionality
- **Method Chaining**: Enhanced ability dump method chains (170c526)
- **Scroll Behavior**: 
  - Removed scroll into view for ExecuteJavaScript (31bce16)
  - Improved vertical scrolling to top of area (124f39b)

### Configuration & Setup
- **Selenium Configuration**: Fixed missing config when using default Chrome browser (fdc0b53)
- **Browser Setup**: Fixed remote and selenium browser configuration (84de2e5)

### Logging & Debugging
- **Activity Log Checkbox**: Fixed OK value checkbox in activity logs (d560cf1)
- **See Action Logging**: Improved See action activity log (49e4336)
- **Screenshot Logging**: Removed image logging in success cases to reduce noise (e066679)
- **Failing Actions**: Removed screenshots from failing actions (6ed5d6d)

## Bug Fixes
- **Local File Handling**: Fixed local file upload and logging issues (90d5d34, e1f4cde, 903f0d1)
- **URI Conversion**: Fixed file to URI conversion (ca6d860, 2691fbd)
- **Activity Log UI**: Fixed button icons in activity log interface (6240a7d)

## Documentation & Build
- **Documentation**: Updated "Just the Docs" version (3f50717)
- **Build System**: Updated to latest Axion Gradle plugin version (8a8de03)

## Breaking Changes
- Screenshots are no longer automatically captured on failing actions
- Some scroll behavior changes in ExecuteJavaScript activities

## Migration Notes
- Update any custom screenshot handling to use the new TakeScreenshot activity
- Review scroll behavior if using ExecuteJavaScript with scroll expectations
- BrowserStack users can benefit from the new executor and status script optimizations

## Technical Details
- Enhanced video recording capabilities require proper Selenium grid configuration
- File upload operations now support both local and remote file systems
- Activity logging now includes precise timing information for performance analysis
