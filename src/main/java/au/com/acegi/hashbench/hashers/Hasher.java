package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;

/**
 * Adapter interface for benchmarking implementation-specific hash algorithms.
 */
public interface Hasher {
  long hash(byte[] in, int off, int len);

  long hash(ByteBuffer bb, int off, int len);
}
