package no.hild1.bank;

public class MOD11 {

	public static String calculateCD(String number) {
		// We use string as leading 0s are significant
		if (!number.matches("^[0-9]+$")) 
			throw new IllegalArgumentException("Can only calculate MOD11 for number-only strings");
		
		String[] numArr = number.split("");
		int total = 0;
		int weightArr[] = {2,3,4,5,6,7};
		int weight = 0;
		for (int i = numArr.length-1; i>0; i--) {
			total += Integer.parseInt(numArr[i]) * weightArr[weight++];
			weight %= 6;
		}		
		System.out.println(total);
		int mod = (total%11);
		System.out.println(mod);
		if (mod == 1) {
			return "-";
		} else if (mod == 0) {
			return "0";
		} else {
			return Integer.toString(11-mod);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(checkCD("123456785"));
		System.out.println(checkCD("2345676"));
		System.out.println(checkCD("23456711-"));
	}

	public static boolean checkCD(String number) {
		// We use string as leading 0s are significant
		if (!number.matches("^[0-9-]+$")) 
			throw new IllegalArgumentException("Can only calculate MOD11 for number-only strings");
		
		String[] numArr = number.split("");
		int total = 0;
		int weightArr[] = {1,2,3,4,5,6,7};
		int weight = 0;
		for (int i = numArr.length-1; i>0; i--) {
			int val = (numArr[i].equals("-") ? 10 : Integer.parseInt(numArr[i]));
			total = total + (val * weightArr[weight++]);
			if (weight == 7) { weight = 1; } // yes, this is correct
		}		
		return (total%11 == 0);
	}

}
