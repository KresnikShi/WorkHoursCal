package Main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Main Function
 * 
 * @author SHIJun
 *
 */
public class Main {
	
	public static void main(String[] args) {
		// input the path of data file
		String FilePath = null;
		Scanner input = new Scanner(System.in);
		System.out.print("Please input the path of your file:");
		String InputPath = input.nextLine();

		// check if the input is null
		if(InputPath.isEmpty()) {
		System.out.println("This is my own test data.");
		FilePath = "Data/TestData.txt";
		} else {
		FilePath = InputPath;
		}

		//
		DataProcessing dp = new DataProcessing();
		dp.processData(FilePath);
	}

}

