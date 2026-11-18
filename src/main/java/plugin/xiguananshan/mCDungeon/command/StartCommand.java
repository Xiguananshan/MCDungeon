package plugin.xiguananshan.mCDungeon.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p){
            if(!p.isOp()){
                p.sendMessage(Component.text("你不是管理员", NamedTextColor.RED));
                return false;
            }
            for(Player player : Bukkit.getOnlinePlayers()){
                ToolClass.reset(player);
                player.teleport(new Location(player.getWorld(),0.7f,1f,-2f));
                ToolClass.game_players.add(player.getUniqueId());
                player.getInventory().clear();
            }

            ToolClass.Start();

        }
        return false;
    }
}
