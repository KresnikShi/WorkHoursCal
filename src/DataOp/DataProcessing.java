package DataOp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DataStructure.Everyday;
import DataStructure.MyFormat;
import DataStructure.WorkMonth;
import HourCalculation.WorkHourCal;

/**
 * 
 * @author SHIJun
 *
 */
public class DataProcessing {

	/**
	 * parameters
	 */
	private ArrayList<String> Data = new ArrayList<String>();
	private InputFileOpen OpenFile = new InputFileOpen();
	private WorkMonth TheWorkMonth = new WorkMonth();
	private Everyday EverydayTemp = new Everyday();
	private StringSlice DataSlice = new StringSlice();
	private long SpareTime = 0;
	
	/**
	 * 
	 * @param FilePath
	 * @return
	 */
	public WorkMonth processData(String FilePath) {
		setData(OpenFile.openInputFile(FilePath));
		try {
			TheWorkMonth.WorkCalMonth = MyFormat.MonthFormat.parse(Data.get(0));
			TheWorkMonth.WorkDays = new ArrayList<Everyday>();
			
			// format error!!!
			for(int i=1; i < Data.size(); i++) {
				EverydayTemp = new Everyday();
				EverydayTemp.StartTime = new ArrayList<Date>();
				EverydayTemp.EndTime = new ArrayList<Date>();
				
				// slice " "
				String[] DayTemp = DataSlice.sliceString(Data.get(i));
				EverydayTemp.WorkDay = MyFormat.DayFormat.parse(DayTemp[0]);
				
				int j = 1;
				while(j < DayTemp.length) {
					// slice "-"
					String[] MorningTemp = DataSlice.sliceString(DayTemp[j]);
					if(MorningTemp.length == 2) {
						EverydayTemp.StartTime.add(MyFormat.TimeFormat.parse(MorningTemp[0]));
						EverydayTemp.EndTime.add(MyFormat.TimeFormat.parse(MorningTemp[1]));
					} else {
						System.out.print("0");
					}
					j++;
				}
				TheWorkMonth.WorkDays.add(EverydayTemp);
			}
			
			// print test
//			for (int a = 0; a < TheWorkMonth.WorkDays.size(); a++) {
//				Everyday ddd =TheWorkMonth.WorkDays.get(a);
//				System.out.print((a+1)+":");
//				System.out.print(DayFormat.format(ddd.WorkDay) + " ");
//				for(int b = 0; b < ddd.StartTime.size(); b++) {
//					System.out.print(TimeFormat.format(ddd.StartTime.get(b)));
//					System.out.print("-");
//					System.out.print(TimeFormat.format(ddd.EndTime.get(b)));
//					System.out.print(" ");
//				}
//				System.out.println();
//			}
//			
			
			WorkHourCal whc = new WorkHourCal();
			Calendar TempMonth = Calendar.getInstance();
			TempMonth.setTime(TheWorkMonth.WorkCalMonth);
			
			for (int a = 0; a < TheWorkMonth.WorkDays.size(); a++) {
				Everyday EverydayTime = TheWorkMonth.WorkDays.get(a);
				EverydayTime.OverTime = whc.calDayOTWH(TheWorkMonth, EverydayTime);
				TheWorkMonth.WorkDays.get(a).OverTime = EverydayTime.OverTime;
			}
			
			whc.calWeekInExcessOfWH(TheWorkMonth);
			
//			for (int a = 0; a < TheWorkMonth.WorkDays.size(); a++) {
//				Everyday EverydayTime = TheWorkMonth.WorkDays.get(a);
//				System.out.println(TimeSum(EverydayTime.OverTime.InExcessOfWH));
//			}
			
			// time sum
			long OvertimeInWH = 0;  //Overtime within statutory working hours
			long InExcessOfWH = 0;  //Overtime in excess of statutory working hours
			long LateNightWH = 0;  //Late-night overtime
			long PrescriHoliWH = 0;  //Prescribed holiday
			long StatuHoliWH = 0; 
			
			for(int b = 0; b < TheWorkMonth.WorkDays.size(); b++) {
				Everyday everyday = TheWorkMonth.WorkDays.get(b);
				Calendar TempDay = Calendar.getInstance();
				TempDay.setTime(everyday.WorkDay);
				
				if(TempDay.get(Calendar.MONTH) == TempMonth.get(Calendar.MONTH)) {
					OvertimeInWH += everyday.OverTime.OvertimeInWH;
					InExcessOfWH += everyday.OverTime.InExcessOfWH;
					LateNightWH += everyday.OverTime.LateNightWH;
					PrescriHoliWH += everyday.OverTime.PrescriHoliWH;
					StatuHoliWH += everyday.OverTime.StatuHoliWH;
				}
			}
			
			// print
			System.out.println(TimeSum(OvertimeInWH));
			System.out.println(TimeSum(InExcessOfWH));
			System.out.println(TimeSum(LateNightWH));
			System.out.println(TimeSum(PrescriHoliWH));
			System.out.println(TimeSum(StatuHoliWH));
			
			return TheWorkMonth;
			
		} catch (ParseException e) {
			System.out.println("0");
		}
		return TheWorkMonth;
	}

	public ArrayList<String> getData() {
		return Data;
	}

	public void setData(ArrayList<String> arrayList) {
		Data = arrayList;
	}
	
	public long TimeSum(long time) {
		if(time%60 <30) {
			return time/60;
		} else {
			setSpareTime(time%60);
		}
		return time/60;
	}

	public long getSpareTime() {
		return SpareTime;
	}

	public void setSpareTime(long spareTime) {
		SpareTime = spareTime;
	}
}
