package io.codeforall.nanderthals;

import java.io.*;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket clientSocket;
    private Server server;
    private BufferedReader reader;
    private BufferedWriter writer;
    public String name;

    public ServerWorker(Socket clientSocket, Server server) {

        this.clientSocket = clientSocket;
        this.server = server;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            writer.write("What's your name? ");
            writer.flush();
            name = reader.readLine();
            System.out.println(name + " CONNECTED");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);

        try {
            writer.write("WELCOME " + Thread.currentThread().getName() + " FEEL FREE TO CHAT");
            server.broadcast(Thread.currentThread().getName() + " JOINED THE CHAT", name);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message;

        try {

            while ((message = reader.readLine()) != null) {

                if (message.equals(".list")) {
                    server.printClientsServer();
                }

                if (message.startsWith(".quit")) {
                    server.clientQuit(Thread.currentThread().getName());
                }

                if (message.startsWith(".w ")) {
                    // Whisper message: ".w targetUser message"
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String targetUser = parts[1];
                        String whisperMessage = parts[2];
                        server.sendWhisper(targetUser, whisperMessage, name);

                        continue;
                    }
                }
                writer.flush();
                server.broadcast(name + " said: " + message, name);
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void sendMessageToEachClient(String message) {
        try {
            if (!clientSocket.isClosed()) {
                writer.write(message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendWhisper(String target, String message, String sender) {

        try {
            writer.write(sender + "  has send you a whisper: " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeClientSocket(String client) {
        try {
            writer.write(client + " has left the chat");
            writer.newLine();
            clientSocket.close();
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printClients() {
        try {
            writer.write("----PRINTING THE LIST OF CLIENTS IN CHAT----");
            writer.newLine();
            for (ServerWorker serverWorker : server.arrayListServerWorkers) {
                if (serverWorker.name != null) {
                    writer.write(serverWorker.name);
                    writer.newLine();
                }
            }
            writer.write("----------------LIST PRINTED----------------");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}