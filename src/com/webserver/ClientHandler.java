package com.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        // Increment and log active clients
        int currentClients = Server.activeClients.incrementAndGet();
        
        // Start time for measuring response time
        long startTime = System.currentTimeMillis();
        
        String clientIp = clientSocket.getInetAddress().getHostAddress();
        Logger.info("Client connected (IP: " + clientIp + "). Active clients: " + currentClients);
        
        String method = "UNKNOWN";
        String resource = "UNKNOWN";
        int statusCode = 500;

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true)
        ) {
            // Read the first line of the HTTP request (e.g., "GET /index.html HTTP/1.1")
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            // Parse request line
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length >= 3) {
                method = requestParts[0];
                resource = requestParts[1];
                
                // Only support GET method for this simple server
                if (method.equals("GET")) {
                    statusCode = handleGetRequest(resource, out, startTime);
                } else {
                    statusCode = 501; // Not Implemented
                    sendErrorResponse(writer, 501, "Not Implemented");
                }
            } else {
                statusCode = 400; // Bad Request
                sendErrorResponse(writer, 400, "Bad Request");
            }
            
        } catch (IOException e) {
            Logger.error("Error handling client " + clientIp + ": " + e.getMessage());
        } finally {
            try {
                // Ensure socket is always closed
                clientSocket.close();
            } catch (IOException e) {
                Logger.error("Error closing socket: " + e.getMessage());
            }
            // End time and log the request
            long endTime = System.currentTimeMillis();
            long durationMs = endTime - startTime;
            Logger.logRequest(clientIp, method, resource, durationMs, statusCode);
            
            // Decrement active clients
            int remainingClients = Server.activeClients.decrementAndGet();
            Logger.info("Client disconnected (IP: " + clientIp + "). Active clients: " + remainingClients);
        }
    }

    /**
     * Handles HTTP GET requests by serving requested files from the DOCUMENT_ROOT folder.
     */
    private int handleGetRequest(String resource, OutputStream out, long startTime) throws IOException {
        PrintWriter writer = new PrintWriter(out, true);
        
        // Default to index.html if the root is requested
        if (resource.equals("/")) {
            resource = "/index.html";
        }

        // Extremely basic security measure to prevent directory traversal
        resource = resource.replace("..", "");

        // Determine the file path
        Path filePath = Paths.get(Server.DOCUMENT_ROOT, resource);

        // Check if file exists and is not a directory
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            // Read file content completely
            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = getContentType(resource);
            long timeTakenSoFar = System.currentTimeMillis() - startTime;
            
            // Build and send HTTP 200 OK response header
            writer.print("HTTP/1.1 200 OK\r\n");
            writer.print("Server: SimpleJavaHTTPServer\r\n");
            writer.print("Date: " + new Date() + "\r\n");
            writer.print("Content-type: " + contentType + "\r\n");
            writer.print("Content-length: " + fileContent.length + "\r\n");
            writer.print("X-Response-Time: " + timeTakenSoFar + "ms\r\n");
            writer.print("\r\n"); // Empty line separating headers and body
            writer.flush(); // Flush the headers before writing the binary body
            
            // Write file body
            out.write(fileContent);
            out.flush();
            return 200;
        } else {
            // File not found -> Return 404
            sendErrorResponse(writer, 404, "Not Found");
            return 404;
        }
    }

    /**
     * Sends an HTTP error response containing a default HTML body.
     */
    private void sendErrorResponse(PrintWriter writer, int statusCode, String statusMessage) {
        String htmlBody;
        if (statusCode == 404) {
            htmlBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Premium Service Required</title>" +
                       "<style>" +
                       "@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap');" +
                       "body{font-family:'Inter',sans-serif;background:#0f172a;color:#fff;display:flex;justify-content:center;align-items:center;height:100vh;margin:0; text-align:center;}" +
                       ".box{background:linear-gradient(145deg, #1e293b, #0f172a);padding:50px;border-radius:20px;box-shadow:0 10px 30px rgba(0,0,0,0.5);border:1px solid #334155;max-width:500px;}" +
                       "h1{color:#a855f7;font-size:2.5em;margin-bottom:10px;}" +
                       "p{color:#94a3b8;line-height:1.6;margin-bottom:30px;}" +
                       ".vip-badge{background:rgba(168,85,247,0.2);color:#c084fc;padding:8px 16px;border-radius:30px;font-weight:bold;margin-bottom:20px;display:inline-block; border: 1px solid rgba(168,85,247,0.5);}" +
                       "a{display:inline-block;background:#a855f7;color:#fff;text-decoration:none;padding:12px 25px;border-radius:8px;font-weight:bold;transition:0.3s;}" +
                       "a:hover{background:#9333ea;transform:translateY(-2px);}" +
                       "</style></head><body>" +
                       "<div class=\"box\"><div class=\"vip-badge\">💎 ERROR 404: MEMBERS ONLY</div>" +
                       "<h1>Premium Service</h1>" +
                       "<p>The content you are trying to access is reserved for Premium tier members. Upgrade your plan to unlock this advanced route.</p>" +
                       "<a href=\"/\">Return to Free Dashboard</a></div></body></html>";
        } else {
            htmlBody = "<html><head><title>" + statusCode + " " + statusMessage + "</title></head>" +
                       "<body><h1>" + statusCode + " " + statusMessage + "</h1>" +
                       "<p>The requested URL was not found on this server.</p></body></html>";
        }
        
        writer.print("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n");
        writer.print("Server: SimpleJavaHTTPServer\r\n");
        writer.print("Date: " + new Date() + "\r\n");
        writer.print("Content-type: text/html\r\n");
        writer.print("Content-length: " + htmlBody.length() + "\r\n");
        writer.print("\r\n");
        writer.print(htmlBody);
        writer.flush();
    }

    /**
     * Identifies Content-Type depending on the file extension.
     */
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) return "text/html";
        if (fileRequested.endsWith(".css")) return "text/css";
        if (fileRequested.endsWith(".js")) return "application/javascript";
        if (fileRequested.endsWith(".png")) return "image/png";
        if (fileRequested.endsWith(".jpg") || fileRequested.endsWith(".jpeg")) return "image/jpeg";
        if (fileRequested.endsWith(".txt")) return "text/plain";
            
        return "application/octet-stream"; // Default for unknown types
    }
}
