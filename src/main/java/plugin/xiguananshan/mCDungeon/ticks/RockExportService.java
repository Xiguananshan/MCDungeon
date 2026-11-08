package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;
import java.util.HashSet;

import java.util.UUID;

public class RockExportService extends BukkitRunnable {
    private final Location core;
    private final World world;
    private double a;
    private final HashSet<UUID> hurted;
    public RockExportService(World world,Location core){
        this.world = world;
        this.core = core;
        a = 0;
        hurted = new HashSet<>();
        world.playSound(core, Sound.BLOCK_ENCHANTMENT_TABLE_USE,1,1);
    }

    @Override
    public void run() {
        a+=0.2;
        for(double x = 0;x<=2*Math.PI;x+=0.1){
            world.spawnParticle(Particle.DUST,core.clone().add(a*Math.cos(x),0,a*Math.sin(x)),1,new Particle.DustOptions(Color.YELLOW,1));
        }
        Collection<Entity> entities = world.getNearbyEntities(core,4,4,4);
        for(Entity entity : entities){
            if(entity instanceof LivingEntity l){
                if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                    if(l.getLocation().distanceSquared(core) - a <= 0.1 && !hurted.contains(l.getUniqueId())){
                        ToolClass.typeMap.put(l.getUniqueId(), DamageType.ROCK);
                        l.damage(100);
                        hurted.add(l.getUniqueId());
                    }
                }
            }
        }
        if(a>=4){
            cancel();
        }
    }
}
