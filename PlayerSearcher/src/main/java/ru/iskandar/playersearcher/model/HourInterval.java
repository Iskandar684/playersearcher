package ru.iskandar.playersearcher.model;

import java.util.Objects;

public class HourInterval implements Comparable<HourInterval> {

    private int _start;

    private int _end;


    public HourInterval(int aStart, int aEnd) {
        _start = aStart;
        _end = aEnd;
        if (aEnd <= aStart) {
            throw new IllegalArgumentException("Конец интервала должен быть больше начала.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourInterval that = (HourInterval) o;
        return _start == that._start &&
                _end == that._end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_start, _end);
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
