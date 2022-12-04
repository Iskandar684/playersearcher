package ru.iskandar.playersearcher.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import ru.iskandar.playersearcher.model.Gender;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.PlayerLevel;

// @Repository
public class PlayersRepo {

    private static final PlayersRepo INSTANCE = new PlayersRepo();

    private List<Player> _players = new ArrayList<>();

    private PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();

    public PlayersRepo() {
        // FIXME
        String email = "playersearcher@rambler.ru";
        Player pl1 = Player.builder().login("anton").password(passwordEncoder.encode("anton"))
                .name("Антон").gender(Gender.MALE).level(PlayerLevel.PROFESSIONAL)
                .email(email).build();
        Player pl2 = Player.builder().login("inga").password(passwordEncoder.encode("inga"))
                .name("Инга").gender(Gender.FEMALE).level(PlayerLevel.AMATEUR)
                .email(email).build();
        Player pl3 = Player.builder().login("isk").password(passwordEncoder.encode("isk"))
                .name("Искандар").gender(Gender.MALE).level(PlayerLevel.AMATEUR)
                .email(email).build();
        _players.add(pl1);
        _players.add(pl2);
        _players.add(pl3);
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
        return _players.stream().filter(player -> Objects.equals(aLogin, player.getLogin()))
                .findFirst();
    }
}
