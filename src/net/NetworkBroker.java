package net;

import gui.MenuController;

import java.util.LinkedList;
import java.util.Queue;

public class NetworkBroker {

    private final Queue<String> outputBuffer = new LinkedList<>();
    private final Queue<String> inputBuffer = new LinkedList<>();

    // Ha érkezik start, értesítjük a controllert
    private MenuController menuController;

    public NetworkBroker(MenuController menuController) {
        this.menuController = menuController;
    }

    /**
     * Hálózatra küldendő üzenetek
     * @param message üzenet
     */
    public void sendToSocket(String message) {
        synchronized (outputBuffer) {
            outputBuffer.add(message);
        }
    }

    /**
     * Hálózaton kapott üzenetek betevése
     * @param message üzenet
     */
    public void addReceivedMessage(String message) {
        synchronized (inputBuffer) {
            inputBuffer.add(message);
        }

        if (menuController != null) {
            if (message.startsWith("serverstart;")) {
                // Az üzenetet továbbítjuk a controllernek, nem vesszük ki a FIFO-ból, mert még kell nekünk a meghívás elfogadásánál
                menuController.serverInvited(message);
                menuController = null;
            } else if (message.startsWith("clientstart")) {
                // Controllert értesítjük hogy kliens csatlakozott hozzá
                menuController.clientConnected(this);
                menuController = null;
            }

        }

    }

    /**
     * Fogadott üzenet lekérdezése
     * @return üzenet
     */
    public String getReceivedMessage() {
        synchronized (inputBuffer) {
            return inputBuffer.poll();
        }
    }

    /**
     * Hálózatra küldendő üzenet lekérése
     * @return
     */
    public String getPendingMessageToSend() {
        synchronized (outputBuffer) {
            return outputBuffer.poll();
        }
    }

    public void resetInput() {
        synchronized (inputBuffer) {
            inputBuffer.clear();
            inputBuffer.add("stop;");
        }
    }
}
