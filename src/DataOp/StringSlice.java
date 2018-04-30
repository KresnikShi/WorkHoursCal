package DataOp;

public class StringSlice {

	public String[] sliceString(String str) {
		if(str.contains(" ")) {
			return str.split(" ");
		} else if(str.contains("-")) {
			return str.split("-");
		} else {
			System.out.println("Format error!");
			return null;
		}
	}
}
