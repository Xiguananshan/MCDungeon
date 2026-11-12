package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class PhysicSwordSkillService extends BukkitRunnable {
    private final ItemDisplay display;
    private int ticks;
    private Location impact;
    private double a;
    private final HashSet<UUID> attacks;
    public PhysicSwordSkillService(ItemDisplay display){
        this.display = display;
        this.ticks = 45;
        this.a = 0;
        this.attacks = new HashSet<>();
    }
    @Override
    public void run() {
        if(display.isValid()){
            ticks--;
            Transformation t = display.getTransformation();

            t.getLeftRotation().rotateAxis((float) Math.toRadians(5),new Vector3f(0,1,0));
            display.setTransformation(t);
            if(ticks<=0){
                Location loc = display.getLocation();
                loc.subtract(0,0.3,0);
                display.teleport(loc);


                display.setTransformation(t);
                World world = display.getWorld();
                RayTraceResult hit = world.rayTraceBlocks(
                        loc,
                        new Vector(0,-1,0),
                        0.8f,
                        FluidCollisionMode.NEVER,
                        true

                );
                if(hit != null && hit.getHitBlock() != null){
                    impact = hit.getHitPosition().toLocation(world);
                    display.teleport(impact.clone().add(0,0.1,0));
                    world.playSound(impact, Sound.ENTITY_GENERIC_EXPLODE,4,1);
                    world.spawnParticle(Particle.EXPLOSION,impact,10);
                    display.remove();
                }

            }
        }else{
            if(impact != null){
                this.a+=0.2f;
                World world = impact.getWorld();
                for(double x = 0;x<=Math.PI*2;x+=0.2){
                    world.spawnParticle(Particle.DUST,impact.clone().add(a*Math.sin(x),0,a*Math.cos(x)),1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.PHYSIC),2));
                }
                Collection<Entity> entities = world.getNearbyEntities(impact,8,8,8);
                for(Entity entity : entities){
                    if(entity instanceof LivingEntity l){
                        if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            if(l.getLocation().clone().subtract(impact).lengthSquared() - a*a <= 0.2){
                                if(!attacks.contains(l.getUniqueId())){
                                    ToolClass.typeMap.put(l.getUniqueId(),DamageType.PHYSIC);
                                    l.damage(1500);
                                    attacks.add(l.getUniqueId());
                                }
                            }
                        }
                    }
                }
                if(a>=8){
                    cancel();
                }
            }
        }
    }
}
