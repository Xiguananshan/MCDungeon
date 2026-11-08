package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

public class PlayerLightningGunService extends BukkitRunnable {
    private final Player p;
    private final World world;
    public double process;
    public PlayerLightningGunService(Player p){
        this.p = p;
        this.world = p.getWorld();
        this.process = 0;
    }
    @Override
    public void run() {
        ItemStack stack = p.getInventory().getItemInMainHand();
        ItemMeta meta = stack.getItemMeta();
        if(meta!=null){
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if(pdc.has(ToolClass.fromNamespaceKey("lightning_gun")) && !pdc.has(ToolClass.fromNamespaceKey("changing"))){
                if(process<1)
                {
                    process+=0.02;
                }
                Location core = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize().multiply(0.7));
                Vector dir = p.getEyeLocation().getDirection().normalize();
                Vector reference = Math.abs(dir.getY()) > 0.99
                        ? new Vector(1, 0, 0)
                        : new Vector(0, 1, 0);

                Vector u = dir.clone().crossProduct(reference).normalize();
                Vector v = dir.clone().crossProduct(u).normalize();
                for(double a = -1*process*Math.PI;a<=process*Math.PI;a+=0.2){
                    Vector offest = u.clone().multiply(0.8*Math.sin(a)).add(v.clone().multiply(0.8*Math.cos(a)));
                    world.spawnParticle(Particle.DUST,core.clone().add(offest),1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.LIGHTNING),1));
                }



            }else{
                process = 0;
            }
        }else{
            process = 0;
        }
    }
}
