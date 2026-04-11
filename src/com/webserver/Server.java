package com.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    // Port to listen on
    private static final int PORT = 8080;

    // Using a ThreadPoolExecutor with a fixed number of threads
    private static final int THREAD_POOL_SIZE = 10;

    // Directory from where static files will be served
    public static final String DOCUMENT_ROOT = "./www";

    // Track active clients
    public static final AtomicInteger activeClients = new AtomicInteger(0);

    public static void main(String[] args) {
        // Create a Thread Pool to handle multiple client requests concurrently
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Logger.info("Server started. Listening on port " + PORT);
            Logger.info("Serving static files from " + DOCUMENT_ROOT + " directory");

            while (true) {
                // Wait for a client to connect (blocking call)
                Socket clientSocket = serverSocket.accept();

                // Once connected, delegate the client socket to a ClientHandler running in a
                // new thread
                threadPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            Logger.error("Server exception: " + e.getMessage());
        } finally {
            // Shutdown the thread pool gracefully when the server stops
            threadPool.shutdown();
        }
    }
}
