package Main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPOutputStream;


/**
 * Main Function
 * 
 * @author SHIJun
 *
 */
public class Main {
	
	

	// public static void main(String[] args) {
	// // input the path of data file
	// String FilePath = null;
	// Scanner input = new Scanner(System.in);
	// System.out.print("Please input the path of your file:");
	// String InputPath = input.nextLine();
	//
	// // check if the input is null
	// if(InputPath.isEmpty()) {
	// System.out.println("This is my own test data.");
	// FilePath = "Data/TestData.txt";
	// } else {
	// FilePath = InputPath;
	// }
	//
	// //
	// DataProcessing dp = new DataProcessing();
	// dp.processData(FilePath);
	//
	// }
	/*
	 * public static void main(String[] args) { String str = new String(""); for
	 * (int i = 0, l = args.length; i < l; i++) { for(int j = 0; j <
	 * args[i].length(); j++) { char c = args[i].charAt(j);
	 * 
	 * if(isbig(c)) { c += 13; if(c > 'Z') { c = (char) ('A' + c - 'Z'); } }
	 * if(ismall(c)) { c += 13; if(c > 'z') { c = (char) ('a' + c - 'z'); } } str +=
	 * c; }
	 * 
	 * System.out.println(str); str = ""; }
	 * 
	 * }
	 * 
	 * 
	 * public static boolean isbig(int c) { return (c >= 'A' && c <= 'Z'); }
	 * 
	 * public static boolean ismall(int c) { return (c >= 'a' && c <= 'z'); }
	 */

	public static void main(String[] args) 
    {
//		String filepath = args[1];
		
		String filepath = "Data/TestData.txt";

		File DataFile = new File(filepath);
		String MyTxt = null;
		//MyTxt[1];
		if(DataFile.isFile() && DataFile.exists()) {
			
			try {
				InputStreamReader FileStream = new InputStreamReader(new FileInputStream(DataFile));
				BufferedReader FileBuffer = new BufferedReader(FileStream);
				MyTxt = FileBuffer.readLine();
				FileBuffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 

//		System.out.println(MyTxt);
		
//		int countSame = 1;
//		int countDiff = 1;
//		char tmp1 = 0 ;
//		char tmp2 = 0 ;
//		String newStr = "";
//		String SameStr = "";
//		String DiffStr = "";
//		for(int i = 0; i < MyTxt.length() - 1; i++) {
//			if(i < MyTxt.length() -1) {
//				tmp1 = MyTxt.charAt(i);
//				tmp2 = MyTxt.charAt(i+1);
//				if(tmp1 == tmp2) {
//					countSame ++;
//					SameStr = String.valueOf(tmp1);
//					if(!DiffStr.isEmpty()) {
//						newStr += ("-" + Integer.toString(countDiff) + DiffStr);
//					}
//				} else {
//					if(!SameStr.isEmpty()) {
//						newStr += (Integer.toString(countSame) + SameStr);
//					}
//					countDiff ++;
//					DiffStr += String.valueOf(tmp1) + String.valueOf(tmp2);
//				}
//			}
//			
//			
//		}
//		if(!DiffStr.isEmpty()) {
//			newStr += "-" + Integer.toString(countDiff) + tmp1 + tmp2;
//		}
		
		
		
		System.out.print(zipString(MyTxt));

			//JHGDBIAJIB
	}
	
	public static String zipString(String str) {
		StringBuilder sb = new StringBuilder("");
		
        if(str != null && str.length()!=0){
            for(int i = 0; i < str.length(); i++){
                
            	char ch = str.charAt(i);
                String temp = "";
                int count = 1;
                
                while(i + 1 < str.length()){
                    if(ch == str.charAt(i + 1)){
                        count++;
                        temp = String.valueOf(ch);
                    }
                    else {
                    	temp += String.valueOf(ch);
                    }
                }
                
                
                
//                while(i + 1 < str.length()){
//                    if((i + 1) < str.length() && (ch == str.charAt(i + 1))){
//                        break;
//                    }
//                    else {
//                    	i++;
//                    	sb.append("-");
//                    	count++;
//                    	temp += ch;
//                    }
//                }
                
                sb.append(count).append(temp);
                
            }
        }
        return sb.toString();

    }

}

//
