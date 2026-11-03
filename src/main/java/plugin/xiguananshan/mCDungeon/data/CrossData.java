package plugin.xiguananshan.mCDungeon.data;

import org.bukkit.Location;
import plugin.xiguananshan.mCDungeon.enums.CrossStructureType;

public class CrossData {
    public CrossStructureType type;
    public Location l;
    public CrossData(Location l,CrossStructureType type){
        this.l = l;
        this.type = type;
    }
}
