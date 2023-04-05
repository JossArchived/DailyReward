package josscoder.dailyreward.listener;

import josscoder.dailyreward.mongodb.MongoDBProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class AccountHandlerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        MongoDBProvider mongoDBProvider = MongoDBProvider.getInstance();

        if (!mongoDBProvider.existsUserDoc(uuid)) {
            mongoDBProvider.createUserDoc(uuid);
            return;
        }

        if (mongoDBProvider.connectedToday(uuid)) {
            return;
        }

        mongoDBProvider.updateLastLogin(uuid);
        mongoDBProvider.increaseConsecutiveDays(uuid);
    }
}
