package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.digests.Blake2sDigest;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.SHA512tDigest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.digests.SkeinDigest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;

import com.google.common.primitives.Longs;

public class BouncyCastleHasher implements Hasher {
  public static final String BLAKE2B_256 = "blake2b-256";
  public static final String BLAKE2S_256 = "blake2s-256";
  public static final String GOST = "gost-bc";
  public static final String MD2 = "md2-bc";
  public static final String MD4 = "md4-bc";
  public static final String MD5 = "md5-bc";
  public static final String RIPEMD128 = "ripemd128-bc";
  public static final String RIPEMD160 = "ripemd160-bc";
  public static final String RIPEMD256 = "ripemd256-bc";
  public static final String RIPEMD320 = "ripemd320-bc";
  public static final String SHA1 = "sha1-bc";
  public static final String SHA224 = "sha224-bc";
  public static final String SHA256 = "sha256-bc";
  public static final String SHA3 = "sha3-bc";
  public static final String SHA384 = "sha384-bc";
  public static final String SHA512 = "sha512-bc";
  public static final String SHA512_T = "sha512-bc-t";
  public static final String SKEIN1024 = "skein1024-bc";
  public static final String SKEIN256 = "skein256-bc";
  public static final String SKEIN512 = "skein512-bc";
  public static final String SM3 = "sm3-bc";
  public static final String TIGER = "tiger-bc";
  public static final String WHIRLPOOL2 = "whirlpool2-bc";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(BouncyCastleHasher.BLAKE2B_256,
            new BouncyCastleHasher(new Blake2bDigest(256)));
    hashers.put(BouncyCastleHasher.BLAKE2S_256,
            new BouncyCastleHasher(new Blake2sDigest(256)));
    hashers.put(BouncyCastleHasher.GOST,
            new BouncyCastleHasher(new GOST3411Digest()));
    hashers.put(BouncyCastleHasher.MD2,
            new BouncyCastleHasher(new MD2Digest()));
    hashers.put(BouncyCastleHasher.MD4,
            new BouncyCastleHasher(new MD4Digest()));
    hashers.put(BouncyCastleHasher.MD5,
            new BouncyCastleHasher(new MD5Digest()));
    hashers.put(BouncyCastleHasher.RIPEMD128,
            new BouncyCastleHasher(new RIPEMD128Digest()));
    hashers.put(BouncyCastleHasher.RIPEMD160,
            new BouncyCastleHasher(new RIPEMD160Digest()));
    hashers.put(BouncyCastleHasher.RIPEMD256,
            new BouncyCastleHasher(new RIPEMD256Digest()));
    hashers.put(BouncyCastleHasher.RIPEMD320,
            new BouncyCastleHasher(new RIPEMD320Digest()));
    hashers.put(BouncyCastleHasher.SHA1,
            new BouncyCastleHasher(new SHA1Digest()));
    hashers.put(BouncyCastleHasher.SHA224,
            new BouncyCastleHasher(new SHA224Digest()));
    hashers.put(BouncyCastleHasher.SHA256,
            new BouncyCastleHasher(new SHA256Digest()));
    hashers.put(BouncyCastleHasher.SHA3,
            new BouncyCastleHasher(new SHA3Digest()));
    hashers.put(BouncyCastleHasher.SHA384,
            new BouncyCastleHasher(new SHA384Digest()));
    hashers.put(BouncyCastleHasher.SHA512,
            new BouncyCastleHasher(new SHA512Digest()));
    hashers.put(BouncyCastleHasher.SHA512_T,
            new BouncyCastleHasher(new SHA512tDigest(7 * 8)));
    hashers.put(BouncyCastleHasher.SKEIN1024, new BouncyCastleHasher(
            new SkeinDigest(SkeinDigest.SKEIN_1024, Long.BYTES * 8)));
    hashers.put(BouncyCastleHasher.SKEIN256, new BouncyCastleHasher(
            new SkeinDigest(SkeinDigest.SKEIN_256, Long.BYTES * 8)));
    hashers.put(BouncyCastleHasher.SKEIN512, new BouncyCastleHasher(
            new SkeinDigest(SkeinDigest.SKEIN_512, Long.BYTES * 8)));
    hashers.put(BouncyCastleHasher.SM3,
            new BouncyCastleHasher(new SM3Digest()));
    hashers.put(BouncyCastleHasher.TIGER,
            new BouncyCastleHasher(new TigerDigest()));
    hashers.put(BouncyCastleHasher.WHIRLPOOL2,
            new BouncyCastleHasher(new WhirlpoolDigest()));
  }

  private final Digest delegate;
  private final byte[] digest;

  private BouncyCastleHasher(final Digest delegate) {
    this.delegate = delegate;
    this.digest = new byte[(this.delegate.getDigestSize() < Long.BYTES
            ? Long.BYTES : this.delegate.getDigestSize())];
  }

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    this.delegate.reset();
    this.delegate.update(in, off, len);
    this.delegate.doFinal(this.digest, 0);
    return Longs.fromByteArray(this.digest);
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    this.delegate.reset();

    if (bb.hasArray()) {
      this.delegate.update(bb.array(), off, len);
      this.delegate.doFinal(this.digest, 0);
      return Longs.fromByteArray(this.digest);
    }

    final ByteBuffer view = bb.duplicate();
    view.position(off);
    view.limit(off + len);
    final byte[] data = new byte[len];
    view.get(data, 0, len);

    this.delegate.update(data, 0, len);
    this.delegate.doFinal(this.digest, 0);
    return Longs.fromByteArray(this.digest);
  }
}
