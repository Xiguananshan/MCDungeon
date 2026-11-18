package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class PlayerHealEventListener implements Listener {
    @EventHandler
    public void run(EntityRegainHealthEvent e){
        if(e.getEntity() instanceof Player p){
            if(e.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED){
                if(ToolClass.game_players.contains(p.getUniqueId())){
                    e.setCancelled(true);
                }
            }
        }
    }
}
