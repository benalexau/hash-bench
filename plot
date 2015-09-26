#!/bin/bash

if [ "$1" = "-h" -o "$1" = "--help" -o "$1" = "--h" -o "$1" = "" -o "$2" = "" ]; then
cat <<EOF
Usage: plot FILE DIR
Plots the FILE into the output DIR, including a summary README.md.

Arguments:
  FILE   Input CSV (usually jmh-result.csv)
  DIR    To write output (existing files will be removed)

EOF
exit 1;
fi

hash gplot.pl 2>/dev/null || { printf "gplot not found\n"; exit 1; }
hash dos2unix 2>/dev/null || { printf "dos2unix not found\n"; exit 1; }
hash grep 2>/dev/null || { printf "grep not found\n"; exit 1; }
hash cut 2>/dev/null || { printf "cut not found\n"; exit 1; }
hash uniq 2>/dev/null || { printf "uniq not found\n"; exit 1; }
hash sed 2>/dev/null || { printf "sed not found\n"; exit 1; }
hash tr 2>/dev/null || { printf "tr not found\n"; exit 1; }
hash sort 2>/dev/null || { printf "sort not found\n"; exit 1; }

FILE=$1
DIRECTORY=$2

if [ ! -f "$FILE" ]; then
    echo "Input file not found (use -h for help)"
    exit 1
fi

if [ ! -d "$DIRECTORY" ]; then
    echo "Output directory must exist (use -h for help)"
    exit 1
fi

dos2unix -q $FILE

BUFFERS=$(cut -d , -f 1 $FILE | sort | uniq | grep -v Benchmark | sed 's/au.com.acegi.hashbench.HashBench.with//g'  | sed 's/\"//g' | tr '\n' ' ')
ALGOS=$(cut -d , -f 8 $FILE | sort | uniq | grep -v Param | tr '\n' ' ')
LENGTHS=$(cut -d , -f 9 $FILE | sort -n | uniq | grep -v Param | tr '\n' ' ')

