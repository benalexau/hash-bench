package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

import au.com.acegi.hashbench.thirdparty.SipHashInline;

public class SipInlineHasher implements Hasher {
  private static final long KEY_0 = 3945;
  private static final long KEY_1 = 6425;

  public static final String SIP_INLINE = "sip-hash-inline";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(SipInlineHasher.SIP_INLINE, new SipInlineHasher());
  }

  private SipInlineHasher() {}

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    final byte[] data = Arrays.copyOfRange(in, off, off + len);
    return SipHashInline.hash24(SipInlineHasher.KEY_0, SipInlineHasher.KEY_1,
            data);
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    final ByteBuffer view = bb.duplicate();
    view.position(off);
    view.limit(off + len);
    final byte[] data = new byte[len];
    view.get(data, 0, len);

    return SipHashInline.hash24(SipInlineHasher.KEY_0, SipInlineHasher.KEY_1,
            data);
  }
}
