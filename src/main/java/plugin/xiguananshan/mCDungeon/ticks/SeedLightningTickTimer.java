package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;


public class SeedLightningTickTimer extends BukkitRunnable {
    private final Vector v;
    private Location seed;
    private final LivingEntity goal;
    private final World world;
    public int damage;
    private int ticks;
    public SeedLightningTickTimer(Location seed,LivingEntity goal,World world,int damage){
        this.seed = seed;
        this.goal = goal;
        this.world = world;
        this.v = new Vector(0,5,0);
        this.damage = damage;
        ticks = 200;
    }
    public void run(){
        if(goal.isDead()){
            cancel();
            return;
        }
        ticks--;
        if(ticks<=0){
            cancel();
            return;
        }
        seed = seed.add(v.clone().multiply(1/20f));
        Vector force = goal.getLocation().clone().subtract(seed).toVector().normalize().multiply(20);
        v.add(force.multiply(1/20f));
        Collection<Entity> entities = world.getNearbyEntities(seed,0.8,0.8,0.8);
        world.spawnParticle(Particle.DUST,seed,3,new Particle.DustOptions(Color.GREEN,1));
        if(entities.contains(goal)){
            for(Entity entity : entities){
                if(entity instanceof  LivingEntity l){
                    if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                        ToolClass.typeMap.put(l.getUniqueId(), DamageType.GRASS);
                        l.damage(damage);
                    }
                }
            }
            ToolClass.typeMap.put(goal.getUniqueId(), DamageType.GRASS);
            goal.damage(damage);
            cancel();
        }

    }
}
