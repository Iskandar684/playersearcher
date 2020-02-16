package ru.iskandar.playersearcher.model;

/**
 * Уровень игрока.
 */
public class PlayerLevel {

    public static PlayerLevel AMATEUR = new PlayerLevel("Любитель");

    public static PlayerLevel PROFESSIONAL =new PlayerLevel ("Профессионал");

    private final String _text;

    public PlayerLevel(String aText) {
        //Нужен публичный конструктор. Поэтому enum не подходит.
        _text = aText;
    }

    @Override
    public String toString() {
        return _text;
    }

}
