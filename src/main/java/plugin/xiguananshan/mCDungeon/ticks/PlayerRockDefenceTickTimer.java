package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.ArrayList;
import java.util.List;


public class PlayerRockDefenceTickTimer extends BukkitRunnable {
    private final List<RockDefenceState> states;
    private final Player player;

    public PlayerRockDefenceTickTimer(Player player){
        this.player = player;
        states = new ArrayList<>();
    }

    public void AddRockState(int attack,int ticks){
        Integer k = ToolClass.defence.get(this.player.getUniqueId());
        k += (int)Math.ceil(attack*0.8f);
        ToolClass.defence.replace(this.player.getUniqueId(),k);
        RockDefenceState state = new RockDefenceState(attack, ticks);
        states.add(state);

    }

    @Override
    public void run()
    {
        for(RockDefenceState state : states){
            int ticks = state.ticks;
            ticks--;
            state.ticks = ticks;
            if(ticks<=0){
                Integer k = (int)Math.ceil(state.attack*0.8f);
                Integer kk = ToolClass.defence.get(this.player.getUniqueId());
                ToolClass.defence.replace(this.player.getUniqueId(),kk-k);
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
