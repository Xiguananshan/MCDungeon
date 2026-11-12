package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.UUID;

public class BowIceSkillService extends BukkitRunnable {

    @Override
    public void run() {
        for(UUID uuid : ToolClass.IceBowSkillTick.keySet()){
            Integer k = ToolClass.IceBowSkillTick.get(uuid);
            if(k<=0){
                ToolClass.IceBowSkillTick.remove(uuid);
                return;
            }
            k--;
            ToolClass.IceBowSkillTick.put(uuid,k);
        }
    }
}
