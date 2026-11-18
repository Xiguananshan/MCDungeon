package plugin.xiguananshan.mCDungeon.ticks;


import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.PDCModify;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PDCModifierService extends BukkitRunnable {

    @Override
    public void run() {

        Flush();
    }

    public static void AddModify(PDCModify modify){
        UUID uuid;
        do{
            uuid = UUID.randomUUID();
        }while (ToolClass.modifyMap.containsKey(uuid));
        ToolClass.modifyMap.put(uuid,modify);
    }

    public static void Flush(){
        HashMap<UUID,HashMap<NamespacedKey,Float>> values = new HashMap<>();
        for(PDCModify modify : ToolClass.modifyMap.values()){
            HashMap<NamespacedKey,Float> group = values.computeIfAbsent(modify.owner, k -> new HashMap<>());
            Float origin = modify.pdc.get(modify.key, PersistentDataType.FLOAT);
            group.putIfAbsent(modify.key, origin != null ? origin : 0f);
        }

        for(Map.Entry<UUID, PDCModify> entry : ToolClass.modifyMap.entrySet()){
            UUID uuid = entry.getKey();
            PDCModify modify = entry.getValue();
            HashMap<NamespacedKey,Float> group = values.get(modify.owner);
            if(group == null) continue;

            if (!ToolClass.modified.contains(uuid)) {
                Float k = group.get(modify.key);
                if(k == null) k = 0f;
                if (modify.modifier == PDCModify.Modifier.ADD) {
                    k += modify.value;
                }
                if (modify.modifier == PDCModify.Modifier.REMOVE) {
                    k -= modify.value;
                }
                group.put(modify.key, k);
                ToolClass.modified.add(uuid);
                continue;
            }

            modify.ticks--;
            if (modify.ticks <= 0) {
                Float k = group.get(modify.key);
                if(k == null) k = 0f;
                if (modify.modifier == PDCModify.Modifier.ADD) {
                    k -= modify.value;
                }
                if (modify.modifier == PDCModify.Modifier.REMOVE) {
                    k += modify.value;
                }
                group.put(modify.key, k);
            }
        }

        for(PDCModify modify : ToolClass.modifyMap.values()){
            HashMap<NamespacedKey,Float> group = values.get(modify.owner);
            if(group == null) continue;
            Float now = group.get(modify.key);
            if(now == null) continue;
            Float origin = modify.pdc.get(modify.key,PersistentDataType.FLOAT);
            if(origin == null || Math.abs(origin - now) > 1e-6f){
                modify.pdc.set(modify.key,PersistentDataType.FLOAT,now);
            }
        }

        Iterator<Map.Entry<UUID,PDCModify>> itr = ToolClass.modifyMap.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<UUID,PDCModify> modifyEntry = itr.next();
            if(modifyEntry.getValue().ticks <= 0){
                ToolClass.modified.remove(modifyEntry.getKey());
                itr.remove();
            }
        }
    }
}
