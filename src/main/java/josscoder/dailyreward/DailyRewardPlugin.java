package josscoder.dailyreward;

import com.samjakob.spigui.SpiGUI;
import josscoder.dailyreward.listener.ConnectionListener;
import josscoder.dailyreward.menu.RewardMenu;
import josscoder.dailyreward.mongodb.MongoDBProvider;
import josscoder.dailyreward.reward.RewardFactory;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class DailyRewardPlugin extends JavaPlugin {

    @Getter
    private static DailyRewardPlugin instance;

    private SpiGUI gui;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();
        MongoDBProvider.make(config.getString("mongodb.host", "localhost"),
                config.getInt("mongodb.port", 27017),
                config.getString("mongodb.database"),
                config.getString("mongodb.username"),
                config.getString("mongodb.password", "password")
        );

        RewardFactory.make(config);

        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);

        gui = new SpiGUI(this);

        getServer().getPluginCommand("dailyreward").setExecutor((commandSender, command, s, strings) -> {
            if (!(commandSender instanceof Player)) {
                return false;
            }

            RewardMenu rewardMenu = new RewardMenu(((Player) commandSender).getPlayer());
            rewardMenu.send();

            return true;
        });

        getLogger().info(ChatColor.GREEN + "this plugin has been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "this plugin has been disabled");
    }
}
