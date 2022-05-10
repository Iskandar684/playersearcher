package ru.iskandar.playersearcher.repo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.iskandar.playersearcher.model.PlayersSearchParams;

import javax.security.auth.login.FailedLoginException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayersSearchParamsRepo {

    public static final PlayersSearchParamsRepo INSTANCE = new PlayersSearchParamsRepo();


    private Map<String, PlayersSearchParams> _params = new HashMap<String, PlayersSearchParams>();

    public PlayersSearchParams getParams (@NonNull String aLogin){
       return  _params.computeIfAbsent(aLogin, login-> new PlayersSearchParams () ) ;
    }

    public void setParams (@NonNull String aLogin, PlayersSearchParams aParams){
        _params.put(aLogin, aParams);
    }

}
