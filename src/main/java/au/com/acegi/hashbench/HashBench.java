package au.com.acegi.hashbench;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import au.com.acegi.hashbench.hashers.AdlerHasher;
import au.com.acegi.hashbench.hashers.CRC32Hasher;
import au.com.acegi.hashbench.hashers.GuavaHasher;
import au.com.acegi.hashbench.hashers.Hasher;
import au.com.acegi.hashbench.hashers.JacksumHasher;
import au.com.acegi.hashbench.hashers.Jp32Hasher;
import au.com.acegi.hashbench.hashers.Jp64Hasher;
import au.com.acegi.hashbench.hashers.SipFwdHasher;
import au.com.acegi.hashbench.hashers.SipInlineHasher;
import au.com.acegi.hashbench.hashers.ZeroAllocHasher;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
public class HashBench {
  private static final int BUFFER_SIZE = 65536;
  private static final Map<String, Hasher> HASHERS = new HashMap<String, Hasher>();
  private static final int OFFSET;
  private static final Random RND = new SecureRandom();

  static {
    OFFSET = HashBench.RND.nextInt(32000);
    AdlerHasher.register(HashBench.HASHERS);
    CRC32Hasher.register(HashBench.HASHERS);
    GuavaHasher.register(HashBench.HASHERS);
    JacksumHasher.register(HashBench.HASHERS);
    Jp32Hasher.register(HashBench.HASHERS);
    Jp64Hasher.register(HashBench.HASHERS);
    SipFwdHasher.register(HashBench.HASHERS);
    SipInlineHasher.register(HashBench.HASHERS);
    ZeroAllocHasher.register(HashBench.HASHERS);
    for (final String key : HashBench.HASHERS.keySet()) {
      if (key.contains("_")) {
        throw new IllegalStateException(key);
      }
    }
  }

  @Param({
          AdlerHasher.ADLER_32,
          CRC32Hasher.CRC_32,
          GuavaHasher.ADLER32,
          GuavaHasher.CRC32_DELEGATING,
          GuavaHasher.CRC32_RFC,
          GuavaHasher.GOOD_FAST_HASH_32,
          GuavaHasher.GOOD_FAST_HASH_64,
          GuavaHasher.MD5,
          GuavaHasher.MURMUR_3_32,
          GuavaHasher.MURMUR_3_128,
          GuavaHasher.SHA1,
          GuavaHasher.SHA256,
          GuavaHasher.SHA384,
          GuavaHasher.SHA512,
          GuavaHasher.SIP,
          JacksumHasher.ADLER32_ALT,
          JacksumHasher.ADLER32_DELEGATING,
          JacksumHasher.CKSUM,
          JacksumHasher.CRC16,
          JacksumHasher.CRC24,
          JacksumHasher.CRC32_BZIP2,
          JacksumHasher.CRC32_DELEGATING,
          JacksumHasher.CRC32_FCS_32,
          JacksumHasher.CRC32_MPEG2,
          JacksumHasher.CRC64,
          JacksumHasher.CRC8,
          JacksumHasher.ED2K,
          JacksumHasher.ELF,
          JacksumHasher.FCS16,
          JacksumHasher.GOST,
          JacksumHasher.HAS160,
          JacksumHasher.HAVAL128H3,
          JacksumHasher.HAVAL128H4,
          JacksumHasher.HAVAL128H5,
          JacksumHasher.HAVAL160H3,
          JacksumHasher.HAVAL160H4,
          JacksumHasher.HAVAL160H5,
          JacksumHasher.HAVAL192H3,
          JacksumHasher.HAVAL192H4,
          JacksumHasher.HAVAL192H5,
          JacksumHasher.HAVAL224H3,
          JacksumHasher.HAVAL224H4,
          JacksumHasher.HAVAL224H5,
          JacksumHasher.HAVAL256H3,
          JacksumHasher.HAVAL256H4,
          JacksumHasher.HAVAL256H5,
          JacksumHasher.MD2,
          JacksumHasher.MD4,
          JacksumHasher.MD5,
          JacksumHasher.MD5_ALT,
          JacksumHasher.RIPEMD128,
          JacksumHasher.RIPEMD160,
          JacksumHasher.RIPEMD256,
          JacksumHasher.RIPEMD320,
          JacksumHasher.SHA0,
          JacksumHasher.SHA1,
          JacksumHasher.SHA1_ALT,
          JacksumHasher.SHA224,
          JacksumHasher.SHA256,
          JacksumHasher.SHA256_ALT,
          JacksumHasher.SHA384,
          JacksumHasher.SHA384_ALT,
          JacksumHasher.SHA512,
          JacksumHasher.SHA512_ALT,
          JacksumHasher.SUM16,
          JacksumHasher.SUM24,
          JacksumHasher.SUM32,
          JacksumHasher.SUM8,
          JacksumHasher.SUMBSD,
          JacksumHasher.SUMSYSV,
          JacksumHasher.TIGER,
          JacksumHasher.TIGER_TREE,
          JacksumHasher.TIGER128,
          JacksumHasher.TIGER160,
          JacksumHasher.TIGER2,
          JacksumHasher.TIGER2_TREE,
          JacksumHasher.WHIRLPOOL0,
          JacksumHasher.WHIRLPOOL1,
          JacksumHasher.WHIRLPOOL2,
          JacksumHasher.XOR8,
          Jp32Hasher.XXH32_JNI,
          Jp32Hasher.XXH32_SAFE,
          Jp32Hasher.XXH32_UNSAFE,
          Jp64Hasher.XXH64_JNI,
          Jp64Hasher.XXH64_SAFE,
          Jp64Hasher.XXH64_UNSAFE,
          SipFwdHasher.SIP,
          SipInlineHasher.SIP,
          ZeroAllocHasher.CITY_1_1,
          ZeroAllocHasher.FARM_NA,
          ZeroAllocHasher.FARM_UO,
          ZeroAllocHasher.XXH64,
          ZeroAllocHasher.MURMUR_3
  })
  private String algo;

