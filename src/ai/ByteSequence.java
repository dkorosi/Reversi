package ai;

import java.util.Arrays;

/**
 * Egy bájttömböt tárol, hogy kulcsként tudjon működni egy hashtable-ben
 */
public class ByteSequence {

    private byte[] bytes;

    /**
     * Inicializálja a sequence-t
     *
     * @param bytes byte-ok
     */
    public ByteSequence(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteSequence that = (ByteSequence) o;
        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
