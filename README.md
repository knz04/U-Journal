# 📝 U-Journal

**U-Journal** is a modern journaling app built with Jetpack Compose. It allows users to document their life entries enriched with images and geolocation, all wrapped in a visually appealing and intuitive mobile experience.

---

## 🚀 Features

### 🔐 Onboarding
- **Login/Sign Up** screen to authenticate users before accessing the app.

### 📘 Journey Activity
- Displays a list of user journal entries.
- App Bar with greeting, search, and profile placeholder.
- Bottom Navigation for switching between:
  - Journey
  - Calendar
  - Media
  - Atlas
- Floating Action Button (FAB) for creating a new journal entry.

### 🆕 New Entry Screen
- Text area for adding journal content.
- Media button to attach photos from camera or gallery.
- Geotag button to tag location.
- Save entry and return to main screen.

### 📄 Entry Detail Screen
- Displays selected journal in detail:
  - Date & time
  - Description
  - Image
- App Bar includes Back, Edit, and Delete options.

### ✏️ Edit Entry Screen
- Similar to New Entry, but pre-filled with selected content.
- Includes Delete option with confirmation dialog.

### 🗓️ Calendar Screen
- Calendar view showing marked dates with existing entries.
- Tapping on a date navigates to the entry details.

### 🖼️ Media Screen
- Gallery of all uploaded journal images.
- Clicking an image opens the corresponding entry.

### 🗺️ Atlas Screen
- Google Maps view showing markers for entries with geotags.
- Tapping on a marker shows a pop-up with entry info and "View" button.

---

## 📦 Libraries Used

- **Jetpack Compose** – UI Toolkit
- **Navigation Compose** – Navigation between screens
- **Google Maps Compose** – Atlas integration with markers
- **CameraX & Activity Results API** – Media integration
- **Material3** – Modern Android UI components
- **Kotlin Coroutines & StateFlow** – Reactive state management

---

## 🔧 Setup

1. Clone the repository.
2. Open in Android Studio Arctic Fox or later.
3. Add your **Google Maps API key** to `AndroidManifest.xml`:
   ```xml
   <meta-data
     android:name="com.google.android.geo.API_KEY"
     android:value="YOUR_API_KEY_HERE"/>

## Kelompok 1: <[|SMD|]>

- 68900	Kanza Amanda
- 68930	Dimas Takeda Wukir
- 73280	Rifqi Habib Ur Rahman
- 76381	Raden Muhammad Rafael Herdani
