package plugin.xiguananshan.mCDungeon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.xiguananshan.mCDungeon.command.GenerateTestCommand;
import plugin.xiguananshan.mCDungeon.command.TestCommand;
import plugin.xiguananshan.mCDungeon.command.TestGunCommand;
import plugin.xiguananshan.mCDungeon.listeners.*;
import plugin.xiguananshan.mCDungeon.ticks.*;

public final class MCDungeon extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityDamageEventListener(),this);
        getServer().getPluginManager().registerEvents(new EntityDropExpEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerUseItemEventListener(),this);
        getServer().getPluginManager().registerEvents(new GunShootEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerPickItemEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(),this);
        getServer().getPluginManager().registerEvents(new PotionBreakEventListener(),this);
        getCommand("test").setExecutor(new TestCommand());
        getCommand("test_gun").setExecutor(new TestGunCommand());
        getCommand("generate").setExecutor(new GenerateTestCommand());
        new ElementTypeTimer().runTaskTimer(this,0,1);
        new FreezeTicksTimer().runTaskTimer(this,0,1);
        new GrassTickTimer().runTaskTimer(this,0,1);
        new BurnTickTImer().runTaskTimer(this,0,1);
        new PhysicAttackTicksTimer().runTaskTimer(this,0,1);
        new EntityDefenceService().runTaskTimer(this,0,1);
        new GrassSwordSkillService().runTaskTimer(this,0,1);
        new FireBookSkillService().runTaskTimer(this,0,1);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player p : Bukkit.getOnlinePlayers()){
            p.kick(Component.text("插件热重载，请重新进服", NamedTextColor.RED));
        }
    }
}
