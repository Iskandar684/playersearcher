package ru.iskandar.playersearcher.repo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.NonNull;

/**
 * Репозиторий встреч для игры.
 */
//@Repository
public class MeetingRepo {

	/***/
	public static final MeetingRepo INSTANCE = new MeetingRepo();

	private final Set<Meeting> _meetings = new HashSet<>();

	public void addMeeting(@NonNull Meeting aMeeting) {
		_meetings.add(aMeeting);
	}

	public void removeMeeting(@NonNull Meeting aMeeting) {
		_meetings.remove(aMeeting);
	}

	public Set<Meeting> getMeetings() {
		return Collections.unmodifiableSet(_meetings);
	}

}
