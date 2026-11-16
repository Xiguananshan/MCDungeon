package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
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
        right = GetRight(p);
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
            if(entity instanceof Item){
                if (entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("grass"))) {
                    Collection<Entity> entities1 = entity.getNearbyEntities(8, 8, 8);
                    LivingEntity goal = null;
                    double min = Double.MAX_VALUE;
                    for (Entity entity2 : entities1) {
                        if (entity2 instanceof LivingEntity ll) {
                            if (ll.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))) {
                                if (ll.getLocation().distanceSquared(entity.getLocation()) <= min) {
                                    min = ll.getLocation().distanceSquared(entity.getLocation());
                                    goal = ll;

                                }
                            }
                        }
                    }
                    if (goal != null) {
                        Integer d = entity.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("grass"), PersistentDataType.INTEGER);
                        if (d != null)
                            new SeedLightningTickTimer(entity.getLocation(), goal, goal.getWorld(), d).runTaskTimer(ToolClass.PLUGIN, 0, 1);
                    }
                    ToolClass.fillCubeWithParticles(entity.getWorld(), entity.getLocation(), 0.8, 10, new Particle.DustOptions(Color.GREEN, 3));
                    entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_GRASS_BREAK, 1, 1);
                    entity.remove();
                }
            }
            uuids.add(entity.getUniqueId());
        }
        if(ticks>=60){
            cancel();
        }
    }

    public static Vector GetRight(Player p){
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
