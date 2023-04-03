package josscoder.dailyreward;

import josscoder.dailyreward.menu.RewardMenu;
import josscoder.dailyreward.mongodb.MongoDBProvider;
import josscoder.dailyreward.reward.RewardFactory;
import lombok.Getter;
import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class DailyRewardPlugin extends JavaPlugin {

    @Getter
    private static DailyRewardPlugin instance;

    private MongoDBProvider mongoDBProvider;

    @Override
    public void onLoad() {
        instance = this;
        mongoDBProvider = new MongoDBProvider();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();
        mongoDBProvider.init(config.getString("mongodb.host", "localhost"),
                config.getInt("mongodb.port", 27017),
                config.getString("mongodb.database"),
                config.getString("mongodb.username"),
                config.getString("mongodb.password", "password")
        );

        RewardFactory.make(config);

        Events.subscribe(PlayerJoinEvent.class, EventPriority.HIGHEST).handler(event -> {
            UUID uuid = event.getPlayer().getUniqueId();

            if (mongoDBProvider.connectedToday(uuid)) {
                return;
            }

            mongoDBProvider.updateLastLogin(uuid);
            mongoDBProvider.increaseConsecutiveDays(uuid);
        });

        Commands.create()
                .assertPlayer()
                .handler(command -> new RewardMenu(command.sender()))
                .register("dailyreward");

        getLogger().info(ChatColor.GREEN + "<DailyReward> has been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "<DailyReward> has been disabled");
    }
}
