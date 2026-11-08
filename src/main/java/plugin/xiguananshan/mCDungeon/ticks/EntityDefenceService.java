package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.UUID;

public class EntityDefenceService extends BukkitRunnable {

    @Override
    public void run() {

        for(UUID uuid : ToolClass.entity_defence.keySet()){
            Entity entity = Bukkit.getEntity(uuid);
            if(entity == null){
                ToolClass.entity_defence.remove(uuid);
                return;
            }
        }
        for(UUID uuid : ToolClass.sword_tick.keySet()){
            if(Bukkit.getEntity(uuid) == null){
                ToolClass.sword_tick.remove(uuid);
                return;
            }
        }
    }
}
