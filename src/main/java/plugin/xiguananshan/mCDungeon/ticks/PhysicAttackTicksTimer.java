package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.UUID;

public class PhysicAttackTicksTimer extends BukkitRunnable {

    @Override
    public void run() {
        for(UUID uuid : ToolClass.physic_attack.keySet()){
            Integer ticks = ToolClass.physic_attack.get(uuid);
            ticks--;
            ToolClass.physic_attack.replace(uuid,ticks);
            if(ticks<=0){
                ToolClass.physic_attack.remove(uuid);
                return;
            }
        }
    }
}
