package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.UUID;

public class BurnTickTImer extends BukkitRunnable {
    private int ticks;
    public BurnTickTImer(){ticks = 5;}

    @Override
    public void run() {
        ticks--;
        if(ticks<=0){
            ticks = 5;
            for(UUID uuid : ToolClass.grass_burn.keySet()){
                Entity entity = Bukkit.getEntity(uuid);
                if(entity != null){
                    if(entity instanceof LivingEntity l){
                        if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            l.setFireTicks(10);
                            Integer tk = ToolClass.grass_burn.get(uuid);
                            tk--;
                            ToolClass.typeMap.put(l.getUniqueId(), DamageType.FIRE);
                            l.damage(10);
                            ToolClass.grass_burn.replace(uuid,tk);
                            if(tk<=0){
                                l.setFireTicks(0);
                                ToolClass.grass_burn.remove(uuid);
                                return;
                            }
                        }
                    }
                }else{
                    ToolClass.grass_burn.remove(uuid);
                    return;
                }

            }
        }
    }
}
