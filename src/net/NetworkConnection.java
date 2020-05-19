package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkConnection implements Runnable {

    private NetworkBroker networkBroker;
    private boolean isServer;
    private String hostIp;

    /**
     * Konstruktor a szerveroldalnak
     *
     * @param networkBroker bróker objektum üzenetküldéshez
     */
    public NetworkConnection(NetworkBroker networkBroker) {
        this.networkBroker = networkBroker;
        this.isServer = true;
    }

    /**
     * Konstruktor a kilens oldalnak
     *
     * @param networkBroker bróker objektum üzenetküldéshez
     * @param hostIp        szerver IP-je
     */
    public NetworkConnection(NetworkBroker networkBroker, String hostIp) {
        this.networkBroker = networkBroker;
        this.isServer = false;
        this.hostIp = hostIp;
    }

    @Override
    public void run() {
        if (isServer) {
            try (ServerSocket serverSocket = new ServerSocket(NetUtils.PORT)) {
                serverSocket.setSoTimeout(10 * 1000);
                Socket socket = serverSocket.accept();
                startInputOutput(socket);

            } catch (IOException e) {
                networkBroker.resetInput();
            }
        } else {
            try (Socket socket = new Socket(hostIp, NetUtils.PORT)) {
                startInputOutput(socket);
            } catch (IOException ignored) {
            } finally {
                networkBroker.resetInput();
            }
        }
    }

    private void startInputOutput(Socket socket) throws IOException {
        try (BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
                // Ha van üzenet elküldjük
                String message = networkBroker.getPendingMessageToSend();
                if (message != null) {
                    System.out.println("Sending message: " + message);
                    outputStream.write(message);
                    outputStream.newLine();
                    outputStream.flush();
                    if (message.equals("stop;"))
                        break;
                }

                // Ha lehetséges olvasunk
                String line;
                if (inputStream.ready() && (line = inputStream.readLine()) != null) {
                    System.out.println("Received message: " + line);
                    networkBroker.addReceivedMessage(line);
                    if (line.equals("stop;"))
                        break;
                }
            }

        }

    }

}
