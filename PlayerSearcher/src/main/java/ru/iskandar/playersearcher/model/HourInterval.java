package ru.iskandar.playersearcher.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(prefix = "_")
@EqualsAndHashCode(of = { "_start", "_end" })
public class HourInterval implements Comparable<HourInterval> {

	private final int _start;

	private final int _end;

	public HourInterval(int aStart, int aEnd) {
		_start = aStart;
		_end = aEnd;
		if (aEnd <= aStart) {
			throw new IllegalArgumentException("Конец интервала должен быть больше начала.");
		}
	}

	public static HourInterval of(String aString) {
		// 18:00-19:00
		String[] times = aString.split("-");
		int start = Integer.valueOf(times[0].split(":")[0]);
		int end = Integer.valueOf(times[1].split(":")[0]);
		return new HourInterval(start, end);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_start);
		builder.append(":00-");
		builder.append(_end);
		builder.append(":00");
		return builder.toString();
	}

	@Override
	public int compareTo(HourInterval aOther) {
		return Integer.compare(_start, aOther._start);
	}
}
