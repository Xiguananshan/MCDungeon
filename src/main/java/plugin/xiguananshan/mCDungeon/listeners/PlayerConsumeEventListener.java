package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.DataModify;
import plugin.xiguananshan.mCDungeon.ticks.DefenceModifyService;
import plugin.xiguananshan.mCDungeon.ticks.DataModifierService;

public class PlayerConsumeEventListener implements Listener {
    @EventHandler
    public void run(PlayerItemConsumeEvent e){
        ItemMeta meta = e.getItem().getItemMeta();
        if(meta != null){
            if(meta.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("superman_potion"))){
                e.setCancelled(true);
                if(e.getHand() == EquipmentSlot.HAND){
                    ItemStack stack = e.getPlayer().getInventory().getItemInMainHand();
                    if(stack.getAmount() > 1){
                        stack.setAmount(stack.getAmount()-1);
                        e.getPlayer().getInventory().setItemInMainHand(stack);
                    }else{
                        e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    }
                }
                if(e.getHand() == EquipmentSlot.OFF_HAND){
                    ItemStack stack = e.getPlayer().getInventory().getItemInOffHand();
                    if(stack.getAmount() > 1){
                        stack.setAmount(stack.getAmount()-1);
                        e.getPlayer().getInventory().setItemInOffHand(stack);
                    }else{
                        e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    }
                }
                DataModifierService.AddModify(new DataModify(DataModify.Modifier.ADD,1200,48.5f,"lv",e.getPlayer().getUniqueId()));
                DataModifierService.AddModify(new DataModify(DataModify.Modifier.ADD,1200,60f,"la",e.getPlayer().getUniqueId()));
                DefenceModifyService.AddModify(e.getPlayer().getUniqueId(),450,1200);
                ToolClass.super_man_ticks.put(e.getPlayer().getUniqueId(),1200);
            }
            if(meta.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("superman_potion_pro"))){
                e.setCancelled(true);
                if(e.getHand() == EquipmentSlot.HAND){
                    ItemStack stack = e.getPlayer().getInventory().getItemInMainHand();
                    if(stack.getAmount() > 1){
                        stack.setAmount(stack.getAmount()-1);
                        e.getPlayer().getInventory().setItemInMainHand(stack);
                    }else{
                        e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    }
                }
                if(e.getHand() == EquipmentSlot.OFF_HAND){
                    ItemStack stack = e.getPlayer().getInventory().getItemInOffHand();
                    if(stack.getAmount() > 1){
                        stack.setAmount(stack.getAmount()-1);
                        e.getPlayer().getInventory().setItemInOffHand(stack);
                    }else{
                        e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    }
                }
                DataModifierService.AddModify(new DataModify(DataModify.Modifier.ADD,1200,56.6f,"lv",e.getPlayer().getUniqueId()));
                DataModifierService.AddModify(new DataModify(DataModify.Modifier.ADD,1200,72.5f,"la",e.getPlayer().getUniqueId()));
                DefenceModifyService.AddModify(e.getPlayer().getUniqueId(),600,1200);
            }
        }
    }
}
