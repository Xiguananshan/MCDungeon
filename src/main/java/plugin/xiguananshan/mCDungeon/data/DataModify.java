package plugin.xiguananshan.mCDungeon.data;

import java.util.UUID;

public class DataModify {
    public Modifier modifier;
    public int ticks;
    public Float value;
    public String key;
    public UUID owner;
    public DataModify(Modifier modifier, int ticks, Float value, String key, UUID owner){
        this.modifier = modifier;
        this.ticks = ticks;
        this.value = value;
        this.key = key;
        this.owner = owner;
    }

    public enum Modifier{
        ADD,
        REMOVE
    }
}
