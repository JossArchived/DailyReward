package josscoder.dailyreward.utils;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkullUtils {

    public static ItemStack getClaimedSkull() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc1ZDEzY2ExNGJjYmJkMWNkZTIxYWEwNjYwMDEwMWU0NTZkMzE4YWFkZjE3OGIyNzkzNjc4YjQ5NGY2ZGNlOCJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lReward Claimed"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack getRewardSkull(int day) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZkNDUyODcwZDQ5MzcxOGViNjM2NDdhZDgwZTAwZjUwYjc3NDYwMWNiMDY3Nzc1ZjkwZmMxZWFhZGE4ZmNlZiJ9fX0=";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lReward of day #" + day + "\n&r&b Click to claim"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack getNilSkull() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRjMjI0MTQ1MjVlMDUwZjlhZjRmZGVmMWE0YWM1YjljNTJhYWRiOGI0NThhMGRiNzVjYWM5MDU5ZDgyODJmIn19fQ==";

        ItemStack itemStack = SkullCreator.itemFromBase64(base64);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
