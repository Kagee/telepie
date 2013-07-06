set -x
for F in *.layout; 
do 
    awk -f MakeJava.awk $F >tmp.java; 
    FILE=$(basename "$F" .layout);
    # Let's NOT call this variable "PATH"
    FILEPATH="../src/no/hild1/bank/telepay/$FILE.java";
    ls -la "$FILEPATH";
    awk 'BEGIN { doPrint = 1; }
    /\/\* makeBetforData.sh START \*\// { doPrint = 0; print $0; system("cat tmp.java") }
    /\/\* makeBetforData.sh STOP \*\// { doPrint = 1; }
    { if (doPrint) print $0; }' "$FILEPATH" > "$FILEPATH".tmp;
    mv "$FILEPATH".tmp "$FILEPATH";
    rm tmp.java
done;

