package ru.iskandar.playersearcher.repo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.Schedule;

/**
 * Встреча для игры.
 */
@Getter
@Builder
@Accessors(prefix = "_")
@AllArgsConstructor
@EqualsAndHashCode(of = { "_initiator", "_player" })
public class Meeting {

	/** Игрок - инициатор */
	@NonNull
	private final Player _initiator;

	/** Игрок, принявший приглашение на игру */
	@NonNull
	private final Player _player;

	/** График игры */
	@Setter
	@NonNull
	private  Schedule _schedule;

	@Setter
	@NonNull
	private MeetingStatus _status;

}
