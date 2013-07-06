BEGIN { ENUM="\tpublic enum Element {\n\t"; REGEXP=""; TOTAL=0; FIRST=1; COUNT=0; }
{ 
    if (FIRST == 1) {
        ENUM = ENUM $1;
        FIRST = 0;
        REGEXP = "\"^(" $1 ".{" $2 "})\"";
    } else {
        COUNT = COUNT + 1;
        ENUM = ENUM ", ";
        if (COUNT == 3) {
            ENUM = ENUM "\n\t\t";
            COUNT=0;
        }
        REGEXP = REGEXP "\n\t\t+ \"(?<\"+ Element." $1 ".name() + \">.{" $2 "}  \"";
        ENUM = ENUM $1;
    }
    REGEXP = REGEXP "";
    TOTAL=TOTAL+$2;
}
END { 
    if (TOTAL == 320) {
        split(FILENAME,name,".");
        print "\t/* Generated by makeBetforData.sh */";
        print "\tprivate static Log log = LogFactory.getLog(" name[1] ".class);"
        print "\tprivate static String betforRegexp = " REGEXP;
        print "\t\t+ \"$\";\n\tpublic static Pattern betforPattern = Pattern.compile(betforRegexp);"
        print "\tMatcher m;\n" ENUM "\n\t}"; 
    } else {
        print "Lengths in " FILENAME " does not total 320 "
    }
}