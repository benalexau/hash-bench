package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class JacksumHasher implements Hasher {
  public static final String ADLER32_ALT = "adler32-jacksum-alt"; //
  public static final String ADLER32_DELEGATING = "adler32-jacksum-delegating";
  public static final String CKSUM = "cksum-jacksum"; // unix
  public static final String CRC16 = "crc16-jacksum"; // LHA/ARC
  public static final String CRC24 = "crc24-jacksum"; // Open PGP
  public static final String CRC32_BZIP2 = "crc32-jacksum-bzip2";
  public static final String CRC32_DELEGATING = "crc32-jacksum-delegating";
  public static final String CRC32_FCS_32 = "crc32-jacksum-fcs32"; // FCS-32
  public static final String CRC32_MPEG2 = "crc32-jacksum-mpeg2"; // MPEG-2
  public static final String CRC64 = "crc64-jacksum"; // ISO 3309
  public static final String CRC8 = "crc8-jacksum"; // FLAC
  public static final String ED2K = "ed2k-jacksum"; // eDonkey
  public static final String ELF = "elf-jacksum";
  public static final String FCS16 = "fcs16-jacksum";
  public static final String GOST = "gost-jacksum"; // GOST (R 34.11-94)
  public static final String HAS160 = "has160-jacksum";
  public static final String HAVAL128H3 = "haval128h3-jacksum"; // 3 rounds
  public static final String HAVAL128H4 = "haval128h4-jacksum"; // 4 rounds
  public static final String HAVAL128H5 = "haval128h5-jacksum"; // 5 rounds
  public static final String HAVAL160H3 = "haval160h3-jacksum"; // 3 rounds
  public static final String HAVAL160H4 = "haval160h4-jacksum"; // 4 rounds
  public static final String HAVAL160H5 = "haval160h5-jacksum"; // 5 rounds
  public static final String HAVAL192H3 = "haval192h3-jacksum"; // 3 rounds
  public static final String HAVAL192H4 = "haval192h4-jacksum"; // 4 rounds
  public static final String HAVAL192H5 = "haval192h5-jacksum"; // 5 rounds
  public static final String HAVAL224H3 = "haval224h3-jacksum"; // 3 rounds
  public static final String HAVAL224H4 = "haval224h4-jacksum"; // 4 rounds
  public static final String HAVAL224H5 = "haval224h5-jacksum"; // 5 rounds
  public static final String HAVAL256H3 = "haval256h3-jacksum"; // 3 rounds
  public static final String HAVAL256H4 = "haval256h4-jacksum"; // 4 rounds
  public static final String HAVAL256H5 = "haval256h5-jacksum"; // 5 rounds
  public static final String MD2 = "md2-jacksum";
  public static final String MD4 = "md4-jacksum";
  public static final String MD5 = "md5-jacksum";
  public static final String MD5_ALT = "md5-jacksum-alt";
  public static final String RIPEMD128 = "ripemd128-jacksum";
  public static final String RIPEMD160 = "ripemd160-jacksum";
  public static final String RIPEMD256 = "ripemd256-jacksum";
  public static final String RIPEMD320 = "ripemd320-jacksum";
  public static final String SHA0 = "sha0-jacksum";
  public static final String SHA1 = "sha0-jacksum";
  public static final String SHA1_ALT = "sha0-jacksum-alt";
  public static final String SHA224 = "sha224-jacksum";
  public static final String SHA256 = "sha256-jacksum";
  public static final String SHA256_ALT = "sha256-jacksum-alt";
  public static final String SHA384 = "sha384-jacksum";
  public static final String SHA384_ALT = "sha384-jacksum-alt";
  public static final String SHA512 = "sha512-jacksum";
  public static final String SHA512_ALT = "sha512-jacksum-alt";
  public static final String SUM16 = "sum16-jacksum";
  public static final String SUM24 = "sum24-jacksum";
  public static final String SUM32 = "sum32-jacksum";
  public static final String SUM8 = "sum8-jacksum";
  public static final String SUMBSD = "sumbsd-jacksum"; // BSD Unix
  public static final String SUMSYSV = "sumsysv-jacksum"; // System V Unix
  public static final String TIGER = "tiger-jacksum";
  public static final String TIGER_TREE = "tiger-jacksum-tree";
  public static final String TIGER128 = "tiger128-jacksum";
  public static final String TIGER160 = "tiger160-jacksum";
  public static final String TIGER2 = "tiger2-jacksum";
  public static final String TIGER2_TREE = "tiger2-jacksum-tree";
  public static final String WHIRLPOOL0 = "whirlpool0-jacksum";
  public static final String WHIRLPOOL1 = "whirlpool1-jacksum";
  public static final String WHIRLPOOL2 = "whirlpool2-jacksum";
  public static final String XOR8 = "xor8-jacksum"; // xor 8

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(JacksumHasher.ADLER32_ALT, new JacksumHasher("adler32", true));
    hashers.put(JacksumHasher.ADLER32_DELEGATING, new JacksumHasher("adler32"));
    hashers.put(JacksumHasher.CKSUM, new JacksumHasher("cksum"));
    hashers.put(JacksumHasher.CRC16, new JacksumHasher("crc16"));
    hashers.put(JacksumHasher.CRC24, new JacksumHasher("crc24"));
    hashers.put(JacksumHasher.CRC32_BZIP2, new JacksumHasher("crc32_bzip2"));
    hashers.put(JacksumHasher.CRC32_DELEGATING, new JacksumHasher("crc32"));
    hashers.put(JacksumHasher.CRC32_FCS_32, new JacksumHasher("fcs32", true));
    hashers.put(JacksumHasher.CRC32_MPEG2, new JacksumHasher("crc32_mpeg2"));
    hashers.put(JacksumHasher.CRC64, new JacksumHasher("crc64"));
    hashers.put(JacksumHasher.CRC8, new JacksumHasher("crc8"));
    hashers.put(JacksumHasher.ED2K, new JacksumHasher("ed2k"));
    hashers.put(JacksumHasher.ELF, new JacksumHasher("elf"));
    hashers.put(JacksumHasher.FCS16, new JacksumHasher("fcs16"));
    hashers.put(JacksumHasher.GOST, new JacksumHasher("gost"));
    hashers.put(JacksumHasher.HAS160, new JacksumHasher("has160"));
    hashers.put(JacksumHasher.HAVAL128H3, new JacksumHasher("haval_128_3"));
    hashers.put(JacksumHasher.HAVAL128H4, new JacksumHasher("haval_128_4"));
    hashers.put(JacksumHasher.HAVAL128H5, new JacksumHasher("haval_128_5"));
    hashers.put(JacksumHasher.HAVAL160H3, new JacksumHasher("haval_160_3"));
    hashers.put(JacksumHasher.HAVAL160H4, new JacksumHasher("haval_160_4"));
    hashers.put(JacksumHasher.HAVAL160H5, new JacksumHasher("haval_160_5"));
    hashers.put(JacksumHasher.HAVAL192H3, new JacksumHasher("haval_192_3"));
    hashers.put(JacksumHasher.HAVAL192H4, new JacksumHasher("haval_192_4"));
    hashers.put(JacksumHasher.HAVAL192H5, new JacksumHasher("haval_192_5"));
    hashers.put(JacksumHasher.HAVAL224H3, new JacksumHasher("haval_224_3"));
    hashers.put(JacksumHasher.HAVAL224H4, new JacksumHasher("haval_224_4"));
    hashers.put(JacksumHasher.HAVAL224H5, new JacksumHasher("haval_224_5"));
    hashers.put(JacksumHasher.HAVAL256H3, new JacksumHasher("haval_256_3"));
    hashers.put(JacksumHasher.HAVAL256H4, new JacksumHasher("haval_256_4"));
    hashers.put(JacksumHasher.HAVAL256H5, new JacksumHasher("haval_256_5"));
    hashers.put(JacksumHasher.MD2, new JacksumHasher("md5"));
    hashers.put(JacksumHasher.MD4, new JacksumHasher("md5"));
    hashers.put(JacksumHasher.MD5, new JacksumHasher("md5"));
    hashers.put(JacksumHasher.MD5_ALT, new JacksumHasher("md5", true));
    hashers.put(JacksumHasher.RIPEMD128, new JacksumHasher("ripemd128"));
    hashers.put(JacksumHasher.RIPEMD160, new JacksumHasher("ripemd160"));
    hashers.put(JacksumHasher.RIPEMD256, new JacksumHasher("ripemd256"));
    hashers.put(JacksumHasher.RIPEMD320, new JacksumHasher("ripemd320"));
    hashers.put(JacksumHasher.SHA0, new JacksumHasher("sha0"));
    hashers.put(JacksumHasher.SHA1, new JacksumHasher("sha1"));
    hashers.put(JacksumHasher.SHA1_ALT, new JacksumHasher("sha1", true));
    hashers.put(JacksumHasher.SHA224, new JacksumHasher("sha224"));
    hashers.put(JacksumHasher.SHA256, new JacksumHasher("sha256"));
    hashers.put(JacksumHasher.SHA256_ALT, new JacksumHasher("sha256", true));
    hashers.put(JacksumHasher.SHA384, new JacksumHasher("sha384"));
    hashers.put(JacksumHasher.SHA384_ALT, new JacksumHasher("sha384", true));
    hashers.put(JacksumHasher.SHA512, new JacksumHasher("sha512"));
    hashers.put(JacksumHasher.SHA512_ALT, new JacksumHasher("sha512", true));
    hashers.put(JacksumHasher.SUM8, new JacksumHasher("sum8"));
    hashers.put(JacksumHasher.SUM16, new JacksumHasher("sum16"));
    hashers.put(JacksumHasher.SUM24, new JacksumHasher("sum24"));
    hashers.put(JacksumHasher.SUM32, new JacksumHasher("sum32"));
    hashers.put(JacksumHasher.SUMBSD, new JacksumHasher("sumbsd"));
    hashers.put(JacksumHasher.SUMSYSV, new JacksumHasher("sumsysv"));
    hashers.put(JacksumHasher.TIGER, new JacksumHasher("tiger"));
    hashers.put(JacksumHasher.TIGER_TREE, new JacksumHasher("tree:tiger"));
    hashers.put(JacksumHasher.TIGER128, new JacksumHasher("tiger128"));
    hashers.put(JacksumHasher.TIGER160, new JacksumHasher("tiger160"));
    hashers.put(JacksumHasher.TIGER2, new JacksumHasher("tiger2"));
    hashers.put(JacksumHasher.TIGER2_TREE, new JacksumHasher("tree:tiger2"));
    hashers.put(JacksumHasher.WHIRLPOOL0, new JacksumHasher("whirlpool0"));
    hashers.put(JacksumHasher.WHIRLPOOL1, new JacksumHasher("whirlpool1"));
    hashers.put(JacksumHasher.WHIRLPOOL2, new JacksumHasher("whirlpool2"));
    hashers.put(JacksumHasher.XOR8, new JacksumHasher("xor8"));
  }

  private final AbstractChecksum delegate;

  private JacksumHasher(final String algorithm) {
    this(algorithm, false);
  }

  private JacksumHasher(final String algorithm, final boolean alternate) {
    try {
      this.delegate = JacksumAPI.getChecksumInstance(algorithm, alternate);
    } catch (final NoSuchAlgorithmException nsae) {
      throw new IllegalStateException(nsae);
    }
  }

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    this.delegate.reset();
    this.delegate.update(in, off, len);
    return this.delegate.getValue();
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    this.delegate.reset();
    if (bb.hasArray()) {
      this.delegate.update(bb.array(), off, len);
      return this.delegate.getValue();
    }

    final ByteBuffer view = bb.duplicate();
    view.position(off);
    view.limit(off + len);
    final byte[] data = new byte[len];
    view.get(data, 0, len);

    this.delegate.update(data);
    return this.delegate.getValue();
  }
}
