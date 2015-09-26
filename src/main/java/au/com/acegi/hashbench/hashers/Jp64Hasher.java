package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;

import net.jpountz.xxhash.XXHash64;
import net.jpountz.xxhash.XXHashFactory;

public class Jp64Hasher implements Hasher {
  public static final String JP_XXH64_JNI = "xxh64-jpountz-jni";
  public static final String JP_XXH64_SAFE = "xxh64-jpountz-safe";
  public static final String JP_XXH64_UNSAFE = "xxh64-jpountz-unsafe";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(Jp64Hasher.JP_XXH64_JNI,
            new Jp64Hasher(XXHashFactory.nativeInstance().hash64()));
    hashers.put(Jp64Hasher.JP_XXH64_UNSAFE,
            new Jp64Hasher(XXHashFactory.unsafeInstance().hash64()));
    hashers.put(Jp64Hasher.JP_XXH64_SAFE,
            new Jp64Hasher(XXHashFactory.safeInstance().hash64()));
  }

  private final XXHash64 delegate;

  private Jp64Hasher(final XXHash64 delegate) {
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
