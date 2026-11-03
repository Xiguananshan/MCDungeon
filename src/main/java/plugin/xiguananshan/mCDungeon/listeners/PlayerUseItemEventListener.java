package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

public class PlayerUseItemEventListener implements Listener {
    @EventHandler
    public void run(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
            return;
        }
        long now_time = System.currentTimeMillis();
        if(e.getItem() != null){
            PersistentDataContainer pdc = e.getItem().getItemMeta().getPersistentDataContainer();
            DamageType t;
            String ts = pdc.get(ToolClass.fromNamespaceKey("element"), PersistentDataType.STRING);
            try{
                t = DamageType.valueOf(ts);
            }catch (Exception E){
                t = DamageType.PHYSIC;
            }
            if(pdc.has(ToolClass.fromNamespaceKey("normal_gun"))){
                if(ToolClass.Shoot_timer.containsKey(e.getPlayer().getUniqueId())){
                    if(now_time - ToolClass.Shoot_timer.get(e.getPlayer().getUniqueId()) >= 300){

                        ToolClass.Shoot(e.getPlayer().getEyeLocation(),e.getPlayer().getWorld(),10f,35, t);
                        ToolClass.Shoot_timer.put(e.getPlayer().getUniqueId(),now_time);

                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
                    }
                }else{
                    ToolClass.Shoot_timer.put(e.getPlayer().getUniqueId(),now_time);
                    ToolClass.Shoot(e.getPlayer().getEyeLocation(),e.getPlayer().getWorld(),10f,35, t);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
                }
            }

        }

    }
}
