package ru.iskandar.playersearcher.model;

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
@EqualsAndHashCode(of = "_player")
public class Suggestion {

	/** Игрок */
	private Player _player;

	/** Возможный график игры */
	private Schedule _schedule;

	private String _description;

	private LinkDescription _createOrEditSuggestionLink;

	private LinkDescription _cancelSuggestionLink;

	public Suggestion(Player aPlayer, Schedule aSchedule) {
		_player = aPlayer;
		_schedule = aSchedule;
	}

	public boolean hasCancelSuggestionLink() {
		return _cancelSuggestionLink != null;
	}

	public boolean hasCreateOrEditSuggestionLink() {
		return _createOrEditSuggestionLink != null;
	}

}
