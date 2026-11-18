package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.Nullable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.CornerData;

import java.util.UUID;

public class CompassService extends BukkitRunnable {

    @Override
    public void run() {
        for(UUID uuid : ToolClass.game_players){
            Player p = Bukkit.getPlayer(uuid);
            if(p== null) continue;
            ItemStack stack = p.getInventory().getItemInMainHand();
            if(stack.getType() == Material.COMPASS){
                CompassMeta meta = (CompassMeta) stack.getItemMeta();
                if(meta.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("compass"))){
                    meta.setLodestoneTracked(false);
                    World world = Bukkit.getWorld("world");
                    assert world != null;
                    Location goal = getNearestCenterLoc(p,world);
                    if(goal != null && !goal.equals(meta.getLodestone())){
                        meta.setLodestone(goal);
                        stack.setItemMeta(meta);
                        p.getInventory().setItemInMainHand(stack);
                    }
                }

            }
        }
    }
    @Nullable
    public static Location getNearestCenterLoc(Player p, World world){
        if(ToolClass.EntityRoomData.corners.isEmpty()){
            if(ToolClass.BossRoom != null){
                return new Location(world,ToolClass.BossRoom.minx+14,ToolClass.BossRoom.miny+1,ToolClass.BossRoom.minz+14);
            }else{
                return null;
            }
        }else{
            double min = Double.MAX_VALUE;
            Location min_c = null;
            for(CornerData.Corner corner : ToolClass.EntityRoomData.corners){
                Location center = new Location(world,corner.minx+14,corner.miny+1,corner.minz+14);
                if(p.getLocation().distanceSquared(center) < min){
                    min = p.getLocation().distanceSquared(center);
                    min_c = center;
                }
            }
            return min_c;
        }
    }
}
