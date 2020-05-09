package ru.iskandar.playersearcher.form;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.iskandar.playersearcher.model.Gender;
import ru.iskandar.playersearcher.model.HourInterval;
import ru.iskandar.playersearcher.model.PlayerLevel;
import ru.iskandar.playersearcher.model.Schedule;

@Getter
@Setter
public class SuggestionForm {

    private Schedule schedule = new Schedule();

}