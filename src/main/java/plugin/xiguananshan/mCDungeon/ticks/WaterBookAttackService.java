package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.SplashPotion;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;
import java.util.List;

public class WaterBookAttackService extends BukkitRunnable {
    private final Location core;
    private int ticks;
    public WaterBookAttackService(Location core){
        this.core = core;
        this.ticks = 60;
    }
    @Override
    public void run() {


        World world = core.getWorld();
        for(double a = 0; a<=3;a+=0.2){
            for(double b = 0;b<=Math.PI*2;b+=0.2){
                world.spawnParticle(Particle.DUST,core.clone().add(new Vector(a*Math.cos(b),0,a*Math.sin(b))),1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.WATER),1));
            }
        }
        if(ticks%20 == 0){

            Collection<Entity> entities = world.getNearbyEntities(core,3,1,3);
            for(Entity entity :  entities){
                if(entity.getLocation().distanceSquared(core) <= 9){
                    if(entity instanceof LivingEntity l){
                        if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            ToolClass.typeMap.put(l.getUniqueId(),DamageType.WATER);
                            l.damage(65);
                        }
                    }
                }
            }
        }
        ticks--;
        if(ticks<=0){
            cancel();
        }

    }
}
