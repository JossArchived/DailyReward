package josscoder.dailyreward.utils;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkullUtils {

    public static ItemStack getClaimedSkull() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIxYTNlMDRmNTBhYTkzZmJjMDBlY2IyZTcxYTJkOTUxNmZmZTQyMDZiZGNkZmE5NTNkNmJjZThmZmYxZDI0MyJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.RED + "Reward Claimed");
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static ItemStack getRewardSkull(int day) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZlNWQ5M2ZmNDFiZGVkNTc2MjA5M2JmODFhMzI3M2RiMDc5YWE0NTdiOGRiZjQ4Mjc1NDg3MGFmNGIwZjBhOSJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);
        itemStack.setAmount(day);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lReward of Day #" + day + " &r&b(Click to claim)"));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static ItemStack getNoReadySkull(int day) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRjMjI0MTQ1MjVlMDUwZjlhZjRmZGVmMWE0YWM1YjljNTJhYWRiOGI0NThhMGRiNzVjYWM5MDU5ZDgyODJmIn19fQ==";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(String.format(ChatColor.RED + "Reward of Day #%s (No ready)", day));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
