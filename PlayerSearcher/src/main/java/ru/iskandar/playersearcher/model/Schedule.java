package ru.iskandar.playersearcher.model;

import java.util.*;

/**
 * График игры.
 */
public class Schedule {

    private Map<DayOfWeek, List<String>> _dayToHoursMap = new HashMap<>();

    public List<String> getIntervalsByDay1() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(1);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay1(List<String> intervalsByDay1) {
        _dayToHoursMap.put(DayOfWeek.valueOf(1), intervalsByDay1);
    }

    public List<String> getIntervalsByDay2() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(2);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay2(List<String> aIntervalsByDay) {
        _dayToHoursMap.put(DayOfWeek.valueOf(2), aIntervalsByDay);
    }

    public List<String> getIntervalsByDay3() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(3);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay3(List<String> aIntervalsByDay) {
        _dayToHoursMap.put(DayOfWeek.valueOf(2), aIntervalsByDay);
    }

    public List<String> getIntervalsByDay4() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(4);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay4(List<String> aIntervalsByDay) {
        _dayToHoursMap.put(DayOfWeek.valueOf(4), aIntervalsByDay);
    }

    public List<String> getIntervalsByDay5() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(5);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay5(List<String> aIntervalsByDay) {
        _dayToHoursMap.put(DayOfWeek.valueOf(5), aIntervalsByDay);
    }


    public List<String> getIntervalsByDay6() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(6);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay6(List<String> aIntervalsByDay) {
        _dayToHoursMap.put(DayOfWeek.valueOf(6), aIntervalsByDay);
    }

    public List<String> getIntervalsByDay7() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(7);
        List<String> intervals = _dayToHoursMap.get(dayOfWeek);
        if (intervals == null) {
            intervals = new ArrayList<>();
            _dayToHoursMap.put(dayOfWeek, intervals);
        }
        return intervals;
    }

    public void setIntervalsByDay7(List<String> aIntervalsByDay) {
        _dayToHoursMap.put(DayOfWeek.valueOf(7), aIntervalsByDay);
    }


    public boolean isEmpty() {
        for (Map.Entry<DayOfWeek, List<String>> entry : _dayToHoursMap.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<DayOfWeek, List<String>> entry : _dayToHoursMap.entrySet()) {
            Collection<String> intervals = entry.getValue();
            if (intervals == null || intervals.isEmpty()) {
                continue;
            }
            builder.append(entry.getKey());
            builder.append(System.lineSeparator());
            for (String interval : intervals) {
                builder.append(interval);
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }


}
