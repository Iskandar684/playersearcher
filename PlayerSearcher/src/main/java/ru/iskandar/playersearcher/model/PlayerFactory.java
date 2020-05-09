package ru.iskandar.playersearcher.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PlayerFactory {

    @NonNull
    private final PasswordEncoder passwordEncoder;

    public  Player createPlayer(NewUser aNewUser){
        String password = passwordEncoder.encode(aNewUser.getPassword());
        return new Player(aNewUser.getLogin(),password,aNewUser.getName(), aNewUser.getGender(), aNewUser.getLevel());
    }
}
