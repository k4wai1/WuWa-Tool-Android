# WuWa Tool 🛠️

<div align="center">
  <img src="https://count.getloli.com/@hio?name=hio&theme=rule34&padding=7&offset=0&align=top&scale=1&pixelated=1&darkmode=1&num=5687" alt="Moe Counter" />
  <br><br>
  <a href="https://ko-fi.com/kwai1">
    <img src="https://ko-fi.com/img/githubbutton_sm.svg" alt="Support me on Ko-fi" />
  </a>
</div>

<br>

**WuWa Tool** is an Android utility designed to organize, backup, and apply graphic configuration files (`.ini`) for **Wuthering Waves** securely and efficiently. It utilizes **Shizuku** to access and modify internal game files without requiring a traditional Root.

---

## 📥 Download

Go to the **[Releases Page](../../releases)** to download the latest APK ready for your Android.

---

## 🔗 Need Configs?
If you are looking for optimized configuration files to use with this tool, check out this great repository:
* **[Mobile WuWa Configs by Arglax](https://github.com/Arglax/Mobile-WuWa-Config)**

---

## ⚠️ Disclaimer & Safety Warning

**PLEASE READ CAREFULLY BEFORE USE:**

1.  **Not Affiliated:** This application is an unofficial tool and is **NOT** affiliated, endorsed, or supported by **Kuro Games**.
2.  **Use at Your Own Risk:** This tool modifies internal game configuration files (`Engine.ini`, `GameUserSettings.ini`, `DeviceProfiles.ini`). While modifying `.ini` files for performance is a common practice, **modifying game clients always carries a theoretical risk.**
3.  **No Liability:** The developer (k4wai1) is not responsible for any bans, game crashes, or data loss resulting from the use of this tool.

---

## ✨ Features

* **Smart Injection:** Applies configurations intelligently. It doesn't matter if your downloaded config is buried in 10 subfolders; the app finds the `.ini` files and puts them where they belong.
* **Auto-Backup:** The first time you use the app, it creates a secure backup of your original files in `.WuWa_Backup`.
* **Material You UI:** A modern interface that adapts to your device's theme (Dark/Light mode support).
* **Root-less Operation:** Uses **Shizuku** (ADB) to access `Android/data`, preserving the security of your device.

---

## 📂 Folder Structure Guide

**Recommended Structure:**
```text
Internal Storage/
└── WuWa_Configs/          <-- Select this folder in the App
    ├── MaxFPS/            <-- Config Name (Appears in App)
    │   ├── Engine.ini
    │   └── GameUserSettings.ini
    └── HighQuality/       <-- Config Name (Appears in App)
        └── Engine.ini
```

---

## 🤝 Credits & Acknowledgments

* **[Android Code Studio](https://github.com/AndroidCSOfficial/android-code-studio):** For the incredible development environment (AndroidIDE).
* **[Shizuku](https://github.com/RikkaApps/Shizuku):** For the non-root permission API.
* **[Arglax](https://github.com/Arglax/Mobile-WuWa-Config):** For the config examples.

### 🤖 Special Note
The User Interface (UI) and logic implementation of this application were developed with the assistance of **Google Gemini AI**.

---

## 👨‍💻 Developer
**[k4wai1](https://github.com/k4wai1)**
