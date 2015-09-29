package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;

import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

public class Jp32Hasher implements Hasher {
  public static final String XXH32_JNI = "xxh32-jpountz-jni";
  public static final String XXH32_SAFE = "xxh32-jpountz-safe";
  public static final String XXH32_UNSAFE = "xxh32-jpountz-unsafe";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(Jp32Hasher.XXH32_JNI,
            new Jp32Hasher(XXHashFactory.nativeInstance().hash32()));
    hashers.put(Jp32Hasher.XXH32_UNSAFE,
            new Jp32Hasher(XXHashFactory.unsafeInstance().hash32()));
    hashers.put(Jp32Hasher.XXH32_SAFE,
            new Jp32Hasher(XXHashFactory.safeInstance().hash32()));
  }

  private final XXHash32 delegate;

  private Jp32Hasher(final XXHash32 delegate) {
    this.delegate = delegate;
  }

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    return this.delegate.hash(in, off, len, 0);
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    return this.delegate.hash(bb, off, len, 0);
  }
}
