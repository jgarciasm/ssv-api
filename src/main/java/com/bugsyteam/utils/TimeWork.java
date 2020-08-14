package com.bugsyteam.utils;

/**
 * The Class TimeWork that encapsulates methods to work with time and dates.
 */
public class TimeWork {

	public static void delayTime() {
		try {
			Thread.sleep((int) (Math.random() * ((15000 - 8000) + 1)) + 8000);
			// Thread.sleep((int) (Math.random() * ((500 - 100) + 1)) + 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static long getMillisFromSeconds(long seconds) {
		return seconds * 1000;
	}

	public static long getMillisFromMinutes(long minutes) {
		return getMillisFromSeconds(minutes * 60);
	}

	public static long getMillisFromHours(long hours) {
		return getMillisFromMinutes(hours * 60);
	}

}
