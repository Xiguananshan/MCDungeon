package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;
import java.util.List;

public class WaterBookAttackService extends BukkitRunnable {
    private final Location core;
    private final Player source;
    private int ticks;
    public WaterBookAttackService(Location core,Player source){
        this.core = core;
        this.source = source;
        this.ticks = 60;
    }
    @Override
    public void run() {


        World world = core.getWorld();
        if(ticks % 10 == 0){
            for(double a = 0; a<=3;a+=0.4){
                for(double b = 0;b<=Math.PI*2;b+=0.4){
                    world.spawnParticle(Particle.DUST,core.clone().add(new Vector(a*Math.cos(b),0,a*Math.sin(b))),1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.WATER),1));
                }
            }
        }

        if(ticks%20 == 0){

            Collection<Entity> entities = world.getNearbyEntities(core,3,1,3);
            for(Entity entity :  entities){
                if(entity.getLocation().distanceSquared(core) <= 9){
                    if(entity instanceof LivingEntity l){
                        if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            ToolClass.Damage(l, source, 65, DamageType.WATER);
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
