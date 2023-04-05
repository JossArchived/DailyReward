package josscoder.dailyreward.session;

import josscoder.dailyreward.session.data.UserSession;
import lombok.Getter;

import java.util.*;

public class SessionFactory {

    @Getter
    private static SessionFactory instance;

    private final Map<UUID, UserSession> sessionStorage = new HashMap<>();

    public SessionFactory() {
        instance = this;
    }

    public static void make() {
        new SessionFactory();
    }

    public void store(UserSession userSession) {
        sessionStorage.put(userSession.getUuid(), userSession);
    }

    public void remove(UUID uuid) {
        sessionStorage.remove(uuid);
    }

    public UserSession getSession(UUID uuid) {
        return sessionStorage.get(uuid);
    }
}
