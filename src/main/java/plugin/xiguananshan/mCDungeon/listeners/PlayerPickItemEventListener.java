package plugin.xiguananshan.mCDungeon.listeners;


import net.kyori.adventure.text.Component;

import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;


public class PlayerPickItemEventListener implements Listener {
    @EventHandler
    public void run(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player p){
            Item item = e.getItem();
            if(item.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("rock"))){
                Integer k = item.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("rock"), PersistentDataType.INTEGER);
                if(k!= null){
                    ToolClass.rockDefenceTickTimerMap.get(p.getUniqueId()).AddRockState(k,200);
                    p.sendMessage(Component.text("你捡起了个岩结晶"));
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
                    item.remove();
                    e.setCancelled(true);
                }

            }


        }else{
            e.setCancelled(true);
        }
    }
}
