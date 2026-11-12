package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerOpenChestEventListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void run(PlayerInteractEvent e){
        if(!e.getAction().isRightClick() || e.getHand() != EquipmentSlot.HAND) return;
        if(e.getClickedBlock() == null) return;
        if(!(e.getClickedBlock().getState() instanceof Chest chest)) return;
        if(!chest.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("box"))) return;

        chest.getPersistentDataContainer().remove(ToolClass.fromNamespaceKey("box"));
        Inventory inventory = chest.getSnapshotInventory();
        inventory.clear();
        List<ItemStack> loot = new ArrayList<>();
        for(Map.Entry<ItemStack, Float> entry : ToolClass.itemPool.entrySet()){
            if(ThreadLocalRandom.current().nextFloat() < entry.getValue()){
                loot.add(entry.getKey());
            }
        }
        if(loot.isEmpty()){
            loot.add(ToolClass.Null);
        }


        List<Integer> slots = new ArrayList<>();
        for(int i = 0; i < inventory.getSize(); i++){
            slots.add(i);
        }
        Collections.shuffle(slots);
        for(int i = 0; i < loot.size() && i < slots.size(); i++){
            inventory.setItem(slots.get(i), ToolClass.fromItem(loot.get(i)));
        }

        chest.update(true);
    }
}
