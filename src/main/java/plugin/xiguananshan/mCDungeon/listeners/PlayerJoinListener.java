package plugin.xiguananshan.mCDungeon.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.ticks.PlayerGunService;
import plugin.xiguananshan.mCDungeon.ticks.PlayerLightningGunService;
import plugin.xiguananshan.mCDungeon.ticks.PlayerRockDefenceTickTimer;
import plugin.xiguananshan.mCDungeon.ticks.PlayerSkillService;

import java.time.Duration;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void run(PlayerJoinEvent e){
        if(!ToolClass.defence.containsKey(e.getPlayer().getUniqueId())){
            ToolClass.defence.put(e.getPlayer().getUniqueId(),0);
        }
        if(!ToolClass.rockDefenceTickTimerMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerRockDefenceTickTimer  timer = new PlayerRockDefenceTickTimer(e.getPlayer().getUniqueId());
            timer.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.rockDefenceTickTimerMap.put(e.getPlayer().getUniqueId(),timer);
        }
        if(!ToolClass.gunServiceMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerGunService service = new PlayerGunService(e.getPlayer().getUniqueId());
            service.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.gunServiceMap.put(e.getPlayer().getUniqueId(),service);
        }
        if(!ToolClass.skillServiceMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerSkillService service = new PlayerSkillService(e.getPlayer().getUniqueId());
            service.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.skillServiceMap.put(e.getPlayer().getUniqueId(),service);
        }
        if(!ToolClass.lightningGunServiceMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerLightningGunService service = new PlayerLightningGunService(e.getPlayer().getUniqueId());
            service.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.lightningGunServiceMap.put(e.getPlayer().getUniqueId(),service);
        }
        if(!e.getPlayer().getPersistentDataContainer().has(ToolClass.fromNamespaceKey("lv")) || !e.getPlayer().getPersistentDataContainer().has(ToolClass.fromNamespaceKey("la"))){
            e.getPlayer().getPersistentDataContainer().set(ToolClass.fromNamespaceKey("lv"), PersistentDataType.FLOAT,5f);
            e.getPlayer().getPersistentDataContainer().set(ToolClass.fromNamespaceKey("la"), PersistentDataType.FLOAT,50f);
        }
        if(!ToolClass.scoreboardMap.containsKey(e.getPlayer().getUniqueId())){
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective info_main = scoreboard.registerNewObjective("info_main", Criteria.DUMMY,Component.text("玩家信息",NamedTextColor.BLUE));
            info_main.getScore("lv").setScore(2);
            info_main.getScore("la").setScore(1);
            info_main.getScore("defence").setScore(0);
            ToolClass.scoreboardMap.put(e.getPlayer().getUniqueId(),scoreboard);
            e.getPlayer().setScoreboard(scoreboard);
        }else{
            Scoreboard scoreboard = ToolClass.scoreboardMap.get(e.getPlayer().getUniqueId());
            e.getPlayer().setScoreboard(scoreboard);
        }
        if(!ToolClass.game_players.contains(e.getPlayer().getUniqueId())){
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(),0,-60,0));
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().getInventory().clear();
            Title title = Title.title(Component.text("MCDungeon Beta", NamedTextColor.GREEN, TextDecoration.BOLD),
                    Component.text("玩得开心",NamedTextColor.YELLOW),
                    Title.Times.times(
                            Duration.ofMillis(500),
                            Duration.ofMillis(2000),
                            Duration.ofMillis(500)
                    ));
            e.getPlayer().showTitle(title);
        }
        e.getPlayer().sendMessage(Component.text("输入/settings可以更改游戏设置", NamedTextColor.YELLOW));
        if(ToolClass.un_relief_players.containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().teleport(ToolClass.un_relief_players.get(e.getPlayer().getUniqueId()));
            ToolClass.un_relief_players.remove(e.getPlayer().getUniqueId());
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().clearActivePotionEffects();
        }
    }
}
