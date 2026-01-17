# WuWa Tool 🛠️

**WuWa Tool** is an Android utility designed to manage, backup, and apply graphic configuration files () for **Wuthering Waves** securely and efficiently. It utilizes **Shizuku** to access and modify internal game files without requiring a traditional Root, protecting your data with automatic backups.

---

## ⚠️ Disclaimer & Safety Warning

**PLEASE READ CAREFULLY BEFORE USE:**

1.  **Not Affiliated:** This application is an unofficial tool and is **NOT** affiliated, endorsed, or supported by **Kuro Games**.
2.  **Use at Your Own Risk:** This tool modifies internal game configuration files (, , ). While modifying  files for performance is a common practice and there have been no widespread reports of bans for this specific activity, **modifying game clients always carries a theoretical risk of account suspension or bans.**
3.  **No Liability:** The developer (k4wai1) is not responsible for any bans, game crashes, or data loss resulting from the use of this tool.

---

## ✨ Features

* **Smart Injection:** Applies configurations intelligently. It doesn't matter if your downloaded config is buried in 10 subfolders; the app finds the  files and puts them where they belong.
* **Auto-Backup:** The first time you use the app, it creates a secure backup of your original files in `.WuWa_Backup`.
* **Material You UI:** A modern interface that adapts to your device's theme (Dark/Light mode support).
* **Root-less Operation:** Uses **Shizuku** (ADB) to access `Android/data`, preserving the security of your device.

---

## 📱 Requirements

* **Android 11+** (Recommended for best compatibility).
* **[Shizuku](https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api)** installed and running.
* **Wuthering Waves** installed.

---

## 📂 Folder Structure Guide

Configs downloaded from the internet often come with complex, deep folder structures meant for manual copying. **WuWa Tool simplifies this.**

### 1. How downloaded configs usually look (Messy):
They often contain the full game path.
```text
Downloads/
└── Super_Low_Config_v2/
    └── files/
        └── UE4Game/
            └── Client/
                └── ... /
                    └── Config/
                        └── Android/
                            ├── Engine.ini
                            └── GameUserSettings.ini
```

### 2. How to organize them for WuWa Tool (Clean):
You just need to create a main folder (e.g., `WuWa_Configs`) and place folders with descriptive names inside. You don't need the full path structure.

**Recommended Structure:**
```text
Internal Storage/
└── WuWa_Configs/          <-- Select this folder in the App
    ├── MaxFPS/            <-- Config Name (Appears in App)
    │   ├── Engine.ini
    │   └── GameUserSettings.ini
    ├── HighQuality/       <-- Config Name (Appears in App)
    │   ├── Engine.ini
    │   └── DeviceProfiles.ini
    └── Potatophone/       <-- Config Name (Appears in App)
        └── Engine.ini
```
*Note: Even if you put the "Messy" folder structure inside "MaxFPS", the app's **Smart Search** will still find the files!*

---

## 🤝 Credits & Acknowledgments

This project was made possible thanks to these amazing tools and resources:

* **[Android Code Studio](https://github.com/AndroidCSOfficial/android-code-studio):** For the incredible development environment on Android (AndroidIDE).
* **[Shizuku](https://github.com/RikkaApps/Shizuku):** For providing the API to handle system permissions without root.
* **Material You:** For the design language guidelines.

### 🤖 Special Note
The User Interface (UI) and logic implementation of this application were developed with the assistance of **Google Gemini AI**, acting as a coding partner to streamline development.

---

## 👨‍💻 Developer
**[k4wai1](https://github.com/k4wai1)**
