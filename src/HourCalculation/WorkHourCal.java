package HourCalculation;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import DataStructure.AllOverHours;
import DataStructure.Everyday;
import DataStructure.MyFormat;
import DataStructure.WorkMonth;

public class WorkHourCal {

	private AllOverHours OverHour = new AllOverHours();
	private static int MaxDayWorkHour = 8;
	private static int MaxWeekWorkHour = 40;
	private static int OneHourMinutes = 60;
	private Date TrueStartTime = new Date();
	private Date TrueEndTime = new Date();
	private static int NightWorkStart = 22;
	private static int NightWorkEnd = 5;

	public AllOverHours calDayOTWH(WorkMonth workmonth, Everyday everyday) throws ParseException {

		OverHour = new AllOverHours();

		OverHour.OvertimeInWH = calOvertimeInWH(everyday);
		OverHour.InExcessOfWH = calDayInExcessOfWH(everyday);
		OverHour.LateNightWH = calLateNightWH(everyday);
		OverHour.PrescriHoliWH = calPrescriHoliWH(everyday);
		OverHour.StatuHoliWH = calStatuHoliWH(everyday);

		return OverHour;
	}

	/**
	 * get the working time and the rest time of every day
	 * 
	 * @param everyday
	 * @return Minutes of working:TimeOneDay[0] and minutes of rest:TimeOneDay[1]
	 */
	public long[] getEverydayWorkTime(Everyday everyday) {
		long[] TimeOneDay = { 0, 0 };

		for (int i = 0; i < everyday.EndTime.size(); i++) {
			Date EndTime = everyday.EndTime.get(i);
			Date StartTime = everyday.StartTime.get(i);
			if (i > 0) {
				TimeOneDay[1] += conMMToMinute(StartTime.getTime() - everyday.EndTime.get(i - 1).getTime());
			}
			TimeOneDay[0] += conMMToMinute(EndTime.getTime() - StartTime.getTime());
		}
		return TimeOneDay;
	}

