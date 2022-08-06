package ru.iskandar.playersearcher.form;

import lombok.Getter;
import lombok.Setter;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.Schedule;

/**
 * Данные формы назначения встречи для игры.
 */
@Getter
@Setter
public class SuggestGameForm {

	private Player opponent;

	private Schedule schedule = new Schedule();

}