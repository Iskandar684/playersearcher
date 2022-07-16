package ru.iskandar.playersearcher.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Предложение игры.
 */
@Setter
@Getter
@Accessors(prefix = "_")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "_player")
public class Suggestion {

	/** Игрок */
	private Player _player;

	/** Возможный график игры */
	private Schedule _schedule;

	private String _description;
	
	private String _UUID = "myUUID";

	public Suggestion(Player aPlayer, Schedule aSchedule) {
		_player = aPlayer;
		_schedule = aSchedule;
	}
	
	

}
