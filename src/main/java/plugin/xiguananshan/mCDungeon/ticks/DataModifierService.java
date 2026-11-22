package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.DataModify;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class DataModifierService extends BukkitRunnable {

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            if(ToolClass.game_players.contains(p.getUniqueId())){
                boolean flag = false;
                for(ItemStack i : p.getInventory()){
                    if(i != null){
                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if(meta.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("goal"))){
                            flag = true;
                            break;
                        }
                    }
                }
                if(flag){
                    AddModify(new DataModify(
                            DataModify.Modifier.ADD,
                            1,
                            7.6f,
                            "lv",
                            p.getUniqueId()));
                }
            }
        }

        Flush();
    }

    public static void AddModify(DataModify modify){
        UUID uuid;
        do{
            uuid = UUID.randomUUID();
        }while (ToolClass.modifyMap.containsKey(uuid));
        ToolClass.modifyMap.put(uuid,modify);
    }

    /**
     * 所有暴击率/暴击伤害数据都在 ToolClass.player_float_data 中，
     * 不再依赖玩家 PDC，因此玩家退出重进不会丢失基础值。
     */
    public static void Flush(){
        HashMap<UUID,HashMap<String,Float>> values = new HashMap<>();
        for(DataModify modify : ToolClass.modifyMap.values()){
            HashMap<String,Float> group = values.computeIfAbsent(modify.owner, k -> new HashMap<>());
            group.putIfAbsent(modify.key, ToolClass.getPlayerFloat(modify.owner, modify.key));
        }

        for(Map.Entry<UUID, DataModify> entry : ToolClass.modifyMap.entrySet()){
            UUID uuid = entry.getKey();
            DataModify modify = entry.getValue();
            HashMap<String,Float> group = values.get(modify.owner);
            if(group == null) continue;

            if (!ToolClass.modified.contains(uuid)) {
                Float k = group.get(modify.key);
                if(k == null) k = 0f;
                if (modify.modifier == DataModify.Modifier.ADD) {
                    k += modify.value;
                }
                if (modify.modifier == DataModify.Modifier.REMOVE) {
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
                if (modify.modifier == DataModify.Modifier.ADD) {
                    k -= modify.value;
                }
                if (modify.modifier == DataModify.Modifier.REMOVE) {
                    k += modify.value;
                }
                group.put(modify.key, k);
            }
        }

        for(DataModify modify : ToolClass.modifyMap.values()){
            HashMap<String,Float> group = values.get(modify.owner);
            if(group == null) continue;
            Float now = group.get(modify.key);
            if(now != null){
                ToolClass.setPlayerFloat(modify.owner,modify.key,now);
            }
        }

        Iterator<Map.Entry<UUID, DataModify>> itr = ToolClass.modifyMap.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<UUID, DataModify> modifyEntry = itr.next();
            if(modifyEntry.getValue().ticks <= 0){
                ToolClass.modified.remove(modifyEntry.getKey());
                itr.remove();
            }
        }
    }
}
