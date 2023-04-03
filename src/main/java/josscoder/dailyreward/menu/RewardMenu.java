package josscoder.dailyreward.menu;

import me.lucko.helper.menu.Gui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RewardMenu extends Gui {

    public RewardMenu(Player player) {
        super(player, 6, ChatColor.BOLD.toString() + ChatColor.UNDERLINE + ChatColor.GREEN + "DAILY REWARDS");
    }

    @Override
    public void redraw() {

    }
}
