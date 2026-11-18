package plugin.xiguananshan.mCDungeon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.xiguananshan.mCDungeon.command.*;
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
        getServer().getPluginManager().registerEvents(new IceBowListener(),this);
        getServer().getPluginManager().registerEvents(new FriendWardenListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerOpenChestEventListener(),this);
        getServer().getPluginManager().registerEvents(new EntityDeadEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerRightClickEntityEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerDiedEventListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerHealEventListener(),this);
        getCommand("test").setExecutor(new TestCommand());
        getCommand("test_gun").setExecutor(new TestGunCommand());
        getCommand("generate").setExecutor(new GenerateTestCommand());
        getCommand("chest").setExecutor(new TestChestCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("start").setExecutor(new StartCommand());
        new ElementTypeTimer().runTaskTimer(this,0,1);
        new FreezeTicksTimer().runTaskTimer(this,0,1);
        new GrassTickTimer().runTaskTimer(this,0,1);
        new BurnTickTImer().runTaskTimer(this,0,1);
        new PhysicAttackTicksTimer().runTaskTimer(this,0,1);
        new EntityDefenceService().runTaskTimer(this,0,1);
        new GrassSwordSkillService().runTaskTimer(this,0,1);
        new FireBookSkillService().runTaskTimer(this,0,1);
        new BowService().runTaskTimer(this,0,1);
        new BowIceSkillService().runTaskTimer(this,0,1);
        new FriendService().runTaskTimer(this,0,1);
        new PlayerGameService().runTaskTimer(this,0,1);
        new SpiderTargetService().runTaskTimer(this,0,10);
        new ScoreBoardInfoService().runTaskTimer(this,0,1);
        new PDCModifierService().runTaskTimer(this,0,1);
        new CompassService().runTaskTimer(this,0,1);
        saveDefaultConfig();
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
