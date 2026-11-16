package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.CornerData;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PlayerGameService extends BukkitRunnable {

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            if(ToolClass.gaming){
                if(!ToolClass.game_players.contains(p.getUniqueId())){
                    p.sendActionBar(Component.text("游戏已开始，请等待游戏结束", NamedTextColor.RED));
                }else{
                    double x,y,z;
                    x = p.getX();
                    y = p.getY();
                    z = p.getZ();
                    double margin = 2.0;
                    for(CornerData.Corner corner : ToolClass.EntityRoomData.corners){
                        if(corner.minx + margin <= x && x <= corner.maxx - margin
                                && corner.miny <= y && y <= corner.maxy
                                && corner.minz + margin <= z && z <= corner.maxz - margin){
                            Random random = new Random();
                            Location safe = EntityRoomCheckService.findSafeLocation(p.getWorld(), corner, random);
                            if(safe == null){
                                safe = p.getLocation().clone();
                            }
                            for(UUID uuid : ToolClass.game_players){
                                Player player = Bukkit.getPlayer(uuid);
                                if(player != null){
                                    Title title = Title.title(Component.text("Fight", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                                            Component.text().build(),
                                            Title.Times.times(
                                                    Duration.ofMillis(500),
                                                    Duration.ofMillis(2000),
                                                    Duration.ofMillis(500)
                                            ));
                                    player.showTitle(title);
                                    player.teleport(safe);
                                    player.playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT,3,1);
                                }
                            }
                            new EntityRoomCheckService(corner,random.nextInt(36)+10,p.getWorld(), List.of(Zombie.class, Creeper.class, Spider.class, CaveSpider.class, Witch.class, Vindicator.class)).runTaskTimer(ToolClass.PLUGIN,0,1);
                            ToolClass.EntityRoomData.corners.remove(corner);
                            break;
                        }

                    }
                }
            }else{
                p.sendActionBar(Component.text("等待管理员开始游戏",NamedTextColor.GREEN));
            }
            p.setFoodLevel(20);
            p.setSaturation(20f);

        }
    }
}
