# 📍 Location Alarm — Project Documentation

---

## 1. 🌟 Core Concept

**Location Alarm** is an Android application that triggers an alarm when the user reaches near a selected geographic destination. It is designed for travelers, commuters, and daily passengers who may fall asleep or lose track of their location during transit.

Instead of time‑based alarms, this app uses **GPS‑based geofencing logic** to alert users based on *distance to a destination*.

**Core Idea:**
> “Wake me up when I reach this place.”

---

## 2. 🎯 Problem It Solves

| Problem | Solution by App |
|--------|-----------------|
| Missing bus/train stop | Alarm triggers near stop |
| Traveling long distances | No need to watch map constantly |
| Night travel safety | Alerts when nearing destination |
| Forgetting to track location | App tracks automatically |

---

## 3. 🧠 System Architecture

The app follows a **Client‑Side GPS Monitoring Architecture**.

```
User → UI Layer → Location Service → Distance Engine → Alarm Engine
```

### 🔹 Components

| Layer | Responsibility |
|------|----------------|
| UI Layer | Screens, buttons, map, user input |
| Permission Manager | Handles location permissions |
| Location Engine | Gets live GPS coordinates |
| Destination Manager | Stores selected destination |
| Distance Calculator | Calculates distance in meters |
| Alarm Controller | Plays sound + vibration |

---

## 4. ⚙️ How the App Works (Flow)

1. App asks for location permission.
2. GPS tracking starts.
3. User selects destination on map.
4. App continuously calculates distance between:
   - Current Location
   - Destination Location
5. When distance ≤ alarm threshold → Alarm triggers.

---

## 5. ✨ Features

### Core Features
- 📍 Live location tracking
- 🗺 Map‑based destination selection
- 📏 Real‑time distance calculation
- 🔔 Alarm sound trigger
- 📳 Phone vibration alert
- 🎯 Adjustable distance threshold

### Smart Behaviors
- Alarm triggers only once
- Automatically updates distance
- Works while user is moving

---

## 6. 🖥 UI Design Overview

### Home Screen
Displays:
- Current Latitude & Longitude
- Destination status
- Distance remaining
- Alarm threshold controls
- Button to open map

### Map Screen
- Google Map view
- Tap to drop destination marker
- Confirm button

UI Principles:
- Minimal
- Travel‑friendly
- Clear information hierarchy

---

## 7. 🧩 Tech Stack

| Technology | Purpose |
|-----------|---------|
| Kotlin | App development language |
| Jetpack Compose | UI framework |
| Google Maps SDK | Map rendering |
| Fused Location Provider | GPS tracking |
| Android Location APIs | Distance calculations |
| RingtoneManager | Alarm sound |
| Vibrator API | Vibration feedback |

---

## 8. 📡 Location Logic

Distance is calculated using:

```
Location.distanceBetween()
```

This computes straight‑line distance between two GPS coordinates using Earth curvature calculations.

---

## 9. 🔊 Alarm Logic

Triggered when:

```
distanceToDestination <= threshold
```

Actions performed:
- Play system alarm sound
- Start vibration pattern

---

## 10. 🔐 Permissions Used

| Permission | Why Needed |
|-----------|------------|
| ACCESS_FINE_LOCATION | GPS accuracy |
| ACCESS_COARSE_LOCATION | Fallback location |
| VIBRATE | Alert feedback |

---

## 11. 🚀 Future Roadmap

### Phase 1 (Current)
✔ Basic alarm near location  
✔ Map selection  
✔ Distance tracking

### Phase 2
- Background tracking service
- Notification alarm instead of ringtone
- Save favorite places
- Dark mode

### Phase 3
- Multiple alarms
- Route‑aware alerts (along path)
- Public transport integration
- Smart battery optimization

---

## 12. 🧪 Testing Strategy

| Test Type | What to Test |
|----------|-------------|
| Unit Test | Distance calculation |
| Field Test | Real travel scenarios |
| Permission Test | Runtime permission flow |
| Edge Cases | GPS loss, app restart |

---

## 13. 🔮 Scalability Potential

This system can evolve into:
- Smart travel assistant
- Safety alert system
- Delivery tracking tool
- Geo‑notification platform

---

## 14. 🏁 Summary

Location Alarm is a **GPS‑triggered alert system** that shifts alarms from time‑based to location‑based logic. It demonstrates real‑time location processing, geospatial calculations, and event‑driven mobile architecture.

It is simple in concept, but powerful in real‑world usefulness.

