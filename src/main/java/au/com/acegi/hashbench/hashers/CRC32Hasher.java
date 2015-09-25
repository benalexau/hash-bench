package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.zip.CRC32;

public class CRC32Hasher implements Hasher {
  public static final String JRE_CRC_32 = "crc32-jre";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(CRC32Hasher.JRE_CRC_32, new CRC32Hasher());
  }

  private final CRC32 delegate = new CRC32();

  private CRC32Hasher() {}

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
    this.delegate.update(view);
    return this.delegate.getValue();
  }
}
