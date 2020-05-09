package ru.iskandar.playersearcher.controller;

import java.util.*;

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

    private Player getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        Optional<Player> player = PlayersRepo.getInstance().findPlayerByLogin(principal.getUsername());
        return player.orElseThrow(() -> new IllegalStateException("Не определен текущий пользователь."));
    }

    @RequestMapping(value = {"/suggestions"}, method = RequestMethod.GET)
    public String getSuggestions(Model model) {
        model.addAttribute("suggestions", SuggestionsRepo.getInstance().getSuggestions());
        return "suggestions";
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.GET)
    public String showAddSuggestionPage(Model model) {
        SuggestionForm suggestionForm = new SuggestionForm();
        Optional<Suggestion> suggestionOpt = SuggestionsRepo.getInstance().findByLogin(getCurrentUser().getLogin());
        suggestionOpt.ifPresent(suggestion -> suggestionForm.setSchedule(suggestion.getSchedule()));
        fillEditSuggestionModel(model, suggestionForm);
        return "addSuggestion";
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.POST)
    public String saveSuggestion(Model model,
                                 @ModelAttribute("suggestionForm") SuggestionForm aSuggestionForm) {
        Schedule schedule = aSuggestionForm.getSchedule();
        System.out.println("schedule " + schedule + "   intervals Str " + schedule.getIntervalsByDay1());
        if (schedule.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
            fillEditSuggestionModel(model, aSuggestionForm);
            return "addSuggestion";
        }
        Player player = getCurrentUser();
        Optional<Suggestion> suggestionOpt = SuggestionsRepo.getInstance().findByLogin(player.getLogin());
        if (suggestionOpt.isPresent()){
            suggestionOpt.get().setSchedule(schedule);
        }else{
            SuggestionsRepo.getInstance().addSuggestion(new Suggestion(player, schedule));
        }
        return "redirect:/suggestions";
    }

    private void fillEditSuggestionModel(Model model, SuggestionForm aSuggestionForm) {
        model.addAttribute("suggestionForm", aSuggestionForm);
        List<HourInterval> intervals = new HourIntervalFactory().create();
        model.addAttribute("intervals", intervals);
        model.addAttribute("userName", getCurrentUser().getName());
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
        fillRegistrationModel(model, new NewUser());
        return "registration";
    }


    @RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
    public String createNewUser(Model model, @ModelAttribute("newUser") NewUser aNewUser) {
        System.out.println("createNewUser " + aNewUser.getLogin());
        fillRegistrationModel(model, aNewUser);
        if (aNewUser.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        } else if (!aNewUser.passwordsIsMatch()) {
            model.addAttribute("errorMessage", "Пароли не совпадают.");
        } else if (PlayersRepo.getInstance().hasPlayerByLogin(aNewUser.getLogin())) {
            model.addAttribute("errorMessage", "Игрок с указанным логином уже зарегистрирован в системе.");
        } else {
            Player player = new PlayerFactory(passwordEncoder).createPlayer(aNewUser);
            PlayersRepo.getInstance().addPlayer(player);
            return "redirect:/registrationSuccess";
        }
        return "registration";
    }

    private void fillRegistrationModel(Model model, NewUser aNewUser) {
        model.addAttribute("newUser", aNewUser);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("levels", PlayerLevel.values());
    }

    @RequestMapping(value = {"/registrationSuccess"}, method = RequestMethod.GET)
    public String registrationSuccess() {
        return "registrationSuccess";
    }

}