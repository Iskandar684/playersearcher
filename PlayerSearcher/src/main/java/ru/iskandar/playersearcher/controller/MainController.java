package ru.iskandar.playersearcher.controller;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.NonNull;
import ru.iskandar.playersearcher.form.SuggestGameForm;
import ru.iskandar.playersearcher.form.SuggestionForm;
import ru.iskandar.playersearcher.model.AcceptDeclineLinks;
import ru.iskandar.playersearcher.model.ChatInfo;
import ru.iskandar.playersearcher.model.Gender;
import ru.iskandar.playersearcher.model.HourInterval;
import ru.iskandar.playersearcher.model.HourIntervalFactory;
import ru.iskandar.playersearcher.model.LinkDescription;
import ru.iskandar.playersearcher.model.NewUser;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.PlayerFactory;
import ru.iskandar.playersearcher.model.PlayerLevel;
import ru.iskandar.playersearcher.model.PlayersSearchParams;
import ru.iskandar.playersearcher.model.Schedule;
import ru.iskandar.playersearcher.model.Suggestion;
import ru.iskandar.playersearcher.repo.Meeting;
import ru.iskandar.playersearcher.repo.MeetingRepo;
import ru.iskandar.playersearcher.repo.MeetingStatus;
import ru.iskandar.playersearcher.repo.PlayersRepo;
import ru.iskandar.playersearcher.repo.PlayersSearchParamsRepo;
import ru.iskandar.playersearcher.repo.SuggestionsRepo;

