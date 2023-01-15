package ru.iskandar.playersearcher.controller;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.repo.PlayersRepo;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Player> userOpt = PlayersRepo.getInstance().findPlayerByLogin(username);
        Player user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
