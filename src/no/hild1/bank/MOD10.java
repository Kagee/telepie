package no.hild1.bank;

public class MOD10 {
	public static boolean checkCD(String number) {
		// We use string as leading 0s are significant
		if (!number.matches("^[0-9]+$"))
			throw new IllegalArgumentException(
					"Can only calculate MOD10 for number-only strings");
		String[] numArr = number.split("");
		int total = 0;
		int product = 0;
		int weight = 1;
		for (int i = numArr.length - 1; i > 0; i--) {
			product = Integer.parseInt(numArr[i]) * weight;
			// System.out.println("N:" + numArr[i] + " W:"+ weight + " P:" +
			// product + " P2:" + (product > 9 ? (product - 9) : product));
			total += (product > 9 ? (product - 9) : product);
			weight = (weight == 2 ? 1 : 2); // swap 2-1-2-1...
		}
		// System.out.println(total);
		//total = 10 - total % 10;
		return (total %10 == 0);
	}

	public static String calculateCD(String number) {
		// We use string as leading 0s are significant
		if (!number.matches("^[0-9]+$"))
			throw new IllegalArgumentException(
					"Can only calculate MOD10 for number-only strings");
		String[] numArr = number.split("");
		int total = 0;
		int product = 0;
		int weight = 2;
		for (int i = numArr.length - 1; i > 0; i--) {

			product = Integer.parseInt(numArr[i]) * weight;
			// System.out.println("N:" + numArr[i] + " W:"+ weight + " P:" +
			// product + " P2:" + (product > 9 ? (product - 9) : product));
			total += (product > 9 ? (product - 9) : product);
			weight = (weight == 2 ? 1 : 2); // swap 2-1-2-1...
		}
		// System.out.println(total);
		total = 10 - total % 10;
		return Integer.toString(total != 10 ? total : 0);
	}

	public static void main(String[] args) {
		System.out.println(checkCD("123456782"));
		System.out.println(checkCD("2345676"));
		System.out.println(checkCD("04117818"));
		System.out.println(checkCD("4117818"));
		System.out.println(checkCD("4579314"));
		System.out.println(checkCD("457937"));
		System.out.println(checkCD("04499000153338"));
		System.out.println(checkCD("0022582902"));
		System.out.println(checkCD("0023565484"));
		return;
	}

}