@Controller
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService _emailService;

    // ​​​​​​​
    // Вводится (inject) из application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", message);
        addCurrentUserName(model);
        return "index";
    }

    private Player getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        Optional<Player> player =
                PlayersRepo.getInstance().findPlayerByLogin(principal.getUsername());
        return player
                .orElseThrow(() -> new IllegalStateException("Не определен текущий пользователь."));
    }

    @RequestMapping(value = {"/suggestions"}, method = RequestMethod.GET)
    public String getSuggestions(Model model) {
        PlayersSearchParams searchParams =
                PlayersSearchParamsRepo.INSTANCE.getParams(getCurrentUser().getLogin());
        List<Suggestion> suggestions = SuggestionsRepo.getInstance().getSuggestions();
        suggestions = suggestions.stream().filter(suggestion -> filter(suggestion, searchParams))
                .collect(Collectors.toList());
        suggestions.forEach(this::fillSuggestion);
        model.addAttribute("suggestions", suggestions);
        addCurrentUserName(model);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("playerLevels", PlayerLevel.values());
        model.addAttribute("playersSearchParams", searchParams);
        return "suggestions";
    }

    private void fillSuggestion(Suggestion aSuggestion) {
        Player currentUser = getCurrentUser();
        Optional<Meeting> outgoingMeetingOpt = MeetingRepo.INSTANCE.getMeetings().stream()
                .filter(meeting -> currentUser.equals(meeting.getInitiator())
                        && meeting.getPlayer().equals(aSuggestion.getPlayer()))
                .findFirst();

        Optional<Meeting> incomingMeetingOpt = MeetingRepo.INSTANCE.getMeetings().stream().filter(
                meeting -> currentUser.equals(meeting.getPlayer())
                        && currentUser.equals(aSuggestion.getPlayer()))
                .findFirst();
        AcceptDeclineLinks acceptDeclineLinks = null;
        String desc;
        if (incomingMeetingOpt.isPresent()) {
            Player opponent = incomingMeetingOpt.get().getInitiator();
            MeetingStatus status = incomingMeetingOpt.get().getStatus();
            switch (status) {
            case SUGGESTED: {
                desc = String.format("%s пригласил вас на игру: %s.", opponent.getName(),
                        incomingMeetingOpt.get().getSchedule());
                acceptDeclineLinks = createAcceptDeclineLinks(opponent);
                break;
            }
            case ACCEPTED:
                desc = String.format("У вас назначена игра с %s: %s.", opponent.getName(),
                        incomingMeetingOpt.get().getSchedule());
                break;
            case DECLINED:
                desc = String.format("Игра с %s отклонена: %s.", opponent.getName(),
                        incomingMeetingOpt.get().getSchedule());
                break;
            default:
                desc = "";
                break;
            }
        } else {
            desc = outgoingMeetingOpt.map(this::getDescription).orElse("");
        }
        aSuggestion.setDescription(desc);
        aSuggestion.setAcceptDeclineLinks(acceptDeclineLinks);
        boolean isCurrentUserSuggestion = currentUser.equals(aSuggestion.getPlayer());
        LinkDescription createOrEditSuggestionLink = null;
        if (!isCurrentUserSuggestion
                && (outgoingMeetingOpt.isEmpty()
                        || outgoingMeetingOpt.get().getStatus() == MeetingStatus.SUGGESTED)) {
            String text = outgoingMeetingOpt.isPresent() ? "Изменить" : "Назначить игру";
            String link = String.format("%s%s", "/suggestGame?login=",
                    aSuggestion.getPlayer().getLogin());
            createOrEditSuggestionLink = new LinkDescription(text, link);
        }
        aSuggestion.setCreateOrEditSuggestionLink(createOrEditSuggestionLink);
        LinkDescription cancelLinkDescription = outgoingMeetingOpt.map(meeting -> {
            String cancelLink = String.format("%s%s", "/cancelGameSuggestion?login=",
                    outgoingMeetingOpt.get().getPlayer().getLogin());
            return new LinkDescription("Отменить", cancelLink);
        }).orElse(null);
        aSuggestion.setCancelSuggestionLink(cancelLinkDescription);
        if (!currentUser.equals(aSuggestion.getPlayer())) {
            aSuggestion.setChatLink(createChatLink(aSuggestion));
        }
    }

    private LinkDescription createChatLink(@NonNull Suggestion aSuggestion) {
        return createChatLink("Написать", aSuggestion.getPlayer());
    }

    private LinkDescription createChatLink(@NonNull String aText, @NonNull Player aPlayer) {
        String link = String.format("%s%s", "/openChat?login=", aPlayer.getLogin());
        return new LinkDescription(aText, link);
    }

    @RequestMapping(value = {"/openChat"}, method = RequestMethod.GET)
    public String openChat(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("openChat " + "  aLogin " + aLogin);
        addCurrentUserName(model);
        Player recipient = PlayersRepo.getInstance().findPlayerByLogin(aLogin).orElseThrow();
        model.addAttribute("recipient", recipient.getName());
        return String.format("redirect:/chat?login=%s", aLogin);
    }

    @RequestMapping(value = {"/chat"}, method = RequestMethod.GET)
    public String getChat(Model model, @ModelAttribute("login") String aRecipientLogin) {
        addCurrentUserName(model);
        Player recipient =
                PlayersRepo.getInstance().findPlayerByLogin(aRecipientLogin).orElseThrow();
        model.addAttribute("recipient", recipient);
        return "chat";
    }

    @RequestMapping(value = {"/messenger"}, method = RequestMethod.GET)
    public String getMessenger(Model model) {
        addCurrentUserName(model);
        Player currentUser = getCurrentUser();
        List<ChatInfo> chats = PlayersRepo.getInstance().getPlayers().stream()
                .filter(Predicate.not(currentUser::equals))
                .map((player -> ChatInfo.builder().sender(player)
                        .link(createChatLink(player.getName(), player)).build()))
                .collect(Collectors.toList());
        model.addAttribute("chats", chats);
        return "messenger";
    }

    private AcceptDeclineLinks createAcceptDeclineLinks(Player aOpponent) {
        LinkDescription acceptLink = new LinkDescription("Принять",
                String.format("/acceptMeeting?login=%s", aOpponent.getLogin()));
        LinkDescription declineLink = new LinkDescription("Отклонить",
                String.format("/declineMeeting?login=%s", aOpponent.getLogin()));
        return AcceptDeclineLinks.builder().acceptLink(acceptLink).declineLink(declineLink).build();
    }

    private String getDescription(Meeting aMeeting) {
        switch (aMeeting.getStatus()) {
        case SUGGESTED:
            return "Ожидание оппонента.";
        case ACCEPTED:
            return "Игра назначена.";
        default:
            return "";
        }
    }

    private boolean filter(Suggestion aSuggestion, PlayersSearchParams aSearchParams) {
        Gender gender = aSearchParams.getGender();
        boolean res = true;
        if (gender != null && !gender.isNone()) {
            res = gender.equals(aSuggestion.getPlayer().getGender());
        }
        PlayerLevel level = aSearchParams.getPlayerLevel();
        if (level != null && !level.isNone()) {
            res = res && level.equals(aSuggestion.getPlayer().getLevel());
        }
        return res;
    }

    private void addCurrentUserName(Model model) {
        model.addAttribute("userName", getCurrentUser().getName());
        model.addAttribute("currentUser", getCurrentUser());
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.GET)
    public String showAddSuggestionPage(Model model) {
        SuggestionForm suggestionForm = new SuggestionForm();
        Optional<Suggestion> suggestionOpt =
                SuggestionsRepo.getInstance().findByLogin(getCurrentUser().getLogin());
        suggestionOpt.ifPresent(suggestion -> suggestionForm.setSchedule(suggestion.getSchedule()));
        fillEditSuggestionModel(model, suggestionForm);
        return "addSuggestion";
    }

    @RequestMapping(value = {"/addSuggestion"}, method = RequestMethod.POST)
    public String saveSuggestion(Model model,
            @ModelAttribute("suggestionForm") SuggestionForm aSuggestionForm) {
        Schedule schedule = aSuggestionForm.getSchedule();
        System.out.println(
                "schedule " + schedule + "   intervals Str " + schedule.getIntervalsByDay1());
        if (schedule.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
            fillEditSuggestionModel(model, aSuggestionForm);
            return "addSuggestion";
        }
        Player player = getCurrentUser();
        Optional<Suggestion> suggestionOpt =
                SuggestionsRepo.getInstance().findByLogin(player.getLogin());
        if (suggestionOpt.isPresent()) {
            suggestionOpt.get().setSchedule(schedule);
        } else {
            SuggestionsRepo.getInstance().addSuggestion(new Suggestion(player, schedule));
        }
        return "redirect:/suggestions";
    }

    @RequestMapping(value = {"/searchPlayers"}, method = RequestMethod.POST)
    public String searchPlayers(Model model,
            @ModelAttribute("playersSearchParams") PlayersSearchParams searchParams) {
        PlayersSearchParamsRepo.INSTANCE.setParams(getCurrentUser().getLogin(), searchParams);
        return "redirect:/suggestions";
    }

    private void fillEditSuggestionModel(Model model, SuggestionForm aSuggestionForm) {
        model.addAttribute("suggestionForm", aSuggestionForm);
        List<HourInterval> intervals = new HourIntervalFactory().create();
        model.addAttribute("intervals", intervals);
        addCurrentUserName(model);

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
            model.addAttribute("errorMessage",
                    "Игрок с указанным логином уже зарегистрирован в системе.");
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

    @RequestMapping(value = {"/suggestGame"}, method = RequestMethod.GET)
    public String showSuggestGamePage(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("Логин оппонента " + aLogin);
        SuggestGameForm suggestGameForm = new SuggestGameForm();
        Suggestion suggestion = SuggestionsRepo.getInstance().findByLogin(aLogin).orElseThrow();
        suggestGameForm.setOpponent(suggestion.getPlayer());
        suggestGameForm.setSchedule(suggestion.getSchedule());
        model.addAttribute("suggestGameForm", suggestGameForm);
        Collection<HourInterval> intervals =
                suggestion.getSchedule().getNotEmptyIntervals().stream().sorted()
                        .collect(Collectors.toList());
        model.addAttribute("intervals", intervals);
        addCurrentUserName(model);
        return "suggestGame";
    }

    @RequestMapping(value = {"/suggestGame"}, method = RequestMethod.POST)
    public String suggestGame(Model model,
            @ModelAttribute("suggestGameForm") SuggestGameForm suggestGameForm,
            @ModelAttribute("login") String aLogin) {
        System.out.println("suggestGame " + suggestGameForm + "  aLogin " + aLogin);
        Player currentUser = getCurrentUser();
        if (currentUser.getLogin().equals(aLogin)) {
            throw new IllegalArgumentException("Нельзя назначить самому себе игру.");
        }
        Suggestion suggestion = SuggestionsRepo.getInstance().findByLogin(aLogin).orElseThrow();
        Optional<Meeting> meetingOpt = MeetingRepo.INSTANCE.getMeetings().stream()
                .filter(meeting -> currentUser.equals(meeting.getInitiator())
                        && meeting.getPlayer().equals(suggestion.getPlayer()))
                .findFirst();
        Meeting meeting;
        if (meetingOpt.isEmpty()) {
            meeting = Meeting.builder().player(suggestion.getPlayer()).initiator(getCurrentUser())
                    .status(MeetingStatus.SUGGESTED).schedule(suggestGameForm.getSchedule())
                    .build();
            MeetingRepo.INSTANCE.addMeeting(meeting);
        } else {
            meeting = meetingOpt.get();
            meeting.setSchedule(suggestGameForm.getSchedule());
        }
        Player opponent = PlayersRepo.getInstance().findPlayerByLogin(aLogin).orElseThrow();
        _emailService.sendEmail(opponent.getEmail(), "Приглашение на игру.",
                String.format("%s пригласил вас на игру.",
                        currentUser.getName()));
        return "redirect:/suggestions";
    }

    @RequestMapping(value = {"/cancelGameSuggestion"}, method = RequestMethod.GET)
    public String cancelGameSuggestion(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("cancelGameSuggestion " + "  aLogin " + aLogin);
        Player currentUser = getCurrentUser();
        Suggestion suggestion = SuggestionsRepo.getInstance().findByLogin(aLogin).orElseThrow();
        Optional<Meeting> meetingOpt = MeetingRepo.INSTANCE.getMeetings().stream()
                .filter(meeting -> currentUser.equals(meeting.getInitiator())
                        && meeting.getPlayer().equals(suggestion.getPlayer()))
                .findFirst();
        if (meetingOpt.isEmpty()) {
            String opponent = PlayersRepo.getInstance().findPlayerByLogin(aLogin)
                    .map(Player::getName).orElse(aLogin);
            throw new IllegalStateException(String.format("Встреча с %s не найдена.", opponent));
        }
        MeetingRepo.INSTANCE.removeMeeting(meetingOpt.get());
        return "redirect:/suggestions";
    }

    @RequestMapping(value = {"/acceptMeeting"}, method = RequestMethod.GET)
    public String acceptMeeting(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("acceptMeeting " + "  aLogin " + aLogin);
        Player currentUser = getCurrentUser();
        Optional<Meeting> meetingOpt = MeetingRepo.INSTANCE.getMeetings().stream()
                .filter(meeting -> Objects.equals(aLogin, meeting.getInitiator().getLogin())
                        && Objects.equals(currentUser, meeting.getPlayer()))
                .findFirst();
        Player opponent = PlayersRepo.getInstance().findPlayerByLogin(aLogin).orElseThrow();
        if (meetingOpt.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Встреча с %s не найдена.", opponent.getName()));
        }
        meetingOpt.get().setStatus(MeetingStatus.ACCEPTED);
        _emailService.sendEmail(opponent.getEmail(), "Подтверждение предстоящей игры.",
                String.format("%s принял ваше приглашение на игру.",
                        currentUser.getName()));
        return "redirect:/suggestions";
    }

    @RequestMapping(value = {"/declineMeeting"}, method = RequestMethod.GET)
    public String declineMeeting(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("declineMeeting " + "  aLogin " + aLogin);
        Player currentUser = getCurrentUser();
        Optional<Meeting> meetingOpt = MeetingRepo.INSTANCE.getMeetings().stream()
                .filter(meeting -> Objects.equals(aLogin, meeting.getInitiator().getLogin())
                        && Objects.equals(currentUser, meeting.getPlayer()))
                .findFirst();
        Player opponent = PlayersRepo.getInstance().findPlayerByLogin(aLogin).orElseThrow();
        if (meetingOpt.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Встреча с %s не найдена.", opponent.getName()));
        }
        meetingOpt.get().setStatus(MeetingStatus.DECLINED);
        _emailService.sendEmail(opponent.getEmail(), "Отмена предстоящей игры.",
                String.format("%s отклонил ваше приглашение на игру.",
                        currentUser.getName()));
        return "redirect:/suggestions";
    }

}