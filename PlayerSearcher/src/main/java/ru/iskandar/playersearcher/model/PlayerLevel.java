package ru.iskandar.playersearcher.model;

/**
 * Уровень игрока.
 */
public enum PlayerLevel {

    AMATEUR("Любитель"),

    PROFESSIONAL("Профессионал");

    private final String _text;

    PlayerLevel(String aText) {
        _text = aText;
    }

    @Override
    public String toString() {
        return _text;
    }

}
