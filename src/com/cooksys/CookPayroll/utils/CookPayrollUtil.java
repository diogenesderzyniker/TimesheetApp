package com.cooksys.CookPayroll.utils;

public class CookPayrollUtil {

	/**
	 * Does not work with negative values <br/>
	 * 
	 * @param cycleNum
	 * @return the corresponding month
	 */
	public static Integer cycleNumToMonth(Integer cycleNum) {

		Integer month = cycleNum % 24;
		month = month / 2;
		return month;

	}

	/**
	 * the current cycles start at year 2015 <br/>
	 * Does not work with negative values <br/>
	 * 
	 * @param cycleNum
	 * @return the year corresponding to the cycleNum
	 */
	public static Integer cycleNumToYear(Integer cycleNum) {

		Integer year = cycleNum / 24;
		year += 2015;

		return year;
	}

	/**
	 * Does not work with negative values <br/>
	 * 
	 * @param cycleNum
	 * @return the cycle either one or two
	 */
	public static Integer cycleNumToHalf(Integer cycleNum) {
		Integer cycle = cycleNum % 2;
		cycle += 1;
		return cycle;

	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @param half
	 * @return the cycle id corresponding to the given input
	 */
	public static Integer cycleNum(Integer year, Integer month, Integer half) {

		Integer cycleNum = 0;

		cycleNum += month * 2;
		cycleNum += half - 1;
		cycleNum += (year - 2015) * 24;

		return cycleNum;
	}

	

}
