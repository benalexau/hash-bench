package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.zip.Adler32;

public class AdlerHasher implements Hasher {
  public static final String JRE_ADLER_32 = "adler32-jre";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(AdlerHasher.JRE_ADLER_32, new AdlerHasher());
  }

  private final Adler32 delegate = new Adler32();

  private AdlerHasher() {}

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
