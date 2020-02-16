package ru.iskandar.playersearcher.model;

/**
 * Предложение игры.
 */
public class Suggestion {

    private Player player;

    private  Schedule _schedule;

    public Suggestion(Player aPlayer, Schedule aSchedule) {
        this.player = aPlayer;
        this._schedule = aSchedule;
    }

    public Player getPlayer() {
        return player;
    }

    public Schedule getSchedule() {
        return _schedule;
    }



    public void setPlayer(Player aPlayer) {
        player =  aPlayer;
    }



    public void setSchedule(Schedule aSchedule) {
        _schedule = aSchedule;
    }
}
