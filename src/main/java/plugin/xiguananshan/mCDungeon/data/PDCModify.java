package plugin.xiguananshan.mCDungeon.data;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class PDCModify {
    public Modifier modifier;
    public PersistentDataContainer pdc;
    public int ticks;
    public Float value;
    public NamespacedKey key;
    public UUID owner;
    public PDCModify(Modifier modifier,PersistentDataContainer pdc,int ticks,Float value,NamespacedKey key,UUID owner){
        this.modifier = modifier;
        this.pdc = pdc;
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
