package DataStructure;

import java.util.ArrayList;
import java.util.Date;

/**
 * Data structure of every day
 * For example, 2018/03/02 08:00-12:00 13:00-16:00
 * will be stored as WorkDay
 * @author SHIJun
 *
 */
public class Everyday {
	
	public Date WorkDay;
	public ArrayList<Date> StartTime;
	public ArrayList<Date> EndTime;
	public AllOverHours OverTime;
}
