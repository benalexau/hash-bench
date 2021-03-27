package au.com.acegi.hashbench.hashers;

import com.google.common.primitives.Longs;
import org.kocakosm.jblake2.Blake2b;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Hasher for the BLAKE2b algorithm as implemented by https://github.com/kocakosm/jblake2
 */
public final class KocakosmHasher implements Hasher {

    public static final String BLAKE2B_256 = "blake2b-256-kc";

    public static void register(Map<String, Hasher> hashers) {
        hashers.put(BLAKE2B_256, new KocakosmHasher(new Blake2b(32)));
    }

    private final Blake2b digest;

    public KocakosmHasher(Blake2b digest) {
        this.digest = digest;
    }

    @Override
    public long hash(byte[] in, int off, int len) {
        byte[] hash = digest.digest(in, off, len);
        return Longs.fromByteArray(hash);
    }

    @Override
    public long hash(ByteBuffer bb, int off, int len) {
        if (bb.hasArray()) {
            byte[] hash = digest.digest(bb.array(), off, len);
            return Longs.fromByteArray(hash);
        }

        final ByteBuffer view = bb.duplicate();
        view.position(off);
        view.limit(off + len);
        final byte[] data = new byte[len];
        view.get(data, 0, len);

        byte[] hash = digest.digest(data, 0, len);
        return Longs.fromByteArray(hash);
    }
}
