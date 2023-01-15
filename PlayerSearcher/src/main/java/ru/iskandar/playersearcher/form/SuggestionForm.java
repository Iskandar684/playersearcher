package ru.iskandar.playersearcher.form;

import lombok.Getter;
import lombok.Setter;
import ru.iskandar.playersearcher.model.Schedule;

@Getter
@Setter
public class SuggestionForm {

    private Schedule schedule = new Schedule();

}