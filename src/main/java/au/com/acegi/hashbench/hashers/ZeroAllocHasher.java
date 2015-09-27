package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;

import net.openhft.hashing.LongHashFunction;

public class ZeroAllocHasher implements Hasher {
  public static final String CITY_1_1 = "city64-zah";
  public static final String FARM_NA = "farmna-zah";
  public static final String FARM_UO = "farmuo-zah";
  public static final String MURMUR_3 = "murmur3h128-zah";
  public static final String XXH64 = "xxh64-zah";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(ZeroAllocHasher.CITY_1_1,
            new ZeroAllocHasher(LongHashFunction.city_1_1()));
    hashers.put(ZeroAllocHasher.FARM_NA,
            new ZeroAllocHasher(LongHashFunction.farmNa()));
    hashers.put(ZeroAllocHasher.FARM_UO,
            new ZeroAllocHasher(LongHashFunction.farmUo()));
    hashers.put(ZeroAllocHasher.MURMUR_3,
            new ZeroAllocHasher(LongHashFunction.murmur_3()));
    hashers.put(ZeroAllocHasher.XXH64,
            new ZeroAllocHasher(LongHashFunction.xx_r39()));
  }

  private final LongHashFunction delegate;

  private ZeroAllocHasher(final LongHashFunction delegate) {
    this.delegate = delegate;
  }

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    return this.delegate.hashBytes(in, off, len);
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    return this.delegate.hashBytes(bb, off, len);
  }
}
