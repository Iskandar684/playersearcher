package ru.iskandar.playersearcher.model;

import java.util.ArrayList;
import java.util.List;

public class HourIntervalFactory {

    public List<HourInterval> create() {
        List<HourInterval> intervals = new ArrayList<>();
        for (int h = 8; h < 21; h++) {
            intervals.add(new HourInterval(h, h + 1));
        }
        return intervals;
    }
}
