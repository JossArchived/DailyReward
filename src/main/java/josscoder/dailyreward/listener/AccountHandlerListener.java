package josscoder.dailyreward.listener;

import josscoder.dailyreward.mongodb.MongoDBProvider;
import josscoder.dailyreward.session.SessionFactory;
import josscoder.dailyreward.session.data.UserSession;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountHandlerListener implements Listener {

    private final MongoDBProvider mongoDBProvider;
    private final SessionFactory sessionFactory;

    public AccountHandlerListener() {
        mongoDBProvider = MongoDBProvider.getInstance();
        sessionFactory = SessionFactory.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        CompletableFuture.runAsync(() -> {
            Document document = mongoDBProvider.getOrCreateUserDoc(uuid);
            if (!mongoDBProvider.connectedToday(document)) {
                mongoDBProvider.updateLastLogin(uuid);
                mongoDBProvider.increaseConsecutiveDays(uuid);
            }

            UserSession userSession = new UserSession(uuid, mongoDBProvider.getConsecutiveDaysFromDoc(document), mongoDBProvider.getRewardsClaimed(document));
            sessionFactory.store(userSession);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        sessionFactory.remove(uuid);
    }
}
