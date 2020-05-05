package ru.iskandar.playersearcher.repo;

import ru.iskandar.playersearcher.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlayersRepo {

    private static final PlayersRepo INSTANCE = new PlayersRepo();

    private PlayersRepo(){

    }

    private List<Player> _players = new ArrayList<>();

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(_players);
    }

    public void addPlayer(Player aPlayer){
        _players.add(aPlayer);
    }

    public static PlayersRepo getInstance() {
        return INSTANCE;
    }

    public boolean hasPlayerByLogin(String aLogin){
        return _players.stream().anyMatch(player -> Objects.equals(aLogin, player.getLogin()));
    }
}
