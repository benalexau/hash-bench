package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class GuavaHasher implements Hasher {
  public static final String ADLER32 = "adler32-guava";
  public static final String CRC32_DELEGATING = "crc32-guava-delegating";
  public static final String CRC32_RFC = "crc32-guava-rfc3720";
  public static final String GOOD_FAST_HASH_32 = "gfh32-guava";
  public static final String GOOD_FAST_HASH_64 = "gfh64-guava";
  public static final String MD5 = "md5-guava";
  public static final String MURMUR_3_128 = "murmur3h128-guava";
  public static final String MURMUR_3_32 = "murmur3h32-guava";
  public static final String SHA1 = "sha1-guava";
  public static final String SHA256 = "sha256-guava";
  public static final String SHA384 = "sha384-guava";
  public static final String SHA512 = "sha512-guava";
  public static final String SIP = "sip-guava";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(GuavaHasher.CRC32_DELEGATING,
            new GuavaHasher(Hashing.crc32()));
    hashers.put(GuavaHasher.CRC32_RFC,
            new GuavaHasher(Hashing.crc32c()));
    hashers.put(GuavaHasher.ADLER32,
            new GuavaHasher(Hashing.adler32()));
    hashers.put(GuavaHasher.GOOD_FAST_HASH_32,
            new GuavaHasher(Hashing.goodFastHash(32)));
    hashers.put(GuavaHasher.GOOD_FAST_HASH_64,
            new GuavaHasher(Hashing.goodFastHash(64)));
    hashers.put(GuavaHasher.MD5, new GuavaHasher(Hashing.md5()));
    hashers.put(GuavaHasher.MURMUR_3_32,
            new GuavaHasher(Hashing.murmur3_32()));
    hashers.put(GuavaHasher.MURMUR_3_128,
            new GuavaHasher(Hashing.murmur3_128()));
    hashers.put(GuavaHasher.SHA1, new GuavaHasher(Hashing.sha1()));
    hashers.put(GuavaHasher.SHA256, new GuavaHasher(Hashing.sha256()));
    hashers.put(GuavaHasher.SHA384, new GuavaHasher(Hashing.sha384()));
    hashers.put(GuavaHasher.SHA512, new GuavaHasher(Hashing.sha512()));
    hashers.put(GuavaHasher.SIP, new GuavaHasher(Hashing.sipHash24()));
  }

  private final HashFunction delegate;

  private GuavaHasher(final HashFunction delegate) {
    this.delegate = delegate;
  }

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    return this.delegate.hashBytes(in, off, len).padToLong();
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    if (bb.hasArray()) {
      return this.delegate.hashBytes(bb.array(), off, len).padToLong();
    }

    final ByteBuffer view = bb.duplicate();
    view.position(off);
    view.limit(off + len);
    final byte[] data = new byte[len];
    view.get(data, 0, len);

    return this.delegate.hashBytes(data).padToLong();
  }
}
