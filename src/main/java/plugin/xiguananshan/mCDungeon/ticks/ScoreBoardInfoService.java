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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoardInfoService extends BukkitRunnable {
    private final Map<UUID,Float> lastLv = new HashMap<>();
    private final Map<UUID,Float> lastLa = new HashMap<>();
    private final Map<UUID,Integer> lastDefence = new HashMap<>();

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            Scoreboard scoreboard = ToolClass.scoreboardMap.get(p.getUniqueId());
            if(scoreboard == null) continue;
            Objective info_main = scoreboard.getObjective("info_main");
            if(info_main == null) continue;

            boolean inGame = ToolClass.game_players.contains(p.getUniqueId());
            DisplaySlot desired = inGame ? DisplaySlot.SIDEBAR : null;
            if(info_main.getDisplaySlot() != desired){
                info_main.setDisplaySlot(desired);
            }
            if(!inGame){
                continue;
            }

            Float v = ToolClass.getPlayerFloat(p.getUniqueId(),"lv");
            Float a = ToolClass.getPlayerFloat(p.getUniqueId(),"la");
            Integer d = ToolClass.defence.get(p.getUniqueId());
            if(v == null || a == null || d == null){
                continue;
            }

            UUID uuid = p.getUniqueId();
            Float oldLv = lastLv.get(uuid);
            Float oldLa = lastLa.get(uuid);
            Integer oldDefence = lastDefence.get(uuid);

            if(oldLv == null || Float.compare(oldLv, v) != 0){
                Score score = info_main.getScore("lv");
                score.customName(Component.text("暴击率：", NamedTextColor.YELLOW, TextDecoration.BOLD)
                        .append(Component.text(String.format("%.2f", v), NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text("%", NamedTextColor.GREEN, TextDecoration.BOLD)));
                lastLv.put(uuid, v);
            }

            if(oldLa == null || Float.compare(oldLa, a) != 0){
                Score score = info_main.getScore("la");
                score.customName(Component.text("暴击伤害：", NamedTextColor.YELLOW, TextDecoration.BOLD)
                        .append(Component.text(String.format("%.2f", a), NamedTextColor.AQUA, TextDecoration.BOLD))
                        .append(Component.text("%", NamedTextColor.AQUA, TextDecoration.BOLD)));
                lastLa.put(uuid, a);
            }

            if(oldDefence == null || !oldDefence.equals(d)){
                Score score = info_main.getScore("defence");
                score.customName(Component.text("伤害抗性：", NamedTextColor.YELLOW, TextDecoration.BOLD)
                        .append(Component.text(d, NamedTextColor.GRAY, TextDecoration.BOLD)));
                lastDefence.put(uuid, d);
            }
        }
    }
}
