package ru.iskandar.playersearcher.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ru.iskandar.playersearcher.model.HourInterval;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.Schedule;
import ru.iskandar.playersearcher.model.Suggestion;


//@Repository
public class SuggestionsRepo {

	private List<Suggestion> suggestions = new ArrayList<>();

	private static SuggestionsRepo INSTANCE = new SuggestionsRepo();

	private SuggestionsRepo() {
		Player pl1 = PlayersRepo.getInstance().findPlayerByLogin("anton").orElse(null);
		Player pl2 = PlayersRepo.getInstance().findPlayerByLogin("inga").orElse(null);
		Schedule sh1 = new Schedule();
		sh1.setIntervalsByDay6(Collections.singletonList(new HourInterval(18, 19).toString()));
		Schedule sh2 = new Schedule();
		sh2.setIntervalsByDay7(Collections.singletonList(new HourInterval(16, 17).toString()));
		suggestions.add(new Suggestion(pl1, sh1));
		suggestions.add(new Suggestion(pl2, sh2));
	}

	public static SuggestionsRepo getInstance() {
		return INSTANCE;
	}

	public List<Suggestion> getSuggestions() {
		return Collections.unmodifiableList(suggestions);
	}

	public void addSuggestion(Suggestion aSuggestion) {
		suggestions.add(aSuggestion);
	}

	public Optional<Suggestion> findByLogin(String aLogin) {
		return suggestions.stream().filter(suggestion -> suggestion.getPlayer().getLogin().equals(aLogin)).findFirst();
	}

}
