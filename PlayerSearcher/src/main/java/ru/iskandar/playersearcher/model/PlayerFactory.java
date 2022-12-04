package ru.iskandar.playersearcher.model;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerFactory {

    @NonNull
    private final PasswordEncoder passwordEncoder;

    public Player createPlayer(NewUser aNewUser) {
        String password = passwordEncoder.encode(aNewUser.getPassword());
        return Player.builder().login(aNewUser.getLogin()).password(password)
                .name(aNewUser.getName()).gender(aNewUser.getGender()).level(aNewUser.getLevel())
                .email(aNewUser.getEmail()).build();
    }
}
