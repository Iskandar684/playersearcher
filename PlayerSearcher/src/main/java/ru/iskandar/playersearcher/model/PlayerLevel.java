package ru.iskandar.playersearcher.model;

import java.util.Arrays;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Уровень игрока.
 */
@Getter
@Accessors(prefix = "_")
@EqualsAndHashCode(of = "_text")
public class PlayerLevel {

	public static final PlayerLevel AMATEUR = new PlayerLevel("Любитель");

	public static final PlayerLevel PROFESSIONAL = new PlayerLevel("Профессионал");

	private final String _text;

	public PlayerLevel(String aText) {
		// Нужен публичный конструктор. Поэтому enum не подходит.
		_text = aText;
	}

	public static List<PlayerLevel> values() {
		return Arrays.asList(AMATEUR, PROFESSIONAL);
	}

	@Override
	public String toString() {
		return _text;
	}

}
