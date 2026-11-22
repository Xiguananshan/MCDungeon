package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.DataModify;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SuperManTickService extends BukkitRunnable {

    @Override
    public void run() {
        Iterator<Map.Entry<UUID,Integer>> itr = ToolClass.super_man_ticks.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<UUID,Integer> entry = itr.next();
            ToolClass.super_man_ticks.compute(entry.getKey(),(_,v) -> {
                assert v!= null;
                return v-1;
            });
            if(ToolClass.super_man_ticks.get(entry.getKey()) <= 0){
                DataModifierService.AddModify(new DataModify(DataModify.Modifier.REMOVE,400,48.5f,"lv",entry.getKey()));
                DataModifierService.AddModify(new DataModify(DataModify.Modifier.REMOVE,400,60f,"la",entry.getKey()));
                DefenceModifyService.AddModify(entry.getKey(),-450,400);
                itr.remove();
            }
        }
    }
}
