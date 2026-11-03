package plugin.xiguananshan.mCDungeon.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.List;


public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if(sender instanceof Player p){
            ToolClass.SummonEntity(
                    p.getWorld(),
                    p.getLocation(),
                    List.of(
                            ToolClass.FromMobItemstack(Material.LEATHER_HELMET),
                            ToolClass.FromMobItemstack(Material.LEATHER_CHESTPLATE),
                            ToolClass.FromMobItemstack(Material.LEATHER_LEGGINGS),
                            ToolClass.FromMobItemstack(Material.LEATHER_BOOTS),
                            new ItemStack(Material.AIR)
                    ),
                    false,
                    10000,
                    Zombie.class
            );
        }
        return false;
    }
}
