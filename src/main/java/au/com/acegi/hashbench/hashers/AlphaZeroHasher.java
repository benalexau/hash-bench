package au.com.acegi.hashbench.hashers;

import com.google.common.primitives.Longs;
import ove.crypto.digest.Blake2b;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Hasher for the BLAKE2b algorithm as implemented by https://github.com/alphazero/Blake2b
 */
public final class AlphaZeroHasher implements Hasher {

    public static final String BLAKE2B_256 = "blake2b-256-az";

    public static void register(Map<String, Hasher> hashers) {
        hashers.put(BLAKE2B_256, new AlphaZeroHasher(Blake2b.Digest.newInstance(32)));
    }

    private final Blake2b digest;
    private final byte[] hash;

    public AlphaZeroHasher(Blake2b digest) {
        this.digest = digest;
        hash = new byte[digest.state().outlen];
    }

    @Override
    public long hash(byte[] in, int off, int len) {
        digest.update(in, off, len);
        digest.digest(hash, 0, hash.length);
        return Longs.fromByteArray(hash);
    }

    @Override
    public long hash(ByteBuffer bb, int off, int len) {
        if (bb.hasArray()) {
            digest.update(bb.array(), off, len);
            digest.digest(hash, 0, hash.length);
            return Longs.fromByteArray(hash);
        }

        final ByteBuffer view = bb.duplicate();
        view.position(off);
        view.limit(off + len);
        final byte[] data = new byte[len];
        view.get(data, 0, len);

        digest.update(data, 0, len);
        digest.digest(hash, 0, hash.length);
        return Longs.fromByteArray(hash);
    }
}
