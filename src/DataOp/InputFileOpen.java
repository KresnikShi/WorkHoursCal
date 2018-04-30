package DataOp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * get the data from the data file
 * store the data into a list
 * every element is one line in the file
 * @author SHIJun
 *
 */
public class InputFileOpen {

	/**
	 * necessary parameters of opening a file
	 */
	private String FilePathname = "";
	private File DataFile = null;
	private InputStreamReader FileStream = null;  
    private BufferedReader FileBuffer = null;
	private ArrayList<String> DataList = new ArrayList<String>();
    
	/**
     * file open function
     * @return a data list get from input file
     */
	public ArrayList<String> openInputFile(String FilePathname) {
		this.setFilePathname(FilePathname);
		DataFile = new File(FilePathname);
		try {
			if(DataFile.isFile() && DataFile.exists()) {
				FileStream = new InputStreamReader(new FileInputStream(DataFile));
				FileBuffer = new BufferedReader(FileStream);
				String MyTxt = null;
				while ((MyTxt = FileBuffer.readLine()) != null) {
//			        System.out.println(MyTxt);
					DataList.add(MyTxt);
			    }
				FileBuffer.close();
//				System.out.psrintln(DataList.getItemCount());
				return DataList;
			} else {
				System.out.println("Can't find the file!");
				return null;
			}
			
		} catch (Exception e) {  
			System.out.println("not existed"); 
        }
		return null;
	} // end of function openInputFile()

	public String getFilePathname() {
		return FilePathname;
	}

	public void setFilePathname(String filePathname) {
		FilePathname = filePathname;
	}
}
