package ru.iskandar.playersearcher.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import ru.iskandar.playersearcher.model.Gender;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.PlayerLevel;

import java.util.*;

public class PlayersRepo {

    private static final PlayersRepo INSTANCE = new PlayersRepo();

    private List<Player> _players = new ArrayList<>();

    private PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

    public PlayersRepo() {
        System.out.println("passwordEncoder " + passwordEncoder);
        Player pl1 = new Player("anton", passwordEncoder.encode("anton"), "Антон", Gender.MALE, PlayerLevel.PROFESSIONAL);
        Player pl2 = new Player("inga", passwordEncoder.encode("inga"), "Инга", Gender.FEMALE, PlayerLevel.AMATEUR);
        _players.add(pl1);
        _players.add(pl2);
    }


    public List<Player> getPlayers() {
        return Collections.unmodifiableList(_players);
    }

    public void addPlayer(Player aPlayer) {
        _players.add(aPlayer);
    }

    public static PlayersRepo getInstance() {
        return INSTANCE;
    }

    public boolean hasPlayerByLogin(String aLogin) {
        return _players.stream().anyMatch(player -> Objects.equals(aLogin, player.getLogin()));
    }

    public Optional<Player> findPlayerByLogin(String aLogin) {
        return _players.stream().filter(player -> Objects.equals(aLogin, player.getLogin())).findFirst();
    }
}
