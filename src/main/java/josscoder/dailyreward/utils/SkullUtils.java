package josscoder.dailyreward.utils;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkullUtils {

    public static ItemStack getClaimedRewardSkull(int day) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdjN2MxZDBhMWNlOGRmOGQ2Y2QwODU4NjdhZWIxZjRmNzk2YTBlM2U1Y2EzZGZiNjcxZjU3N2UwZDNjZDgyMyJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.RED + String.format("Reward of Day #%s (Claimed)", day));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static ItemStack getRewardSkull(int day) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRjMzZjOWNiNTBhNTI3YWE1NTYwN2EwZGY3MTg1YWQyMGFhYmFhOTAzZThkOWFiZmM3ODI2MDcwNTU0MGRlZiJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);
        itemStack.setAmount(day);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format("&a&lReward of Day #%s &r&b(Click to claim)", day)));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static ItemStack getNoReadyRewardSkull(int day) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU2MjU2MzhkNDFhYTJkM2JiNGNmZjkzYWFkNjBkOTdlOWMxZDUyYTA0NDExZmQxZmRjNjliZmIxZmRiNTllZSJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.RED + String.format("Reward of Day #%s (No ready)", day));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
