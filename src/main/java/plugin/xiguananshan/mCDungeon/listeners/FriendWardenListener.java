package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class FriendWardenListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onFriendWardenAttack(EntityDamageByEntityEvent e){
        if(!e.getDamager().getPersistentDataContainer().has(ToolClass.fromNamespaceKey("friend"), PersistentDataType.BOOLEAN)) return;
        if(!(e.getEntity() instanceof LivingEntity victim)) return;
        if(!victim.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"), PersistentDataType.BOOLEAN)){
            e.setCancelled(true);
        }
    }
}
