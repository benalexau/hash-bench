package au.com.acegi.hashbench.hashers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

import au.com.acegi.hashbench.thirdparty.SipHash_2_4;

public class SipFwdHasher implements Hasher {
  private static final byte KEY[] = {(byte) 0x00, (byte) 0x01, (byte) 0x02,
          (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
          (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c,
          (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};

  public static final String SIP_FWD = "sip_hash_fwd_eng";

  public static final void register(final Map<String, Hasher> hashers) {
    hashers.put(SipFwdHasher.SIP_FWD, new SipFwdHasher());
  }

  private final SipHash_2_4 delegate = new SipHash_2_4();

  private SipFwdHasher() {}

  @Override
  public long hash(final byte[] in, final int off, final int len) {
    final byte[] data = Arrays.copyOfRange(in, off, off + len);
    return this.delegate.hash(SipFwdHasher.KEY, data);
  }

  @Override
  public long hash(final ByteBuffer bb, final int off, final int len) {
    final ByteBuffer view = bb.duplicate();
    view.position(off);
    view.limit(off + len);
    final byte[] data = new byte[len];
    view.get(data, 0, len);

    return this.delegate.hash(SipFwdHasher.KEY, data);
  }
}
