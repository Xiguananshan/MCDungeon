package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class BowService extends BukkitRunnable {

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getInventory().getItemInMainHand().getType() == Material.BOW){
                boolean has = false;
                for(ItemStack stack : player.getInventory()){
                    if(stack !=null  && stack.getType() == Material.ARROW){
                        has = true;
                        break;
                    }
                }
                if(!has){
                    player.getInventory().addItem(ToolClass.fromItem(ToolClass.IceBowArrow));
                }
            }
        }
    }
}
