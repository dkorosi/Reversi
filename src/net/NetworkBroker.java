package net;

import javafx.util.Pair;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

/**
 * A hálózatra küldendő és beolvasott adatokat tárolja, konkurensen hozzáférnek a thread-ek
 * Képes push notification-re ha az üzenet elejére lett beregisztrálva event listener,
 * illetve le is lehet kérdezni a következő üzenetet manuálisan
 */
public class NetworkBroker {

    private final Queue<String> outputBuffer = new LinkedList<>();
    private final Queue<String> inputBuffer = new LinkedList<>();

    private boolean stop = false;

    private final Collection<Pair<String, Function<String, Boolean>>> eventListeners = new LinkedList<>();

    /**
     * Hálózatra küldendő üzenetek
     *
     * @param message üzenet
     */
    public void sendToSocket(String message) {
        synchronized (outputBuffer) {
            outputBuffer.add(message);
        }
    }

    /**
     * Hálózaton kapott üzenetek betevése, minden üzenetnél megvizsgálja, hogy kell-e értesítenie valakit, ha van eventlistener akkor meghívjuk a függvényét
     *
     * @param message üzenet
     */
    public void addReceivedMessage(String message) {
        boolean pushed = false;
        synchronized (eventListeners) {
            Iterator<Pair<String, Function<String, Boolean>>> iterator = eventListeners.iterator();
            while (iterator.hasNext()) {
                Pair<String, Function<String, Boolean>> eventListener = iterator.next();
                if (message.startsWith(eventListener.getKey())) {
                    Boolean toRemove = eventListener.getValue().apply(message);
                    if (toRemove)
                        iterator.remove();
                    pushed = true;
                }
            }
        }

        if (!pushed)
            synchronized (inputBuffer) {
                inputBuffer.add(message);
            }
    }

    public void registerEventListener(String prefix, Function<String, Boolean> function) {
        synchronized (eventListeners) {
            eventListeners.add(new Pair<>(prefix, function));
        }
    }

    /**
     * Fogadott üzenet lekérdezése
     *
     * @return üzenet
     */
    public String getReceivedMessage() {
        synchronized (inputBuffer) {
            return inputBuffer.poll();
        }
    }

    /**
     * Hálózatra küldendő üzenet lekérése
     *
     * @return
     */
    public String getPendingMessageToSend() {
        synchronized (outputBuffer) {
            return outputBuffer.poll();
        }
    }

    /**
     * Ha vége a kapcsolatnak, vagy valami probléma történik, mindenki értesítünk, hogy vége
     */
    public void sendStop() {
        synchronized (inputBuffer) {
            inputBuffer.clear();
            inputBuffer.add("stop;");
        }
        synchronized (outputBuffer) {
            outputBuffer.clear();
            outputBuffer.add("stop;");
        }
        stop = true;
    }

    public boolean isStopped() {
        return stop;
    }
}
