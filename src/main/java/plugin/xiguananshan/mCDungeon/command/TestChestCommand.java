package plugin.xiguananshan.mCDungeon.command;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class TestChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p){
            World world = p.getWorld();
            Component text = Component.text("古老的喷泉", NamedTextColor.GOLD).appendNewline().append(Component.text("往里面扔点金锭说不定会发生点什么",NamedTextColor.GREEN));
            TextDisplay textDisplay = world.spawn(new Location(world,p.getX(),p.getY(),p.getZ()), TextDisplay.class, (e) -> {
                e.text(text);
                e.setBillboard(Display.Billboard.CENTER);
                e.setLineWidth(300);
                e.setShadowed(true);
                e.setSeeThrough(false);
                e.setDefaultBackground(false);
                e.setGravity(false);
                e.setInvulnerable(true);
                e.setPersistent(true);
            });
        }
        return false;
    }
}
