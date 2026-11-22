package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.DefenceModify;

import java.util.*;

public class DefenceModifyService extends BukkitRunnable {
    private static final Map<UUID, List<DefenceModify>> modify_map = new HashMap<>();
    public static void AddModify(UUID uuid,int value,int ticks){
        if(modify_map.containsKey(uuid)){
            modify_map.get(uuid).add(new DefenceModify(ticks,value));
        }else{
            List<DefenceModify> modifies = new ArrayList<>();
            modifies.add(new DefenceModify(ticks,value));
            modify_map.put(uuid,modifies);
        }
    }
    @Override
    public void run() {
        for(Map.Entry<UUID,List<DefenceModify>> entry : modify_map.entrySet()){
            Iterator<DefenceModify> itr = entry.getValue().iterator();
            while(itr.hasNext()){
                DefenceModify modify = itr.next();
                if(!modify.added){
                    ToolClass.defence.compute(entry.getKey(),(_,v) -> {
                        if(v!=null){
                            return v + modify.value;
                        }else{
                            return null;
                        }
                    });
                    modify.added = true;
                }
                modify.ticks--;
                if(modify.ticks <=0){
                    ToolClass.defence.compute(entry.getKey(),(_,v) -> {
                        if(v!=null){
                            return v - modify.value;
                        }else{
                            return null;
                        }
                    });
                    itr.remove();
                }
            }
        }
    }
}
