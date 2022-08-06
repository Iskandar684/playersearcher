package ru.iskandar.playersearcher.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HourIntervalTest {

	@Test
	public void testValueOfCheckStart() {
		//
		String str = "18:00-19:00";
		//
		HourInterval interval = HourInterval.of(str);
		//
		assertEquals (18, interval.getStart());
	}
	
	@Test
	public void testValueOfCheckEnd() {
		//
		String str = "18:00-19:00";
		//
		HourInterval interval = HourInterval.of(str);
		//
		assertEquals (19, interval.getEnd());
	}

}
