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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                if (reward.getDay() > mongoDBProvider.getConsecutiveDays(player.getUniqueId())) {
                    button = new SGButton(SkullUtils.getNoReadySkull(day.get()))
                            .withListener((InventoryClickEvent event) -> {
                                HumanEntity whoClicked = event.getWhoClicked();
                                whoClicked.getWorld().playSound(whoClicked.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.5f);
                                whoClicked.sendMessage(ChatColor.RED + "You still haven't entered the days needed to claim this gift!");
                                whoClicked.closeInventory();
                            });
                } else {
                    if (mongoDBProvider.hasDayClaimed(day.get(), player.getUniqueId())) {
                        button = new SGButton(SkullUtils.getClaimedSkull())
                                .withListener((InventoryClickEvent event) -> {
                                    HumanEntity whoClicked = event.getWhoClicked();
                                    whoClicked.getWorld().playSound(whoClicked.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.5f);
                                    whoClicked.sendMessage(ChatColor.RED + "You have already claimed the rewards of that day!");
                                    whoClicked.closeInventory();
                                });
                    } else {
                        button = new SGButton(SkullUtils.getRewardSkull(day.get()))
                                .withListener((InventoryClickEvent event) -> {
                                    HumanEntity whoClicked = event.getWhoClicked();
                                    whoClicked.getWorld().playSound(whoClicked.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    whoClicked.sendMessage(ChatColor.GREEN + "You have successfully claimed that day's gift.");

                                    ItemStack currentItem = event.getCurrentItem();
                                    if (currentItem != null) {
                                        mongoDBProvider.claimDayReward(currentItem.getAmount(), player.getUniqueId());
                                    }

                                    reward.getCommands().forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", player.getName())));

                                    whoClicked.closeInventory();
                                });
                    }
                }
            } else {
                ItemStack itemStack = new ItemStack(Material.BARRIER);
                itemStack.setAmount(day.get());
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(" ");
                    itemStack.setItemMeta(itemMeta);
                }

                button = new SGButton(itemStack)
                        .withListener((InventoryClickEvent event) -> {
                            HumanEntity whoClicked = event.getWhoClicked();

                            whoClicked.getWorld().playSound(whoClicked.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.5f);
                            whoClicked.sendMessage(ChatColor.RED + "There are no rewards for this day!");
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
