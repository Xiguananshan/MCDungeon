package plugin.xiguananshan.mCDungeon.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jspecify.annotations.NonNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.EnumSet;

public class FriendRandomGoal implements Goal<Mob> {
    private final Mob mob;
    private final double speed;
    public FriendRandomGoal(Mob mob, double speed){
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean shouldActivate() {
        return mob.getTarget() == null || !mob.getTarget().isValid();
    }

    @Override
    public void start() {
        double x = mob.getLocation().getX() + (Math.random() - 0.5)*20;
        double z = mob.getLocation().getZ() + (Math.random() - 0.5)*20;
        double y = mob.getWorld().getHighestBlockYAt((int) x,(int) z)+1;
        mob.getPathfinder().moveTo(new Location(mob.getWorld(),x,y,z),speed);
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();;
    }

    @Override
    public @NonNull GoalKey<Mob> getKey() {
        return GoalKey.of(Mob.class, ToolClass.fromNamespaceKey("friend_random_finder"));
    }

    @Override
    public @NonNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}
