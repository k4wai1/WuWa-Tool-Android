# WuWa Tool 🛠️

<div align="center">
  <img src="https://count.getloli.com/get/@wuwa-tool-k4wai1?name=wuwa-tool-k4wai1&theme=rule34&padding=7&offset=0&align=top&scale=1&pixelated=1&darkmode=1" alt="Moe Counter" />
  <br><br>
  <a href="https://ko-fi.com/kwai1">
    <img src="https://ko-fi.com/img/githubbutton_sm.svg" alt="Support me on Ko-fi" />
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/License-GPL--3.0-blue.svg" alt="License: GPL-3.0" />
  </a>
</div>

<br>

> 🚧 **Maintenance status** — I can't keep this project updated for the moment, so it's currently **unmaintained**. The APK releases may still work, but expect no updates or support.
>
> 🚧 **Estado de mantenimiento** — No voy a poder mantener este proyecto por el momento, así que está **sin mantenimiento activo**. Los APKs publicados pueden seguir funcionando, pero no esperes actualizaciones ni soporte.

---

**WuWa Tool** is an Android utility designed to organize, backup, and apply graphic configuration files (`.ini`) for **Wuthering Waves** securely and efficiently. It utilizes **Shizuku** to access and modify internal game files without requiring a traditional Root.

---

## ⭐ Recommended Alternative

> **This project is my own app and remains as-is.** However, since it is not actively maintained, I recommend using the project developed by **Arglax** — an external developer whose work I personally use:
>
> **Este proyecto es mi app propia y sigue disponible tal cual.** Sin embargo, como no recibe mantenimiento activo, recomiendo usar el proyecto desarrollado por **Arglax** — un desarrollador externo cuyo trabajo yo mismo uso:

* **[Mobile WuWa Config](https://github.com/Arglax/Mobile-WuWa-Config)** — Actively maintained configs for Wuthering Waves (V3.6), updated with each game patch. Includes a Discord community for support. / Configs activamente mantenidas para Wuthering Waves (V3.6), actualizadas con cada parche del juego. Incluye comunidad de Discord para soporte.

* **[WuWa Config Patcher](https://github.com/Arglax/WuWa-Mobile-Config-Patcher)** — A lightweight Android app (6 MB) that does the same as this tool and more: 1-click patching via Shizuku, safe revert, built-in CVars editor, log decryption, and more. / Una app Android ligera (6 MB) que hace lo mismo que esta herramienta y más: parcheo 1-click vía Shizuku, reversión segura, editor de CVars integrado, descifrado de logs, y más.

These are **third-party projects**, not affiliated with this repository, but they are more complete and actively maintained. / Estos son **proyectos de terceros**, no afiliados a este repositorio, pero son más completos y están activamente mantenidos.

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

---

## 📜 License

This project is licensed under the **GNU General Public License v3.0** — see the [LICENSE](LICENSE) file for details.
