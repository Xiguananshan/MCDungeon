package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.List;
import java.util.UUID;

public class GrassTickTimer extends BukkitRunnable {

    @Override
    public void run() {
        for(UUID uuid : ToolClass.grass_tick.keySet()){
            Item item = (Item) Bukkit.getEntity(uuid);
            if(item != null){
                Integer ticks = ToolClass.grass_tick.get(uuid);
                ticks --;
                ToolClass.grass_tick.replace(uuid,ticks);
                if(ticks<=0){
                    List<Entity> entities = item.getNearbyEntities(1,1,1);
                    for(Entity entity : entities){
                        if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            Integer damage = item.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("grass"), PersistentDataType.INTEGER);
                            if(damage != null){
                                if(entity instanceof LivingEntity l){
                                    ToolClass.typeMap.put(l.getUniqueId(), DamageType.GRASS);
                                    l.damage(damage);
                                }
                            }
                        }
                    }
                    ToolClass.fillCubeWithParticles(item.getWorld(),item.getLocation(),1,20,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.GRASS),3));
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST,1,1);
                    item.remove();
                    ToolClass.grass_tick.remove(uuid);
                    return;
                }
            }else{
                ToolClass.grass_tick.remove(uuid);
                return;
            }
        }
    }
}
