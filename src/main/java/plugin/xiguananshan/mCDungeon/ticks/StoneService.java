package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.List;

public class StoneService extends BukkitRunnable {
    private final Item item;
    private final Vector vec;
    private int ticks;
    private Location lastLocation;
    public StoneService(Item item,Vector vec){
        this.item = item;
        this.vec = vec;
        ticks = 40;
        lastLocation = item.getLocation().clone();
    }
    @Override
    public void run() {
        if(!item.isValid()){
            cancel();
            return;
        }
        item.setGravity(false);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setUnlimitedLifetime(true);
        Location current = item.getLocation();
        if(lastLocation != null){
            Vector segment = current.toVector().subtract(lastLocation.toVector());
            double distance = segment.length();
            if(distance > 0.01){
                RayTraceResult hit = item.getWorld().rayTraceBlocks(
                        lastLocation,
                        segment.normalize(),
                        distance+0.2,
                        FluidCollisionMode.NEVER,
                        true
                );
                if(hit != null && hit.getHitBlock() != null){
                    Location hitLoc = hit.getHitPosition().toLocation(item.getWorld());
                    item.getWorld().spawnParticle(Particle.BLOCK,hitLoc,20,0.3,0.3,0.3,0.1, Bukkit.createBlockData(Material.STONE));
                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_STONE_BREAK,1,1);
                    item.remove();
                    cancel();
                    return;
                }
            }
        }
        lastLocation = current.clone();
        if(item.isOnGround()){

            item.getWorld().spawnParticle(Particle.BLOCK,item.getLocation(),20,0.3,0.3,0.3,0.1, Bukkit.createBlockData(Material.STONE));
            item.getWorld().playSound(item.getLocation(), Sound.BLOCK_STONE_BREAK,1,1);
            item.remove();
            cancel();
            return;
        }

        ticks--;
        if(ticks<=0){
            item.remove();
            cancel();
            return;
        }
        List<Entity> entities = item.getNearbyEntities(1f,1f,1f);
        boolean attacked = false;
        for(Entity entity : entities){
            if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                if(entity instanceof LivingEntity l){
                    ToolClass.typeMap.put(l.getUniqueId(), DamageType.ROCK);
                    attacked = true;
                    l.damage(80);
                    l.setVelocity(vec.normalize().multiply(0.5f).setY(0.3f));
                }
            }
        }
        if(attacked){
            item.getWorld().spawnParticle(Particle.BLOCK,item.getLocation(),20,0.3,0.3,0.3,0.1, Bukkit.createBlockData(Material.STONE));
            item.getWorld().playSound(item.getLocation(), Sound.BLOCK_STONE_BREAK,1,1);
            item.remove();
            cancel();
        }
        item.setVelocity(vec);
    }
}
