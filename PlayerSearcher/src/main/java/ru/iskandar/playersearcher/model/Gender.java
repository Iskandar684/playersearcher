package ru.iskandar.playersearcher.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * Пол.
 */
@Getter
@Accessors(prefix = "_")
@EqualsAndHashCode(of = "_text")
public class Gender {

    /**
     * Мужчина
     */
    public static Gender MALE = new Gender("Мужчина");

    /**
     * Женщина
     */
    public static Gender FEMALE = new Gender("Женщина");

    private String _text;


    public Gender(String aText) {
        //Нужен публичный конструктор. Поэтому enum не подходит.
        _text = aText;
    }

    public static List<Gender> values() {
        return Arrays.asList(MALE, FEMALE);
    }
    
	public boolean isNone () {
		return ModelConstants.NONE_PARAMS_ID.equals(_text) ;
	}

    @Override
    public String toString() {
        return _text;
    }
}
