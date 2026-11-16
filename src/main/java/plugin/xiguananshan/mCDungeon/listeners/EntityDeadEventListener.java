package plugin.xiguananshan.mCDungeon.listeners;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.Random;

public class EntityDeadEventListener implements Listener {
    @EventHandler
    public void run(EntityDeathEvent e){
        if (e.getEntity().getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))) {
            World world = e.getEntity().getWorld();
            if (!e.getEntity().getPersistentDataContainer().has(ToolClass.fromNamespaceKey("boss"))) {
                Random random = new Random();
                int iron = random.nextInt(6);
                if(iron > 0){
                    ItemStack iron_stack = ToolClass.Iron.clone();
                    iron_stack.setAmount(iron);
                    world.dropItemNaturally(e.getEntity().getLocation(),iron_stack);
                }

                double f = Math.random();
                int gold;
                if(f<0.05){
                    gold = random.nextInt(4)+1;
                    ItemStack gold_stack = ToolClass.Gold.clone();
                    gold_stack.setAmount(gold);
                    world.dropItemNaturally(e.getEntity().getLocation(),gold_stack);
                }

            }else{
                Random random = new Random();
                int iron = random.nextInt(23)+10;
                int gold = random.nextInt(9)+2;
                ItemStack iron_stack = ToolClass.Iron.clone();
                iron_stack.setAmount(iron);
                ItemStack gold_stack = ToolClass.Gold.clone();
                gold_stack.setAmount(gold);
                world.dropItemNaturally(e.getEntity().getLocation(),iron_stack);
                world.dropItemNaturally(e.getEntity().getLocation(),gold_stack);
            }
        }
    }

}
