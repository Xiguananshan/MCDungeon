package plugin.xiguananshan.mCDungeon.ticks;


import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;


public class WindHoleService extends BukkitRunnable {
    private final ArmorStand display;
    private int ticks;
    private float yaw;
    public WindHoleService(ArmorStand entity){
        display = entity;
        ticks = 200;
        yaw = 0;
    }

    @Override
    public void run() {
        if(!display.isValid()){
            cancel();
            return;
        }
        ticks--;
        Location core = display.getLocation().clone().add(0,1.8,0);
        yaw = (yaw + 5) % 360f;
        display.setRotation(yaw, display.getLocation().getPitch());
        display.setHeadPose(new EulerAngle(0, Math.toRadians(yaw), 0));
        World world = display.getWorld();
        for(double a = 0;a<=Math.PI;a+=0.2){
            for(double b = 0;b<=2*Math.PI;b+=0.2){
                Location goal = core.clone().add(2*Math.sin(a)*Math.cos(b),2*Math.sin(a)*Math.sin(b),2*Math.cos(a));
                world.spawnParticle(Particle.DUST,goal,1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.AIR),1));
            }
        }

        Collection<Entity> entities = world.getNearbyEntities(core,5,5,5);
        for(Entity entity : entities){
            if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                if(entity.getLocation().distanceSquared(core) <= 30){
                    Vector dire = core.clone().subtract(entity.getLocation()).toVector().normalize();
                    entity.setVelocity(entity.getVelocity().clone().add(dire.multiply(3f/20f)));
                }
                if(entity.getLocation().distanceSquared(core) <= 4 && ticks%10==0){
                    if(entity instanceof LivingEntity l){
                        ToolClass.typeMap.put(entity.getUniqueId(),DamageType.AIR);
                        l.damage(40);
                    }
                }
            }
        }
        if(ticks<=0){
            display.remove();
            cancel();
        }

    }
}
