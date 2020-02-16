package ru.iskandar.playersearcher.model;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DayOfWeek {

    private int _dayOfWeek;

    public DayOfWeek(int aDayOfWeek) {
        _dayOfWeek = aDayOfWeek;
    }

    public static DayOfWeek valueOf(int aDayOfWeek) {
        return new DayOfWeek(aDayOfWeek);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayOfWeek dayOfWeek = (DayOfWeek) o;
        return _dayOfWeek == dayOfWeek._dayOfWeek;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_dayOfWeek);
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, _dayOfWeek+1);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }
}
