package ru.iskandar.playersearcher.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(prefix = "_")
public class AcceptDeclineLinks {

	@NonNull
	private final LinkDescription _acceptLink;

	@NonNull
	private final LinkDescription _declineLink;

}
