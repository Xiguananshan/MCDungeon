package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.CornerData;

public class BossRoomLabelService extends BukkitRunnable {
    private final TextDisplay text;
    private final CornerData.Corner corner;
    public BossRoomLabelService(TextDisplay text, CornerData.Corner corner){
        this.text = text;
        this.corner = corner;
    }
    @Override
    public void run() {
        Component text = Component.text("Boss房",NamedTextColor.GOLD)
                .appendNewline()
                .append(Component.text("必须探索所有刷怪房间才能打开",NamedTextColor.YELLOW))
                .appendNewline()
                .append(Component.text("当前剩余未探索刷怪房间：", NamedTextColor.YELLOW))
                .append(Component.text(ToolClass.EntityRoomData.corners.size(),NamedTextColor.GREEN));

        this.text.text(text);
        this.text.setBillboard(Display.Billboard.CENTER);
        this.text.setLineWidth(300);
        this.text.setShadowed(true);
        this.text.setSeeThrough(false);
        this.text.setDefaultBackground(false);
        this.text.setGravity(false);
        this.text.setInvulnerable(true);
        this.text.setPersistent(true);
        if(ToolClass.EntityRoomData.corners.isEmpty()){
            World world = this.text.getWorld();
            for(int x = corner.minx;x<=corner.maxx;x++){
                for(int y = corner.miny;y<=corner.maxy;y++){
                    for(int z = corner.minz;z<=corner.maxz;z++){
                        world.getBlockAt(x,y,z).setType(Material.AIR);
                    }
                }
            }
            this.text.remove();
            cancel();
        }

    }
}
