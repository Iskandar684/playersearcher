package ru.iskandar.playersearcher.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.iskandar.playersearcher.form.SuggestionForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.iskandar.playersearcher.model.*;
import ru.iskandar.playersearcher.repo.PlayersRepo;
import ru.iskandar.playersearcher.repo.SuggestionsRepo;

@Controller
public class MainController {

   @Autowired
    private PasswordEncoder passwordEncoder;

    // ​​​​​​​
    // Вводится (inject) из application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("userName", getCurrentUser().getName());
        return "index";
    }

    private  Player getCurrentUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        Optional<Player> player=  PlayersRepo.getInstance().findPlayerByLogin(principal.getUsername());
        return player.orElseThrow(() ->new IllegalStateException("Не определен текущий пользователь."));
    }

    @RequestMapping(value = {"/suggestions"}, method = RequestMethod.GET)
    public String getSuggestions(Model model) {
        model.addAttribute("suggestions", SuggestionsRepo.getInstance().getSuggestions());
        return "suggestions";
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.GET)
    public String showAddSuggestionPage(Model model) {
        SuggestionForm suggestionForm = new SuggestionForm();
        List<Gender> genders = new ArrayList<>();
        genders.add(Gender.MALE);
        genders.add(Gender.FEMALE);
        model.addAttribute("genders", genders);
        model.addAttribute("suggestionForm", suggestionForm);
        List<PlayerLevel> levels = new ArrayList<>();
        levels.add(PlayerLevel.AMATEUR);
        levels.add(PlayerLevel.PROFESSIONAL);
        model.addAttribute("levels", levels);

        List<HourInterval> intervals = new HourIntervalFactory().create();
        model.addAttribute("intervals", intervals);
        return "addSuggestion";
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.POST)
    public String saveSuggestion(Model model,
                                 @ModelAttribute("suggestionForm") SuggestionForm suggestionForm) {
        String firstName = suggestionForm.getFirstName();
        Gender gender = suggestionForm.getGender();
        PlayerLevel level = suggestionForm.getLevel();
        Schedule schedule = suggestionForm.getSchedule();
        System.out.println("schedule " + schedule + "   intervals Str " + schedule.getIntervalsByDay1());
        if (!schedule.isEmpty()) {
            SuggestionsRepo.getInstance().addSuggestion(new Suggestion(getCurrentUser(), schedule));
            return "redirect:/suggestions";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "addSuggestion";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping({"/index", "/"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public String registration(Model model) {
        NewUser newUser = new NewUser();
        model.addAttribute("newUser", newUser);
        return "registration";
    }


    @RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
    public String createNewUser(Model model, @ModelAttribute("newUser") NewUser aNewUser) {
        System.out.println("createNewUser " + aNewUser.getLogin());

        if (aNewUser.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        } else if (!aNewUser.passwordsIsMatch()) {
            model.addAttribute("errorMessage", "Пароли не совпадают.");
        } else if (PlayersRepo.getInstance().hasPlayerByLogin(aNewUser.getLogin())) {
            model.addAttribute("errorMessage", "Игрок с указанным логином уже зарегистрирован в системе.");
        } else {
            String password = passwordEncoder.encode(aNewUser.getPassword());
            Player player = new Player(aNewUser.getLogin(),password,"name", Gender.MALE, PlayerLevel.AMATEUR);
            PlayersRepo.getInstance().addPlayer(player);
            return "redirect:/registrationSuccess";
        }
        return "registration";
    }

    @RequestMapping(value = {"/registrationSuccess"}, method = RequestMethod.GET)
    public String registrationSuccess() {
        return "registrationSuccess";
    }

}