rm -f $DIRECTORY/*
cp $FILE $DIRECTORY

# Extract buffer-specific, algorithm-specific performance
for buffer in $BUFFERS; do
    for algo in $ALGOS; do
        expr=$buffer
        expr+='.*,'
        expr+=$algo
        expr+=','
        output=$DIRECTORY/${buffer}-${algo}.dat
        echo "# Algo: $algo" > $output
        echo "# ns/op ${buffer}_slice_length" >> $output
        grep $expr $FILE | cut -d , -f 5,9 | sed -e 's/,/ /g' >> $output
    done
done

# Extract length-specific performance (all algos)
for length in $LENGTHS; do
    output=$DIRECTORY/${length}.dat
    echo "# Length: $length" > $output
    echo "# Unit: ns/op" >> $output
    echo "# Algo Orig_Algo_Name $BUFFERS" >> $output
    for algo in $ALGOS; do
        CLEANED=$(echo "${algo}" | sed 's/_/-/g')
        echo -n "${CLEANED} ${algo}" >> $output
        for buffer in $BUFFERS; do
            input=$DIRECTORY/${buffer}-${algo}.dat
            SCORE=$(grep " ${length}$" ${input} | cut -d ' ' -f 1)
            echo -n " $SCORE" >> $output
        done
        echo "" >> $output
    done
    sort -n -k 3 $output -o $output
done

# Plot length-specific performance (all algos)
for length in $LENGTHS; do
    input=$DIRECTORY/${length}.dat
    png=$DIRECTORY/$length.png
    opts=""
    counter=2
    for buffer in $BUFFERS; do
        ((counter++))
        opts+="-name ${buffer} -using (5*column(0)):${counter}:xtic(1) $input "
    done
    gplot.pl -outfile $png -type png -title "Hash of $length Byte Slice" -xlabel "" -ylabel "ns/hash (log scale)" -set "logscale y; set xtics nomirror rotate by -270; set key top left" -pointsize 1 -style points $opts
done

# Plot algo-specific performance as length increases (all algos)
for algo in $ALGOS; do
    png=$DIRECTORY/$algo.png
    opts=""
    counter=1
    for buffer in $BUFFERS; do
        ((counter++))
        input=$DIRECTORY/${buffer}-${algo}.dat
        opts+="-name ${buffer} -using 2:1 $input "
    done
    CLEANED=$(echo "${algo}" | sed 's/_/-/g')
    gplot.pl -outfile $png -type png -title "${CLEANED} by Slice Length" -xlabel "Bytes" -ylabel "ns/hash" -set "xtics nomirror rotate by -270; set key top left" -style linespoints $opts
done

# Summary page
index=$DIRECTORY/README.md
echo "# Hash-Bench Results" >> $index

echo "## Contents" >> $index
echo "* Latency by Byte Slice Length" >> $index
for length in $LENGTHS; do
    echo "  * [${length}](#${length}-byte-slice-latency)" >> $index
done
echo "* Latency by Algorithm Implementation" >> $index
for algo in $ALGOS; do
    echo "  * [${algo}](#${algo}-latency)" >> $index
done
echo "" >> $index
echo "---" >> $index

# Summary page latency by byte slice
for length in $LENGTHS; do
    png=$DIRECTORY/$length.png
    echo "### ${length} Byte Slice Latency" >> $index
    echo "![plot](${length}.png)" >> $index
    echo "" >> $index
    # table heading row
    echo -n "| Algorithm | " >> $index
    for buffer in $BUFFERS; do
        echo -n " ${buffer} |" >> $index
    done
    echo "" >> $index
    # table heading separator row
    echo -n "| --- | " >> $index
    for buffer in $BUFFERS; do
        echo -n "---: | " >> $index
    done
    echo "" >> $index
    # table data rows
    SORTED_ALGOS=$(grep -v '#' $DIRECTORY/${length}.dat | cut -f 2 -d ' ')
    for algo in $SORTED_ALGOS; do
        echo -n "| [${algo}](#${algo}-latency)" >> $index
        for buffer in $BUFFERS; do
            expr='.*'
            expr+=${buffer}
            expr+=".*"
            expr+=${algo}
            expr+=","
            expr+=${length}
            expr+="$"
            SCORE=$(grep $expr $FILE | cut -d ',' -f 5)
            echo -n " | ${SCORE}" >> $index
        done
        echo " |" >> $index
    done
    echo "" >> $index
    echo "---" >> $index
done

# Summary page latency by algorithm impl
for algo in $ALGOS; do
    png=$DIRECTORY/${algo}.png
    echo "### ${algo} Latency" >> $index
    echo "![plot](${algo}.png)" >> $index
    echo "" >> $index
    # table heading row
    echo -n "| Length | " >> $index
    for buffer in $BUFFERS; do
        echo -n " ${buffer} |" >> $index
    done
    echo "" >> $index
    # table heading separator row
    echo -n "| --- | " >> $index
    for buffer in $BUFFERS; do
        echo -n "---: | " >> $index
    done
    echo "" >> $index
    # table data rows
    for length in $LENGTHS; do
        echo -n "| [${length}](#${length}-byte-slice-latency)" >> $index
        for buffer in $BUFFERS; do
            expr='.*'
            expr+=${buffer}
            expr+=".*"
            expr+=${algo}
            expr+=","
            expr+=${length}
            expr+="$"
            SCORE=$(grep $expr $FILE | cut -d ',' -f 5)
            echo -n " | ${SCORE}" >> $index
        done
        echo " |" >> $index
    done
    echo "" >> $index
    echo "---" >> $index
done

rm $DIRECTORY/*.dat

echo "Generated from [JMH CSV](${FILE}) on $(date -Ru) by [Hash-Bench](https://github.com/benalexau/hash-bench)." >> $index
