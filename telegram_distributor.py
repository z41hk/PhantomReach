#!/usr/bin/env python3
import telebot
import random
import time
import re
from datetime import datetime

BOT_TOKEN = "YOUR_BOT_TOKEN_HERE"
DOWNLOAD_LINK = "https://yourdomain.com/QuickChargePro.apk"
TARGET_GROUPS = [
    "@AndroidAppsFree",
    "@ModdedApks",
    "@TechGadgets",
    "@CrackedAndroid",
    "@FreeAppDownloads"
]

bot = telebot.TeleBot(BOT_TOKEN)

MESSAGES = [
    f"🔥 Flashlight app with battery saver - no ads! Download: {DOWNLOAD_LINK}",
    f"🧨 Best flashlight I've used. 4.9 stars! Get it: {DOWNLOAD_LINK}",
    f"🚀 QuickCharge Pro - saves battery while lighting up! {DOWNLOAD_LINK}",
    f"⚡ This app made my battery last 2x longer! {DOWNLOAD_LINK}",
    f"📱 Official QuickCharge Pro - 50M+ downloads! {DOWNLOAD_LINK}"
]

def distribute():
    print("🤖 Telegram Distributor Started")
    print(f"🎯 Targeting {len(TARGET_GROUPS)} groups")
    while True:
        group = random.choice(TARGET_GROUPS)
        msg = random.choice(MESSAGES)
        try:
            bot.send_message(group, msg)
            print(f"[{datetime.now()}] Sent to {group}")
        except Exception as e:
            print(f"❌ Failed: {e}")
        # Random delay between 1-4 hours
        delay = random.randint(3600, 14400)
        print(f"⏳ Waiting {delay//60} minutes...")
        time.sleep(delay)

if __name__ == "__main__":
    try:
        distribute()
    except KeyboardInterrupt:
        print("\n👋 Shutting down distributor")