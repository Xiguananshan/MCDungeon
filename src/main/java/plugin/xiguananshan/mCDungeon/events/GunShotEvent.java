package plugin.xiguananshan.mCDungeon.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

public class GunShotEvent extends Event {
    public static final HandlerList HANDLERS = new HandlerList();
    private final int health;
    private final DamageType types;
    private final LivingEntity entity;
    private final boolean executed;
    public GunShotEvent(int health,DamageType types,LivingEntity entity,boolean executed){
        this.health = health;
        this.types = types;
        this.entity = entity;
        this.executed = executed;
    }
    public DamageType getDamageType(){
        return types;
    }
    public int GetHealth(){
        return health;
    }
    public LivingEntity getEntity(){
        return entity;
    }
    public boolean getExecuted(){
        return executed;
    }

    @Override
    @NonNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
