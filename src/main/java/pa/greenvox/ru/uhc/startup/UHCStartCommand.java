package pa.greenvox.ru.uhc.startup;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pa.greenvox.ru.uhc.UHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UHCStartCommand implements CommandExecutor {

    private static int taskID = 0;
    private static int secondsToStart = 5;
    private final Vector SpawnRadius = new Vector(-500, 0, 500);

    private final List<Vector> setBlockVectors = Arrays.asList(
            new Vector(0, -1, 0),
            new Vector(0, 2, 0),
            new Vector(1, 0 ,0),
            new Vector(-1, 0 ,0),
            new Vector(0, 0 ,1),
            new Vector(0, 0 ,-1)
    );
    private Location generateRandomLocation(World w) {
        int randomX = new Random().nextInt((int) SpawnRadius.getX(), (int) SpawnRadius.getZ());
        int randomZ = new Random().nextInt((int) SpawnRadius.getX(), (int) SpawnRadius.getZ());
        int highestY = w.getHighestBlockYAt(randomX, randomZ);

        return new Location(w, randomX, highestY + 3, randomZ);
    }

    private void generateBlocks(Location l, Material m) {
        Location newLocation = l.clone();

        for (Vector v : setBlockVectors) {
            newLocation = newLocation.add(v);

            l.getWorld().setType(newLocation, m);

            newLocation = l.clone();
        }
    }

    private void teleportPlayerRandom(Player p) {
        Location l = generateRandomLocation(p.getWorld());
        generateBlocks(l, Material.BARRIER);
        p.setGameMode(GameMode.SURVIVAL);

        p.teleport(l.toCenterLocation());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return false;

        for (Player p : Bukkit.getOnlinePlayers()) teleportPlayerRandom(p);

        for (World w : Bukkit.getWorlds()) w.setGameRule(GameRule.NATURAL_REGENERATION, false);

        Player p = (Player) sender;
        World w = p.getWorld();

        w.getWorldBorder().setSize(SpawnRadius.getZ() * 4);

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHC.Instance, () -> {
            if (secondsToStart == 0) {
                Bukkit.getScheduler().cancelTask(taskID);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    generateBlocks(player.getLocation(), Material.AIR);
                    player.sendMessage(ChatColor.GREEN + "Игра началась!");
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(UHC.Instance, () -> {
                    for (World world : Bukkit.getWorlds()) world.getWorldBorder().setSize(40, 10 * 60);
                }, 60 * 5 * 20);
                secondsToStart = 5;

                return;
            }

            for (Player player : Bukkit.getOnlinePlayers())
                player.sendMessage(ChatColor.GOLD + "Игра начнётся через " + secondsToStart + " секунд!");

            secondsToStart -= 1;

        }, 0, 20);

        return true;
    }
}
