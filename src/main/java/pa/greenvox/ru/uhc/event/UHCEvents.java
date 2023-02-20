package pa.greenvox.ru.uhc.event;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UHCEvents implements Listener {

    @EventHandler
    public void OnPlayerDies(PlayerDeathEvent e) {
        e.getPlayer().setGameMode(GameMode.SPECTATOR);

        ItemStack goldenHead = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) goldenHead.getItemMeta();
        meta.setOwningPlayer(e.getPlayer());
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("heal_head"), PersistentDataType.STRING, "true");

        goldenHead.setItemMeta(meta);

        e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation().add(0, 1, 0), goldenHead);
        e.getPlayer().getWorld().spawnParticle(Particle.REDSTONE, e.getPlayer().getLocation().add(0, 1, 0), 3, new Particle.DustOptions(Color.YELLOW, 2));
    }

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e) {
        ItemStack head = e.getItem();
        if (head == null) return;
        if (head.getType() != Material.PLAYER_HEAD) return;
        if (!e.getAction().isRightClick()) return;
        if (!head.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("heal_head"))) return;

        e.getPlayer().getInventory().setItem(e.getHand(), new ItemStack(Material.AIR));
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 3));
    }
}