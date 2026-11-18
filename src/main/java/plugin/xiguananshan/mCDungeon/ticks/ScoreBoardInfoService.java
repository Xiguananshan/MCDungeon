package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class ScoreBoardInfoService extends BukkitRunnable {
    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            Scoreboard scoreboard = ToolClass.scoreboardMap.get(p.getUniqueId());
            if(scoreboard == null) continue;
            Objective info_main = scoreboard.getObjective("info_main");
            if(info_main == null) continue;
            if(ToolClass.game_players.contains(p.getUniqueId())){
                Score lv,la,defence;
                Float v,a;
                Integer d;
                v = p.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("lv"), PersistentDataType.FLOAT);
                a = p.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("la"),PersistentDataType.FLOAT);
                d = ToolClass.defence.get(p.getUniqueId());
                if(v != null && a != null && d != null){
                    lv = info_main.getScore("lv");
                    la = info_main.getScore("la");
                    defence = info_main.getScore("defence");
                    lv.customName(Component.text("暴击率：", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(String.format("%.2f",v),NamedTextColor.GREEN,TextDecoration.BOLD)).append(Component.text("%",NamedTextColor.GREEN,TextDecoration.BOLD)));
                    la.customName(Component.text("暴击伤害：", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(String.format("%.2f",a),NamedTextColor.AQUA,TextDecoration.BOLD)).append(Component.text("%",NamedTextColor.AQUA,TextDecoration.BOLD)));
                    defence.customName(Component.text("伤害抗性：", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(d,NamedTextColor.GRAY,TextDecoration.BOLD)));
                    info_main.setDisplaySlot(DisplaySlot.SIDEBAR);

                }
            }else{
                info_main.setDisplaySlot(null);
            }
        }
    }
}
