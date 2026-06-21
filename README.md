🌐 Multi-Threaded Java HTTP Server

Designed and implemented a lightweight HTTP server from scratch using Java sockets and multithreading concepts.

✨ Features
• Handles multiple clients concurrently using a thread-per-request architecture.
• Supports HTTP GET requests and serves static HTML files.
• Measures and logs response times for performance monitoring.
• Maintains detailed server logs including timestamps, thread names, and client IP addresses.
• Implements custom 404 error handling.
• Demonstrates low-level HTTP and TCP communication without external frameworks.

🛠 Technologies
Java • TCP Sockets • Multithreading • HTTP • File I/O

📚 Key Concepts
• Client-Server Architecture
• Socket Programming
• Concurrent Request Handling
• HTTP Protocol Internals
• Performance Monitoring
• Logging and Error Handling

This project was built to gain practical experience with networking fundamentals and understand how web servers process multiple requests simultaneously.


## Run
1. Compile:
   javac src/com/webserver/*.java

2. Run:
   java com.webserver.Server

3. Open:
   http://localhost:8080
