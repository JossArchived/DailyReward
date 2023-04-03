package josscoder.dailyreward.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import josscoder.dailyreward.DailyRewardPlugin;
import josscoder.dailyreward.mongodb.MongoDBProvider;
import josscoder.dailyreward.reward.RewardFactory;
import josscoder.dailyreward.reward.data.Reward;
import josscoder.dailyreward.utils.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RewardMenu {

    private final Player player;
    private final SGMenu menuInstance;

    public RewardMenu(Player player) {
        this.player = player;

        menuInstance = DailyRewardPlugin.getInstance().getGui().create("&9&l&nDAILY REWARDS",
                6
        );
        menuInstance.setAutomaticPaginationEnabled(false);

        build();
    }

    private void build() {
        //Ignore this, menu format, XD
        List<Integer> slots = new ArrayList<>();
        slots.add(3);
        slots.add(4);
        slots.add(5);

        for (int i = 10; i <= 43; i++) {
            slots.add(i);
        }
        slots.removeAll(Arrays.asList(17, 18, 26, 27, 35, 36));
        //

        RewardFactory factory = RewardFactory.getInstance();
        MongoDBProvider mongoDBProvider = MongoDBProvider.getInstance();

        AtomicInteger day = new AtomicInteger(1);
        slots.forEach(slot -> {
            SGButton button;

            Reward reward = factory.getReward(day.get());
            if (reward != null) {
                if (mongoDBProvider.hasDayClaimed(day.get(), player.getUniqueId())) {
                    button = new SGButton(SkullUtils.getClaimedSkull())
                            .withListener((InventoryClickEvent event) -> {
                                HumanEntity whoClicked = event.getWhoClicked();
                                whoClicked.getWorld().playSound(whoClicked.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0.5f);
                                whoClicked.sendMessage(ChatColor.RED + "You have already claimed the rewards of this day!");
                            });
                } else {
                    button = new SGButton(SkullUtils.getRewardSkull(day.get()))
                            .withListener((InventoryClickEvent event) -> {
                                HumanEntity whoClicked = event.getWhoClicked();
                                whoClicked.getWorld().playSound(whoClicked.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                whoClicked.sendMessage(ChatColor.GREEN + "You correctly claimed the gift of the day #" + day.get());

                                mongoDBProvider.claimDayReward(day.get(), player.getUniqueId());

                                reward.getCommands().forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", player.getName())));
                            });
                }
            } else {
                button = new SGButton(SkullUtils.getNilSkull())
                        .withListener((InventoryClickEvent event) -> {
                            HumanEntity whoClicked = event.getWhoClicked();

                            whoClicked.sendMessage(ChatColor.GOLD + "There are no rewards for this day!");
                            whoClicked.closeInventory();
                        });
            }

            menuInstance.setButton(slot, button);

            day.addAndGet(1);
        });
    }

    public void send() {
        player.openInventory(menuInstance.getInventory());
    }
}
