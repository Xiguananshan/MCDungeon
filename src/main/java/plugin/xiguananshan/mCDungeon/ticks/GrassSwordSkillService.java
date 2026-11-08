package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class GrassSwordSkillService extends BukkitRunnable {
    private final Map<UUID, Location> locationMap = new HashMap<>();
    private final Map<UUID, Location> blockMap = new HashMap<>();
    @Override
    public void run() {
        for(UUID uuid : ToolClass.grass_sword_skill_tick.keySet()){
            Entity entity = Bukkit.getEntity(uuid);
            if(entity == null){
                ToolClass.grass_sword_skill_tick.remove(uuid);
                World world = Bukkit.getWorld("world");
                assert world != null;
                if(world.getBlockAt(blockMap.get(uuid)).getType() == Material.OAK_LEAVES)
                    world.getBlockAt(blockMap.get(uuid)).setType(Material.AIR);
                world.spawnParticle(Particle.BLOCK,locationMap.get(uuid),20,Bukkit.createBlockData(Material.OAK_LEAVES));
                world.playSound(locationMap.get(uuid), Sound.BLOCK_LEAF_LITTER_BREAK,1,1);
                locationMap.remove(uuid);
                blockMap.remove(uuid);
                return;
            }
            if(!locationMap.containsKey(entity.getUniqueId())){
                locationMap.put(entity.getUniqueId(),entity.getLocation());
                entity.getWorld().getBlockAt(entity.getLocation().getBlockX(),entity.getLocation().getBlockY(),entity.getLocation().getBlockZ()).setType(Material.OAK_LEAVES);
                blockMap.put(uuid,entity.getLocation());
            }

            Integer ticks =  ToolClass.grass_sword_skill_tick.get(uuid);
            ticks--;
            ToolClass.grass_sword_skill_tick.replace(uuid,ticks);
            if(ticks <= 0){
                ToolClass.grass_sword_skill_tick.remove(uuid);
                entity.getWorld().getBlockAt(blockMap.get(uuid)).setType(Material.AIR);
                if(entity.getWorld().getBlockAt(blockMap.get(uuid)).getType() == Material.OAK_LEAVES)
                    entity.getWorld().spawnParticle(Particle.BLOCK,entity.getLocation(),20,Bukkit.createBlockData(Material.OAK_LEAVES));
                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_LEAF_LITTER_BREAK,1,1);
                Integer gk = ToolClass.entity_defence.get(uuid).get(DamageType.GRASS);
                ToolClass.entity_defence.get(uuid).replace(DamageType.GRASS,gk+240);
                locationMap.remove(uuid);
                blockMap.remove(uuid);
                return;
            }

            entity.teleport(locationMap.get(uuid));
            if(ToolClass.entity_type.containsKey(uuid)){
                ToolClass.entity_type.get(uuid).add(DamageType.GRASS);
                ToolClass.element_type.get(uuid).put(DamageType.GRASS,100);
            }else{
                ToolClass.entity_type.put(uuid,new HashSet<>(){{add(DamageType.GRASS);}});
                ToolClass.element_type.put(uuid,new HashMap<>(){{put(DamageType.GRASS,100);}});
            }

        }
    }
}
