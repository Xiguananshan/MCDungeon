package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.bossbar.BossBar;
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
                if(ToolClass.died_players.size() >= ToolClass.game_players.size()){
                    ToolClass.gaming = false;

                    for(UUID uuid : ToolClass.game_players){
                        Player player = Bukkit.getPlayer(uuid);
                        if(player == null) continue;
                        player.teleport(new Location(player.getWorld(),0,-60,0));
                        player.setGameMode(GameMode.ADVENTURE);
                        Title title = Title.title(Component.text("Defeat", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                                Component.text("死于   第",NamedTextColor.YELLOW).append(Component.text(ToolClass.round,NamedTextColor.GREEN)).append(Component.text("层",NamedTextColor.YELLOW)),
                                Title.Times.times(
                                        Duration.ofMillis(500),
                                        Duration.ofMillis(2000),
                                        Duration.ofMillis(500)
                                ));
                        player.showTitle(title);

                        player.getInventory().clear();
                        ToolClass.reset(player);
                    }
                    ToolClass.game_players.clear();
                }
                if(!ToolClass.game_players.contains(p.getUniqueId())){
                    p.sendActionBar(Component.text("游戏已开始，请等待游戏结束", NamedTextColor.RED));
                }else{
                    if(p.getGameMode() != GameMode.ADVENTURE){
                        continue;
                    }
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
                            new EntityRoomCheckService(corner,random.nextInt(8)+10,p.getWorld(), List.of(Zombie.class, Creeper.class, Spider.class, CaveSpider.class, Witch.class, Vindicator.class)).runTaskTimer(ToolClass.PLUGIN,0,1);
                            ToolClass.EntityRoomData.corners.remove(corner);
                            break;
                        }

                    }
                    if(ToolClass.BossRoom != null){
                        if(ToolClass.BossRoom.minx + margin <= x && x <= ToolClass.BossRoom.maxx - margin
                                && ToolClass.BossRoom.miny <= y && y <= ToolClass.BossRoom.maxy
                                && ToolClass.BossRoom.minz + margin <= z && z <= ToolClass.BossRoom.maxz - margin){
                            Random random = new Random();
                            Location safe = EntityRoomCheckService.findSafeLocation(p.getWorld(), ToolClass.BossRoom, random);
                            if(safe == null){
                                safe = p.getLocation().clone();
                            }
                            for(UUID uuid : ToolClass.game_players){
                                Player player = Bukkit.getPlayer(uuid);
                                if(player != null){
                                    /*
                                    Title title = Title.title(Component.text("Fight", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                                            Component.text("ZombieKing", NamedTextColor.GOLD, TextDecoration.BOLD),
                                            Title.Times.times(
                                                    Duration.ofMillis(500),
                                                    Duration.ofMillis(2000),
                                                    Duration.ofMillis(500)
                                            ));
                                    player.showTitle(title);
                                    player.teleport(safe);
                                    player.playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT,3,1);
                                    */
                                    Title title = Title.title(Component.text("Fight", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                                            Component.text("CommandBlock", NamedTextColor.GOLD, TextDecoration.BOLD),
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
                            /*
                            EntityRoomCheckService.setAllDoors(Material.OAK_FENCE, p.getWorld());
                            Mob mob = ToolClass.SummonBoss(1,new Location(p.getWorld(),ToolClass.BossRoom.minx + 14,ToolClass.BossRoom.miny+1,ToolClass.BossRoom.minz+14));
                            assert mob != null;
                            new BossRoomService(mob).runTaskTimer(ToolClass.PLUGIN,0,1);
                            new BossBarService(mob,Component.text("ZombieKing", NamedTextColor.GOLD), BossBar.Color.RED).runTaskTimer(ToolClass.PLUGIN,0,1);
                            if(ToolClass.BossDoor != null){
                                for(int X = ToolClass.BossDoor.minx;X<=ToolClass.BossDoor.maxx;X++){
                                    for(int Y = ToolClass.BossDoor.miny;Y<=ToolClass.BossDoor.maxy;Y++){
                                        for(int Z = ToolClass.BossDoor.minz;Z <= ToolClass.BossDoor.maxz;Z++){
                                            p.getWorld().getBlockAt(X,Y,Z).setType(Material.DARK_OAK_FENCE,true);
                                        }
                                    }
                                }
                            }

                             */
                            EntityRoomCheckService.setAllDoors(Material.OAK_FENCE, p.getWorld());
                            Mob mob = ToolClass.SummonBoss(2,new Location(p.getWorld(),ToolClass.BossRoom.minx + 14,ToolClass.BossRoom.miny+1,ToolClass.BossRoom.minz+14));
                            assert mob != null;
                            new BossRoomService(mob).runTaskTimer(ToolClass.PLUGIN,0,1);
                            new BossBarService(mob,Component.text("CommandBlock", NamedTextColor.GOLD), BossBar.Color.RED).runTaskTimer(ToolClass.PLUGIN,0,1);
                            if(ToolClass.BossDoor != null){
                                for(int X = ToolClass.BossDoor.minx;X<=ToolClass.BossDoor.maxx;X++){
                                    for(int Y = ToolClass.BossDoor.miny;Y<=ToolClass.BossDoor.maxy;Y++){
                                        for(int Z = ToolClass.BossDoor.minz;Z <= ToolClass.BossDoor.maxz;Z++){
                                            p.getWorld().getBlockAt(X,Y,Z).setType(Material.DARK_OAK_FENCE,true);
                                        }
                                    }
                                }
                            }
                            ToolClass.BossRoom = null;
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
