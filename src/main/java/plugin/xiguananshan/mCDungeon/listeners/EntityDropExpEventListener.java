package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDropExpEventListener implements Listener {
    @EventHandler
    public void run(EntityDeathEvent e){
        e.setDroppedExp(0);
        e.getDrops().clear();
    }
}
