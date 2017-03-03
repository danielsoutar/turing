
public class PRINTER {

	public static void main(String[] args) {
		print(1000);
	}
	
	public static void print(int size) {
		String number = "9";
		for(int i = 0; i < size ; i++) {
			System.out.println(number + "#9_");
			number += "9";
		}
	}
}
