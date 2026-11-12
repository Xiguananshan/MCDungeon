package plugin.xiguananshan.mCDungeon.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.Comparator;
import java.util.EnumSet;

public class FriendAttackGoal implements Goal<Mob> {
    private static final double RANGE = 16;
    private static final double RANGE_Y = 8;

    private final Mob mob;

    public FriendAttackGoal(Mob mob){
        this.mob = mob;
    }

    @Override
    public boolean shouldActivate() {
        if (mob.getTarget() != null) return true;
        return findNearestEnemy() != null;
    }

    @Override
    public void start() {
        LivingEntity target = findNearestEnemy();
        if (target != null) mob.setTarget(target);
    }

    @Override
    public void tick() {
        mob.setTarget(findNearestEnemy());
        if (!isEnemy(mob.getTarget())) {
            mob.setTarget(findNearestEnemy());
        }
    }

    @Override
    public void stop() {
        mob.setTarget(null);
    }

    private LivingEntity findNearestEnemy(){
        Location loc = mob.getLocation();
        return mob.getWorld().getNearbyEntities(loc, RANGE, RANGE_Y, RANGE).stream()
                .filter(e -> e instanceof LivingEntity)
                .map(e -> (LivingEntity) e)
                .filter(this::isEnemy)
                .min(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(loc)))
                .orElse(null);
    }

    private boolean isEnemy(LivingEntity entity){
        if (entity == null || entity == mob) return false;
        if (!entity.isValid() || entity.isDead()) return false;
        if (entity instanceof Player) return false;

        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(ToolClass.fromNamespaceKey("entity"), PersistentDataType.BOOLEAN)
                && !pdc.has(ToolClass.fromNamespaceKey("friend"), PersistentDataType.BOOLEAN);
    }

    @Override
    public @NonNull GoalKey<Mob> getKey() {
        return GoalKey.of(Mob.class, ToolClass.fromNamespaceKey("friend_attack_finder"));
    }

    @Override
    public @NonNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.TARGET);
    }
}
