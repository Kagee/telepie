package no.hild1.bank.utils;

import java.util.regex.Pattern;

public class Consts {
	private static final String appHeader =
			  "(?<AHID>.{2})" 
			+ "(?<AHVERSION>.{1})"
			+ "(?<AHRETURNCODE>.{2})"
			+ "(?<AHPROCEDUREID>.{4})"
			+ "(?<AHTRANSACTIONDATE>.{4})"
			+ "(?<AHSEQNO>.{6})"
			+ "(?<AHTRANSCODE>.{8})"
			+ "(?<AHUSERID>.{11})"
			+ "(?<AHNOOF80CHAR>.{2})";
	public static Pattern appHeaderPattern = Pattern.compile(appHeader);
}
