package ru.iskandar.playersearcher.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(prefix = "_")
@RequiredArgsConstructor
public class LinkDescription {
	
	@NonNull
	private final String _text;
	
	@NonNull
	private final String _link;

}
