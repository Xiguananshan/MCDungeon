package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class LightningSkillService extends BukkitRunnable {
    private final Player p;
    private int ticks;
    private final Vector dir;
    private final Vector right;
    private final World world;
    private final Location core;
    private final HashSet<UUID> uuids;
    public LightningSkillService(Player p){
        this.p = p;
        ticks = 0;
        right = GetRight();
        dir = p.getEyeLocation().getDirection().normalize();
        this.world = p.getWorld();
        this.core = p.getEyeLocation();
        this.uuids = new HashSet<>();
    }
    @Override
    public void run() {
        ticks++;
        double x = (ticks/20f)*15f;
        for(double l = 2.5;l>=-2.5f;l--){
            Location offset = dir.clone().multiply(x).clone().add(right.clone().multiply(l)).toLocation(world);
            world.spawnParticle(Particle.DUST,core.clone().add(offset),1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.LIGHTNING),3));
        }
        Location half = core.clone().add(dir.clone().multiply(x));
        Collection<Entity> entities = world.getNearbyEntities(half,3,0.4,3);
        for(Entity entity : entities){
            if(!uuids.contains(entity.getUniqueId())){
                if(entity instanceof LivingEntity l){
                    if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                        ToolClass.typeMap.put(l.getUniqueId(),DamageType.LIGHTNING);
                        l.damage(1200);
                    }
                }
            }
            uuids.add(entity.getUniqueId());
        }
        if(ticks>=60){
            cancel();
        }
    }

    public Vector GetRight(){
        Vector up = new Vector(0,1,0);
        Vector dir = p.getEyeLocation().getDirection().normalize();
        if(Math.abs(dir.dot(up))>0.99){
            double yaw = Math.toRadians(p.getLocation().getYaw());
            Vector fwdH = new Vector(-Math.sin(yaw),0,Math.cos(yaw));
            return fwdH.crossProduct(up).normalize();
        }
        return dir.clone().crossProduct(up).normalize();
    }
}
