package ru.iskandar.playersearcher.controller;

import java.util.ArrayList;
import java.util.List;

import ru.iskandar.playersearcher.form.SuggestionForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.iskandar.playersearcher.model.*;

@Controller
public class MainController {

    private static List<Suggestion> suggestions = new ArrayList<>();

    static {
        Player pl1 = new Player("Антон", Gender.MALE, PlayerLevel.PROFESSIONAL);
        Player pl2 = new Player("Инга", Gender.FEMALE, PlayerLevel.AMATEUR);
        Schedule sh1 = new Schedule();
        Schedule sh2 = new Schedule();
        suggestions.add(new Suggestion(pl1, sh1));
        suggestions.add(new Suggestion(pl2, sh2));
    }

    // ​​​​​​​
    // Вводится (inject) из application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", message);
        return "index";
    }

    @RequestMapping(value = {"/suggestions"}, method = RequestMethod.GET)
    public String getSuggestions(Model model) {
        model.addAttribute("suggestions", suggestions);
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
        return "addSuggestion";
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.POST)
    public String saveSuggestion(Model model,
                                 @ModelAttribute("suggestionForm") SuggestionForm suggestionForm) {
        String firstName = suggestionForm.getFirstName();
        Gender gender = suggestionForm.getGender();
        System.out.println("savePerson   gender from form " + gender);
        PlayerLevel level = suggestionForm.getLevel();
        if (firstName != null && !firstName.isEmpty()
                && gender != null && level != null) {
            Player pl = new Player(firstName, gender, level);
            Schedule sh1 = new Schedule();
            suggestions.add(new Suggestion(pl, sh1));
            return "redirect:/suggestions";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "addSuggestion";
    }

}