	/**
	 * real working hour < 8
	 * 
	 * @param everyday
	 * @return
	 */
	public long calOvertimeInWH(Everyday everyday) {

		long[] TimeOneDay = getEverydayWorkTime(everyday);
		long OvertimeInWH = 0;
		Calendar TmpCald = Calendar.getInstance();
		TmpCald.setTime(everyday.WorkDay);

		try {
			TrueStartTime = MyFormat.TimeFormat.parse("08:00");
			TrueEndTime = MyFormat.TimeFormat.parse("16:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Except Saturday and Sunday
		if (TmpCald.get(Calendar.DAY_OF_WEEK) != 1 && TmpCald.get(Calendar.DAY_OF_WEEK) != 7) {
			if (everyday.StartTime.get(0).equals(TrueStartTime)) {
				if (TimeOneDay[0] + TimeOneDay[1] <= conHourToMinute(MaxDayWorkHour)) {
					OvertimeInWH = 0;
				} else if (TimeOneDay[0] <= conHourToMinute(MaxDayWorkHour)) {
					OvertimeInWH = TimeOneDay[0] + TimeOneDay[1] - conHourToMinute(MaxDayWorkHour);
				} else {
					OvertimeInWH = OneHourMinutes;
				}
			} else {
				if (TimeOneDay[0] + TimeOneDay[1] <= conHourToMinute(MaxDayWorkHour)) {
					OvertimeInWH = 0;
				} else if (TimeOneDay[0] <= conHourToMinute(MaxDayWorkHour)) {
					OvertimeInWH = 0;
				} else {
					// OvertimeInWH = conMMToMinute(everyday.StartTime.get(0).getTime() -
					// TrueStartTime.getTime());
					long RealWorkTime = conMMToMinute(TrueEndTime.getTime() - everyday.StartTime.get(0).getTime())
							- OneHourMinutes;
					OvertimeInWH = conHourToMinute(MaxDayWorkHour) - RealWorkTime;
				}
			}
		}
		return OvertimeInWH;
	}

	/**
	 * Calculate the overtime hours in excess of statutory working hours One day
	 * real working hour > 8
	 * 
	 * @param everyday
	 * @return Working hours in excess of statutory working hours
	 */
	public long calDayInExcessOfWH(Everyday everyday) {

		long[] TimeOneDay = getEverydayWorkTime(everyday);
		long InDayExcessOfWH = 0;
		Calendar TmpCald = Calendar.getInstance();
		TmpCald.setTime(everyday.WorkDay);

		if (TmpCald.get(Calendar.DAY_OF_WEEK) != 1 && TmpCald.get(Calendar.DAY_OF_WEEK) != 7) {
			if (TimeOneDay[0] > conHourToMinute(MaxDayWorkHour)) {
				InDayExcessOfWH = TimeOneDay[0] - conHourToMinute(MaxDayWorkHour);
			}
		}
		return InDayExcessOfWH;
	}

	/**
	 * week working hour > 40
	 * 
	 * @param workmonth
	 */
	public void calWeekInExcessOfWH(WorkMonth workmonth) {
		long InWeekExcessOfWH = 0;
		long DayExcessOfWH = 0;
		long WeekWorkHour = 0;
		long[] TempHour = { 0, 0 };

		Calendar TempMonth = Calendar.getInstance();
		TempMonth.setTime(workmonth.WorkCalMonth);

		for (int i = 0; i < workmonth.WorkDays.size(); i++) {
			Everyday everyday = workmonth.WorkDays.get(i);
			Calendar TempDay = Calendar.getInstance();
			
			TempDay.setTime(everyday.WorkDay);
			TempHour = getEverydayWorkTime(everyday);
			WeekWorkHour += TempHour[0];
			DayExcessOfWH += calDayInExcessOfWH(everyday);

			if (i < workmonth.WorkDays.size() - 1) {
				Everyday tomorrow = workmonth.WorkDays.get(i+1);
				Calendar TempTmr = Calendar.getInstance();
				TempTmr.setTime(tomorrow.WorkDay);

				if (TempTmr.get(Calendar.DAY_OF_WEEK) == 2) {
					TempHour = getEverydayWorkTime(everyday);
					if (WeekWorkHour > conHourToMinute(MaxWeekWorkHour)) {
						InWeekExcessOfWH = WeekWorkHour - conHourToMinute(MaxWeekWorkHour);
						everyday.OverTime.InExcessOfWH += InWeekExcessOfWH - DayExcessOfWH;
					}
					WeekWorkHour = 0;
					DayExcessOfWH = 0;
				}
			}
		}
	}

	/**
	 * late night working hour
	 * 
	 * @param everyday
	 * @return minutes
	 */
	public long calLateNightWH(Everyday everyday) {
		long LateNightWH = 0;

		for (int i = 0; i < everyday.EndTime.size(); i++) {

			Date EndTime = everyday.EndTime.get(i);
			Date StartTime = everyday.StartTime.get(i);

			Calendar TmpCaldEnd = Calendar.getInstance();
			Calendar TmpCaldStart = Calendar.getInstance();
			TmpCaldEnd.setTime(EndTime);
			TmpCaldStart.setTime(StartTime);

			int hour = TmpCaldEnd.get(Calendar.HOUR_OF_DAY);
			int minute = TmpCaldEnd.get(Calendar.MINUTE);

			// 22:00 ~ 5:00 Late night working hour
			if (hour > NightWorkStart) {
				LateNightWH = conHourToMinute(hour - NightWorkStart) + minute;
			} else if (hour == NightWorkStart && minute != 0) {
				LateNightWH = minute;
			} else if (hour < NightWorkEnd) {
				LateNightWH = conHourToMinute(hour + 2) + minute;
			} else if (hour == NightWorkEnd) {
				LateNightWH = conHourToMinute(hour + 2);
			}

		}
		return LateNightWH;
	}

	/**
	 * Saturday
	 * 
	 * @param everyday
	 * @return minutes
	 */
	public long calPrescriHoliWH(Everyday everyday) {
		long PrescriHoliWH = 0;

		Calendar TmpCald = Calendar.getInstance();
		TmpCald.setTime(everyday.WorkDay);

		if (TmpCald.get(Calendar.DAY_OF_WEEK) == 6) {
			for (int j = 0; j < everyday.EndTime.size(); j++) {
				Calendar TmpCaldEnd = Calendar.getInstance();
				TmpCaldEnd.setTime(everyday.EndTime.get(j));

				if (TmpCaldEnd.get(Calendar.HOUR_OF_DAY) <= 5) {
					PrescriHoliWH += conHourToMinute(TmpCaldEnd.get(Calendar.HOUR_OF_DAY))
							+ TmpCaldEnd.get(Calendar.MINUTE);
				}
			}
		}

		if (TmpCald.get(Calendar.DAY_OF_WEEK) == 7) {
			long[] time = getEverydayWorkTime(everyday);
			for (int j = 0; j < everyday.EndTime.size(); j++) {
				Calendar TmpCaldEnd = Calendar.getInstance();
				TmpCaldEnd.setTime(everyday.EndTime.get(j));

				if (TmpCaldEnd.get(Calendar.HOUR_OF_DAY) <= 5) {
					PrescriHoliWH = time[0] - conHourToMinute(TmpCaldEnd.get(Calendar.HOUR_OF_DAY))
							- TmpCaldEnd.get(Calendar.MINUTE);
				} else {
					PrescriHoliWH = time[0];
				}
			}
		}
		return PrescriHoliWH;
	}

	/**
	 * Sunday
	 * 
	 * @param everyday
	 * @return minutes
	 */
	public long calStatuHoliWH(Everyday everyday) {
		long StatuHoliWH = 0;

		Calendar TmpCald = Calendar.getInstance();
		TmpCald.setTime(everyday.WorkDay);

		if (TmpCald.get(Calendar.DAY_OF_WEEK) == 1) {
			long[] time = getEverydayWorkTime(everyday);
			for (int j = 0; j < everyday.EndTime.size(); j++) {
				Calendar TmpCaldEnd = Calendar.getInstance();
				TmpCaldEnd.setTime(everyday.EndTime.get(j));

				if (TmpCaldEnd.get(Calendar.HOUR_OF_DAY) <= 5) {
					StatuHoliWH = time[0] - conHourToMinute(TmpCaldEnd.get(Calendar.HOUR_OF_DAY))
							- TmpCaldEnd.get(Calendar.MINUTE);
				} else {
					StatuHoliWH = time[0];
				}
			}
		}
		return StatuHoliWH;
	}

	public long getDateMinute(long minute) {
		return minute % 60;
	}

	public long getDateHour(long minute) {
		return minute / 60;
	}

	public long conHourToMM(long hour) {
		return hour * 60 * 60 * 1000;
	}

	public long conHourToMinute(long hour) {
		return hour * 60;
	}

	public long conMMToMinute(long MMtime) {
		return MMtime / (60 * 1000);
	}
}