  private byte[] byteArray;
  private ByteBuffer byteBufferArray;
  private ByteBuffer byteBufferDirect;

  private Hasher hasher;

  @Param({"32", "128", "512", "2048", "8192", "16384", "32768"})
  private int length;

  private void equals(final long expected, final long received) {
    if (expected != received) {
      throw new IllegalStateException(
              "Received " + received + " (expected " + received + ")");
    }
  }

  @Setup(Level.Iteration)
  public void setup() {
    if ((this.length + HashBench.OFFSET) > HashBench.BUFFER_SIZE) {
      throw new IllegalStateException("Buffer len " + HashBench.BUFFER_SIZE
              + " too small for offset " + HashBench.OFFSET + " with length "
              + this.length);
    }

    this.hasher = HashBench.HASHERS.get(this.algo);
    if (this.hasher == null) {
      throw new UnsupportedOperationException(
              "Unknown hasher '" + this.algo + "'");
    }

    this.byteArray = new byte[HashBench.BUFFER_SIZE];
    HashBench.RND.nextBytes(this.byteArray);

    this.byteBufferArray = ByteBuffer.allocate(HashBench.BUFFER_SIZE);
    this.byteBufferArray.put(this.byteArray, 0, HashBench.BUFFER_SIZE);

    this.byteBufferDirect = ByteBuffer.allocateDirect(HashBench.BUFFER_SIZE);
    this.byteBufferDirect.put(this.byteArray, 0, HashBench.BUFFER_SIZE);

    // verify this hasher provides identical results for each buffer type
    final long hash = this.hasher.hash(this.byteArray, HashBench.OFFSET,
            this.length);
    this.equals(hash,
            this.hasher.hash(this.byteBufferArray, HashBench.OFFSET,
                    this.length));
    this.equals(hash,
            this.hasher.hash(this.byteBufferDirect, HashBench.OFFSET,
                    this.length));
  }

  @Benchmark
  public long withArray() {
    return this.hasher.hash(this.byteArray, HashBench.OFFSET, this.length);
  }

  @Benchmark
  public long withByteBuffer() {
    return this.hasher.hash(this.byteBufferArray, HashBench.OFFSET,
            this.length);
  }

  @Benchmark
  public long withDirectBuffer() {
    return this.hasher.hash(this.byteBufferDirect, HashBench.OFFSET,
            this.length);
  }

}
