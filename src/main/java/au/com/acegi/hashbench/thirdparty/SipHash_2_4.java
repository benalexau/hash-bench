/*
SipHash implementation obtained from:

   http://www.forward.com.au/pfod/SipHashJavaLibrary/index.html
   
As of 24 September 2015.

Original copyright message is provided in the JavaDocs below.
*/
package au.com.acegi.hashbench.thirdparty;

//@formatter:off
/**
* SipHash_2_4 -- This is a streaming implementation of https://131002.net/siphash/<br>
* <p>
* Usage:<br>
* <pre>
// the standard test key 
byte key[] = {(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
              (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c (byte)0x0d, (byte)0x0e, (byte)0x0f};

// the standard test msg  15 bytes long 
byte msg[] = {(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
              (byte)0x08, (byte)0x09, (byte)0x0a, (byte)0x0b, (byte)0x0c (byte)0x0d, (byte)0x0e};
</pre>
* 
* Block Usage:<br>
* <pre>
 long result = sipHash.hash(key, msg);
 // this matches the pdf result https://131002.net/siphash/siphash.pdf
 System.out.println(SipHash.toHex(SipHash.longToBytes(result)); 
</pre>
* 
* <p>
* Streaming Usage:<br>
* <pre>
sipHash.init(key); // initialize with key

// for each byte of the data call updateHash( ) 
 sipHash.updateHash(b); // update hash with each byte of msg

 // after all bytes have been processed, call finish to get result
long hash = sipHash.finish(); 
</pre>
* 
* see https://131002.net/siphash/ for details of algorithm
* <p>
* (c)2013 Forward Computing and Control Pty. Ltd. (www.forward.com.au)<br>
* This code may be freely used for both private and commercial use.<br>
* Provide this copyright is maintained.<br>
*/
//@formatter:on
public class SipHash_2_4 {

  private long v0;
  private long v1;
  private long v2;
  private long v3;

  static int LONG_BYTES = Long.SIZE / Byte.SIZE;
  private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray(); // for
                                                                            // toString
                                                                            // routines

  private byte msg_byte_counter = 0; // hold number of msg bytes % 256
  private int m_idx = 0; // counter of bytes being accumulated
  private long m; // long to accumulate bytes in until we have 8

  /**
   * Initialize hash with 16 byte key <br>
   * Call this before starting each hash of a message
   * 
   * @param key
   *          16 byte secret key.
   * @throws IllegalArgumentException
   *           if key is not exactly 16 bytes long.
   */
  public void initialize(byte[] key) {
    if (key.length != 16) {
      throw new IllegalArgumentException("Key must be exactly 16 bytes.");
    }

    v0 = 0x736f6d6570736575L;
    v1 = 0x646f72616e646f6dL;
    v2 = 0x6c7967656e657261L;
    v3 = 0x7465646279746573L;

    long k0 = bytesLEtoLong(key, 0);
    long k1 = bytesLEtoLong(key, 8);

    v0 ^= k0;
    v1 ^= k1;
    v2 ^= k0;
    v3 ^= k1;
    msg_byte_counter = 0; // to track number of msg byte % 256
  }

  /**
   * Add a byte to the hash and increment the message length count % 256
   * <p>
   * The bytes are accumulated until there are 8 of them and then the
   * corresponding long (read from the bytes as LittleEndian format) is added to
   * the hash
   * 
   * @param b
   *          the byte to add
   */
  public void updateHash(byte b) {
    msg_byte_counter++; // mod 256 since this counter is only 1 byte
    m |= (((long) b & 0xff) << (m_idx * 8));
    m_idx++;
    if (m_idx >= LONG_BYTES) {
      // hash it now
      v3 ^= m;
      siphash_round();
      siphash_round();
      v0 ^= m;
      // clear counter and long
      m_idx = 0;
      m = 0;
    }
  }

  /**
   * Call this to complete the hash by processing any remaining bytes and adding
   * the msg length.
   * <p>
   * This method returns the hash as long. Use one of the utility methods,
   * longToByteLE or longToByte to convert the long to bytes in LittleEndian or
   * BigEndian format respectively and then use toHex to convert the byte[] to a
   * string of hex digits for display or transmission.
   * 
   * @return the hash as long.
   */
  public long finish() {
    byte msgLenMod256 = msg_byte_counter; // save the msg length
    // padd out the last long with zeros
    // leave one space for the message length % 256
    while (m_idx < (LONG_BYTES - 1)) { // leave one byte for length
      updateHash((byte) 0);
    }
    // add the message length
    // this will force the last long to be added to the hash
    updateHash(msgLenMod256);

    // finish off the hash
    v2 ^= 0xff;
    siphash_round();
    siphash_round();
    siphash_round();
    siphash_round();
    // calculate the final result
    // over writes v0
    v0 = v0 ^ v1 ^ v2 ^ v3;
    return v0;
  }

