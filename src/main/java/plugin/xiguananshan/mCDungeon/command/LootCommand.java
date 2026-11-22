package plugin.xiguananshan.mCDungeon.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class LootCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof BlockCommandSender b){
            Location location = b.getBlock().getLocation();
            Collection<Entity> entities = b.getBlock().getWorld().getNearbyEntities(location,2,2,2);
            int count = 0;
            for(Entity entity : entities){
                if(entity instanceof Item item){
                    if(item.getItemStack().getType() == Material.GOLD_INGOT){
                        count+= item.getItemStack().getAmount();
                        item.remove();
                    }
                }
            }
            for(int i = 0;i<count;i++){
                if(Math.random() < 0.15f){
                    List<ItemStack> stacks = ToolClass.itemPool.keySet().stream().toList();
                    Random random = new Random();

                    ItemStack stack = stacks.get(random.nextInt(stacks.size()));
                    b.getBlock().getWorld().dropItemNaturally(location.clone().add(0,3,0),ToolClass.fromItem(stack));
                }
            }
            b.getBlock().getWorld().playSound(location,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,3,1);
            b.getBlock().getWorld().spawnParticle(Particle.POOF,location.clone().add(0,3,0),25);
        }
        return false;
    }
}
