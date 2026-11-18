package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PlayerRockDefenceTickTimer extends BukkitRunnable {
    private final List<RockDefenceState> states;
    private final UUID uuid;

    public PlayerRockDefenceTickTimer(UUID uuid){
        this.uuid = uuid;
        states = new ArrayList<>();
    }

    public void AddRockState(int attack,int ticks){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;
        Integer k = ToolClass.defence.get(player.getUniqueId());
        k += (int)Math.ceil(attack*0.8f);
        ToolClass.defence.replace(player.getUniqueId(),k);
        RockDefenceState state = new RockDefenceState(attack, ticks);
        states.add(state);

    }

    @Override
    public void run()
    {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;
        for(RockDefenceState state : states){
            int ticks = state.ticks;
            ticks--;
            state.ticks = ticks;
            if(ticks<=0){
                Integer k = (int)Math.ceil(state.attack*0.8f);
                Integer kk = ToolClass.defence.get(player.getUniqueId());
                ToolClass.defence.replace(player.getUniqueId(),kk-k);
                states.remove(state);
                return;
            }
        }
    }

    public static class RockDefenceState{
        public int attack;
        public int ticks;
        public RockDefenceState(int attack,int ticks){
            this.attack = attack;
            this.ticks = ticks;
        }
    }
}
