## Overview
Hash-Bench provides a JMH (Java microbenchmark harness) and
[published results](results/1/README.md) for the following JVM hashing implementations:

* [Adler32](https://docs.oracle.com/javase/8/docs/api/java/util/zip/Adler32.html)
* [CRC32](https://docs.oracle.com/javase/8/docs/api/java/util/zip/CRC32.html)
* [Guava](https://github.com/google/guava/wiki/HashingExplained)
* [xxHash for Java](https://github.com/jpountz/lz4-java)
* [SipHash_2_4](http://www.forward.com.au/pfod/SipHashJavaLibrary/index.html)
* [siphash-java-inline](https://github.com/nahi/siphash-java-inline)
* [Zero Allocation Hashing](https://github.com/OpenHFT/Zero-Allocation-Hashing)

![Results](results/1/2048.png)

## Scope
This project is focused on JVM performance.

It does not test the accuracy of the implementations.

Please remember that latency is only one consideration. There is considerable
variation between hash algorithms. Some common variations include:

* Overall hash quality (eg see [SMHasher](http://code.google.com/p/smhasher/))
* Lack of [cryptographic hash](https://en.wikipedia.org/wiki/Cryptographic_hash_function) support
* Whether a hash remains consistent across process restarts or not
* Guarantees around machine-dependence (eg byte order)
* Output length (and associated storage and/or transmission costs)

## Test Configuration
Implementations vary considerably with respect to the inputs they are able to
hash. The most basic support is hashing an entire ``byte[]``. Most
implementations permit an offset and length to be nominated for a ``byte[]``.
Some implementations offer
[ByteBuffer](http://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)
awareness, others use ``Unsafe``, and some delegate to native code via JNI.

My motivating use case for developing this benchmark required hashing
variable-length messages from a proprietary framed IO stream. As such this
benchmark populates a 64 KB buffer with random bytes and then requires each
implementation to hash a particular slice of that buffer. In order to provide
each implementation with the best opportunity to efficiently hash such
IO-sourced slices, the following three scenarios are benchmarked:

* ``byte[]`` from a given offset for a given length
* Array-backed ``ByteBuffer`` from a given offset for a given length
* Direct (native) ``ByteBuffer`` from a given offset for a given length

The [adapter pattern](https://en.wikipedia.org/wiki/Adapter_pattern) is used to
abstract each implementation. This ensures each implementation is tested in the
same manner and by the same harness. Each adapter implementation contains the
minimal logic required to support the above three scenarios. For some of the
simpler implementations it was necessary to copy bytes into a dedicated
``byte[]`` or prepare a ``ByteBuffer`` view.

## Preparation
Until [xxHash for Java](https://github.com/jpountz/lz4-java) 1.4 is released,
please clone and build it locally to access the latest buffer fixes. Then
edit the ``hash-bench/pom.xml`` to reflect the locally-installed snapshot.

Similarly locally build [Zero Allocation Hashing](https://github.com/OpenHFT/Zero-Allocation-Hashing)
before 0.5 is released.

## Running
You'll need at least Java 8 and Maven 3 installed. Then:

    cd hash-bench
    mvn clean package
    java -jar target/benchmarks.jar

This will run in default mode, testing all known libraries and input lengths.
This takes roughly eight hours with server-grade (Xeon E5-2667) hardware.

You can append ``-h`` to the ``java -jar`` line for JMH help. For example, use:

  * ``-wi 0`` to run zero warm-ups (not recommended)
  * ``-i 1`` to run one iteration only (not recommended)
  * ``-f 1`` to run one fork only (not recommended)
  * ``-p length=8,1024`` to test input lengths of 8 and 1024 only
  * ``-p algo=xxh64-zah,xxh64-jpountz-unsafe`` to test two XXH64 implementations
  * ``-rf csv`` to emit CSV output (for use in a spreadsheet etc)

## Naming Convention
Algorithm names (such as ``xxh64-jpountz-unsafe``) are used in reports and
optionally for the ``-p algo`` option. The naming convention is:

    hash-implementation[-qualifier]

The ``hash`` portion denotes the underlying hash algorithm (and potential size
disambiguation). The ``implementation`` is a short abbreviation that identifes
the implementation from those listed at the top of this document. A
``qualifier`` is used if the implementation has been tested in a specific mode.

The published results page uses this naming convention to group related results.
For example, all implementations of a particular hash are compared, and all
available hashes from a particular implementation are compared.

Invoke ``java -cp target/benchmarks.jar au.com.acegi.hashbench.HashBench`` to
list all supported algorithm names. These are also displayed in the ``algo``
column when you run the actual benchmark, and in the result pages and plots.

## License
MIT License, as per [LICENSE.txt](LICENSE.txt).

Two hash implementations (SipHash_2_4, Siphash-java-inline) are not available
from any known Maven repository. As each implementation is a single file, they
have been placed in the ``thirdparty`` directory. Their licenses are shown
in those files.

## Contributing
Please send a pull request if you'd like to improve the project (eg use a
particular hash library in a more efficient manner, add new libraries, update
to new library versions etc).

## Results
| Date       | Processor     | JVM              | Results Link             |
| ---------- | ------------- | ---------------- | ------------------------ |
| 2015-09-24 | Xeon E5-2667  | OpenJDK 1.8.0_60 | [1](results/1/README.md) |
