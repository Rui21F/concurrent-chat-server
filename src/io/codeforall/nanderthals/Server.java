package io.codeforall.nanderthals;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    ArrayList<ServerWorker> arrayListServerWorkers = new ArrayList<>();
    private final int DEFAULT_PORT = 8888;
    private String message;
    private ExecutorService cachedPool;
    private Socket clientSocket;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }


    public void startServer() throws IOException {

        ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
        cachedPool = Executors.newCachedThreadPool();


        while (true) {
            try {
                System.out.println("-^/ WAITING FOR CONNECTION /^-");
                clientSocket = serverSocket.accept();
                ServerWorker serverWorker = new ServerWorker(clientSocket, this);
                arrayListServerWorkers.add(serverWorker);
                cachedPool.submit(serverWorker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String message, String sender) {
        synchronized (arrayListServerWorkers) {
            for (ServerWorker serverWorker : arrayListServerWorkers) {
                if (serverWorker.name != null && !serverWorker.name.equals(sender)) {
                    serverWorker.sendMessageToEachClient(message);
                }
            }
        }
    }


    public void printClientsServer() {
        synchronized (arrayListServerWorkers) {
            for (ServerWorker serverWorker : arrayListServerWorkers) {
                serverWorker.printClients();
            }
        }
    }


    public void sendWhisper(String targetUser, String message, String sender) {
        synchronized (arrayListServerWorkers) {
            for (ServerWorker serverWorker : arrayListServerWorkers) {
                if (serverWorker.name != null && serverWorker.name.equals(targetUser)) {
                    serverWorker.sendWhisper(serverWorker.name + " has send you a whisper: " + message, sender);
                }
            }
        }
    }

    public void clientQuit(String client) {
        synchronized (arrayListServerWorkers) {

            for (ServerWorker serverWorker : arrayListServerWorkers) {
                if (serverWorker.name != null && serverWorker.name == client && clientSocket.isBound()) {
                    serverWorker.closeClientSocket(client);
                }
            }

        }
    }


    public void messageDispatcher(String message) {

        String[] splited = message.split(" ", 3);
        String word1 = splited[0];
        String target = splited[1];
        String finalMessage = splited[2];

        switch (word1) {
            case "/list" -> printClientsServer();
            //case "/w" -> sendWhisper(target, message);
            case "/quit" -> clientQuit(word1);
        }
    }
}

