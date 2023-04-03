package josscoder.dailyreward.reward;

import josscoder.dailyreward.reward.data.Reward;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RewardFactory {

    private final FileConfiguration config;

    @Getter
    private static RewardFactory instance;

    @Getter
    private final List<Reward> rewards = new ArrayList<>();

    public RewardFactory(FileConfiguration config) {
        this.config = config;
        instance = this;

        loadRewards();
    }

    public static void make(FileConfiguration config) {
        new RewardFactory(config);
    }

    private void loadRewards() {
        ConfigurationSection section = config.getConfigurationSection("rewards");
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(key -> {
            List<String> commands = section.getStringList(key + ".commands");

            Reward reward = new Reward(Integer.parseInt(key), commands);
            rewards.add(reward);
        });
    }
}
