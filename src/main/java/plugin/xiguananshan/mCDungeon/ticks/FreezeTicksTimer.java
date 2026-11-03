package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class FreezeTicksTimer extends BukkitRunnable {

    @Override
    public void run() {
        for(UUID uuid : ToolClass.freeze_tick.keySet()){
            Integer ticks = ToolClass.freeze_tick.get(uuid);
            ticks--;
            ToolClass.freeze_tick.replace(uuid,ticks);
            if(!ToolClass.entity_type.containsKey(uuid)){
                ToolClass.entity_type.put(uuid,new HashSet<>(){{
                    add(DamageType.ICE);
                }});
                ToolClass.element_type.put(uuid,new HashMap<>(){{put(DamageType.ICE,1);}});
            }else{
                ToolClass.entity_type.get(uuid).add(DamageType.ICE);
                ToolClass.element_type.get(uuid).put(DamageType.ICE,1);
            }
            Entity entity = Bukkit.getEntity(uuid);
            if(entity != null){
                if(entity instanceof LivingEntity l){
                    l.setAI(false);
                    l.getWorld().setBlockData(l.getLocation(),Bukkit.createBlockData(Material.BLUE_ICE));
                    l.setVelocity(new Vector(0,0,0));
                }
            }
            if(ticks<=0){
                if(entity != null){
                    if(entity instanceof LivingEntity l){
                        l.setAI(true);
                        if(l.getWorld().getBlockAt(l.getLocation()).getType() == Material.BLUE_ICE){
                            l.getWorld().setBlockData(l.getLocation(),Bukkit.createBlockData(Material.AIR));
                        }
                    }
                }
                ToolClass.freeze_tick.remove(uuid);
                return;
            }
        }
    }
}