  /**
   * Convenience method for hashing a byte array with key
   * 
   * @param key
   * @param data
   * @return
   */
  public long hash(byte[] key, byte[] data) {
    initialize(key);
    for (int i = 0; i < data.length; i++) {
      updateHash(data[i]);
    }
    return finish(); // result in v0
  }

  /**
   * Rotate long left by shift bits. bits rotated off to the left are put back
   * on the right
   * 
   * @param l
   *          long to rotate
   * @param shift
   *          number of bits to rotate left
   * @return the rotated long
   */
  public static long rotateLeft(long l, int shift) {
    return (l << shift) | l >>> (64 - shift);
  }

  /**
   * Preform a siphash_round() on the current state
   */
  private void siphash_round() {
    v0 += v1;
    v2 += v3;
    v1 = rotateLeft(v1, 13);
    v3 = rotateLeft(v3, 16);

    v1 ^= v0;
    v3 ^= v2;
    v0 = rotateLeft(v0, 32);

    v2 += v1;
    v0 += v3;
    v1 = rotateLeft(v1, 17);
    v3 = rotateLeft(v3, 21);

    v1 ^= v2;
    v3 ^= v0;
    v2 = rotateLeft(v2, 32);
  }

  // Static Utility conversion methods

  /**
   * Converts byte[] in LittleEndian format to a long
   * 
   * @param b
   *          bytes in LittleEndian format
   * @param offset
   *          idx of first byte
   * @return the resulting long
   */
  public static long bytesLEtoLong(byte[] b, int offset) {
    if ((b.length - offset) < 8) {
      throw new IllegalArgumentException(
              "Less then 8 bytes starting from offset:" + offset);
    }
    long m = 0;
    for (int i = 0; i < 8; i++) {
      m |= ((((long) b[i + offset]) & 0xff) << (8 * i));
    }
    return m;
  }

  /**
   * Convert a long to bytes in LittleEndian format
   * 
   * @param m
   *          the long to convert
   * @return byte[8] containing the LittleEndian format of the long
   */
  public static byte[] longToBytesLE(long m) {
    byte[] b = new byte[8];
    for (int i = 0; i < 8; i++) {
      b[i] = (byte) ((m >>> 8 * i) & 0xff);
    }
    return b;
  }

  /**
   * Convert a long to bytes in BigEndian format
   * 
   * @param m
   *          the long to convert
   * @return byte[8] containing the BigEndian format of the long
   */
  public static byte[] longToBytes(long m) {
    byte[] b = new byte[8];
    for (int i = 0; i < 8; i++) {
      b[7 - i] = (byte) ((m >>> 8 * i) & 0xff);
    }
    return b;
  }

  /**
   * Convert a byte array to Hex Digits
   * 
   * @param b
   *          the byte array
   * @param offset
   *          the starting index
   * @param length
   *          the length to convert
   * @return string contain the hex digits, 2 for each byte.
   */
  public static final String toHex(byte[] b, int offset, int length) {
    if ((b.length - offset) < length) {
      throw new IllegalArgumentException(
              "Less then " + length + " bytes starting from offset:" + offset);
    }
    char[] buf = new char[length * 2];
    for (int i = 0, j = 0, k; i < length;) {
      k = b[offset + i++];
      buf[j++] = HEX_CHARS[(k >>> 4) & 0x0F];
      buf[j++] = HEX_CHARS[k & 0x0F];
    }
    return new String(buf);
  }

  /**
   * Convert a byte array to Hex Digits
   * 
   * @param b
   *          the byte array
   * @return string contain the hex digits, 2 for each byte.
   */
  public static final String toHex(byte[] b) {
    return toHex(b, 0, b.length);
  }

  /**
   * The current state of hash, v0,v1,v2,v3, as hex digits in BigEndian format
   * 
   * @return current state as a string (in BigEndian format)
   */
  public String toString() {
    String rtn = "";
    byte[] bytes;
    bytes = longToBytes(v0);
    String hexStr = toHex(bytes, 0, bytes.length);
    rtn += "v0=" + hexStr + " ";

    bytes = longToBytes(v1);
    hexStr = toHex(bytes, 0, bytes.length);
    rtn += "v1=" + hexStr + " ";

    bytes = longToBytes(v2);
    hexStr = toHex(bytes, 0, bytes.length);
    rtn += "v2=" + hexStr + " ";

    bytes = longToBytes(v3);
    hexStr = toHex(bytes, 0, bytes.length);
    rtn += "v3=" + hexStr + " ";

    return rtn;
  }

}
