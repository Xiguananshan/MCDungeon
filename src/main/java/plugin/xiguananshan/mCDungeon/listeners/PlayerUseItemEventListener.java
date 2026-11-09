package plugin.xiguananshan.mCDungeon.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;
import plugin.xiguananshan.mCDungeon.ticks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerUseItemEventListener implements Listener {
    @EventHandler
    public void run(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
            if(e.getItem()!=null){
                ItemMeta meta = e.getItem().getItemMeta();
                assert meta != null;
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                if(!pdc.has(ToolClass.fromNamespaceKey("changing")) && pdc.has(ToolClass.fromNamespaceKey("count"))){
                    Integer load = pdc.get(ToolClass.fromNamespaceKey("load_tick"),PersistentDataType.INTEGER);
                    pdc.set(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER,load != null ? load : 100);
                    e.getItem().setItemMeta(meta);
                }
            }
            return;
        }
        long now_time = System.currentTimeMillis();
        if(e.getItem() != null){
            ItemMeta meta = e.getItem().getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            DamageType t;
            String ts = pdc.get(ToolClass.fromNamespaceKey("element"), PersistentDataType.STRING);
            try{
                t = DamageType.valueOf(ts);
            }catch (Exception E){
                t = DamageType.PHYSIC;
            }
            Integer id = pdc.get(ToolClass.fromNamespaceKey("id"),PersistentDataType.INTEGER);
            Integer skill = pdc.get(ToolClass.fromNamespaceKey("skill"),PersistentDataType.INTEGER);
            if(id != null && skill != null){
                if(e.getPlayer().isSneaking()){
                    PlayerSkillService service = ToolClass.skillServiceMap.get(e.getPlayer().getUniqueId());

                    if(service!=null){

                        if(!service.isCold(id)){
                            if(pdc.has(ToolClass.fromNamespaceKey("wind_sword"))){
                                ArmorStand armorStand = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), ArmorStand.class);
                                armorStand.setVisible(false);
                                armorStand.setInvulnerable(true);
                                armorStand.getEquipment().setHelmet(new ItemStack(Material.BLACK_CONCRETE));
                                armorStand.setGravity(false);
                                new WindHoleService(armorStand).runTaskTimer(ToolClass.PLUGIN,0,1);
                                e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_GENERIC_EXPLODE,3,1);
                            }
                            if(pdc.has(ToolClass.fromNamespaceKey("stone_book"))){
                                FallingBlock block = e.getPlayer().getWorld().spawn(
                                        e.getPlayer().getLocation().clone().add(0, 3, 0),
                                        FallingBlock.class,
                                        fb -> {
                                            fb.setBlockData(Bukkit.createBlockData(Material.GOLD_BLOCK));
                                            fb.setDropItem(false);
                                            fb.setHurtEntities(false);
                                        }
                                );
                                StoneSkillService stoneSkillService = new StoneSkillService(block);
                                stoneSkillService.runTaskTimer(ToolClass.PLUGIN,0,1);
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.BLOCK_ANVIL_PLACE,3,1);
                            }
                            if(pdc.has(ToolClass.fromNamespaceKey("lightning_gun"))){
                                LightningSkillService skillService = new LightningSkillService(e.getPlayer());
                                skillService.runTaskTimer(ToolClass.PLUGIN,0,1);
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,3,1);

                            }
                            if(pdc.has(ToolClass.fromNamespaceKey("grass_sword"))){
                                List<Entity> entities = e.getPlayer().getNearbyEntities(4,4,4);
                                for(Entity entity : entities){
                                    if(entity instanceof LivingEntity l){
                                        if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                                            ToolClass.typeMap.put(l.getUniqueId(),DamageType.GRASS);
                                            l.damage(400);
                                            if(ToolClass.entity_defence.containsKey(l.getUniqueId())){
                                                if(ToolClass.entity_defence.get(l.getUniqueId()).containsKey(DamageType.GRASS)){
                                                    Integer gk = ToolClass.entity_defence.get(l.getUniqueId()).get(DamageType.GRASS);
                                                    gk-=240;
                                                    ToolClass.entity_defence.get(l.getUniqueId()).put(DamageType.GRASS,gk);
                                                }else{
                                                    ToolClass.entity_defence.get(l.getUniqueId()).put(DamageType.GRASS,-240);
                                                }
                                            }else{
                                                ToolClass.entity_defence.put(l.getUniqueId(),new HashMap<>(){{put(DamageType.GRASS,-240);}});
                                            }
                                            ToolClass.grass_sword_skill_tick.put(l.getUniqueId(),160);

                                        }
                                    }
                                }
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.BLOCK_LEAF_LITTER_PLACE,3,1);
                            }
                            if(pdc.has(ToolClass.fromNamespaceKey("water_book"))){
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.BLOCK_BREWING_STAND_BREW,3,1);
                                new WaterBookSkillService(e.getPlayer()).runTaskTimer(ToolClass.PLUGIN,0,1);
                            }
                            if(pdc.has(ToolClass.fromNamespaceKey("fire_book"))){
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,3,1);
                                ToolClass.fireBookSkillTick.put(e.getPlayer().getUniqueId(),200);
                                ToolClass.defence.put(e.getPlayer().getUniqueId(),ToolClass.defence.get(e.getPlayer().getUniqueId())+1000);
                            }

                            service.addCold(id,skill);
                            return;
                        }
                    }
                }
            }
            if(pdc.has(ToolClass.fromNamespaceKey("stone_book"))){
                if(!ToolClass.book_tick.containsKey(e.getPlayer().getUniqueId())){
                    Item item = e.getPlayer().getWorld().dropItem(e.getPlayer().getEyeLocation(),new ItemStack(Material.STONE));
                    new StoneService(item,e.getPlayer().getEyeLocation().getDirection().normalize().multiply(4)).runTaskTimer(ToolClass.PLUGIN,0,1);
                    e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_ITEM_PICKUP,1,1);
                    ToolClass.book_tick.put(e.getPlayer().getUniqueId(),System.currentTimeMillis());
                }else{
                    Long now = System.currentTimeMillis();
                    if(now - ToolClass.book_tick.get(e.getPlayer().getUniqueId()) >= 400){
                        Item item = e.getPlayer().getWorld().dropItem(e.getPlayer().getEyeLocation(),new ItemStack(Material.STONE));
                        new StoneService(item,e.getPlayer().getEyeLocation().getDirection().normalize().multiply(3)).runTaskTimer(ToolClass.PLUGIN,0,1);
                        e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_ITEM_PICKUP,1,1);
                        ToolClass.book_tick.put(e.getPlayer().getUniqueId(),System.currentTimeMillis());
                    }
                }

            }
            if(pdc.has(ToolClass.fromNamespaceKey("water_book"))){
                if(!ToolClass.book_tick.containsKey(e.getPlayer().getUniqueId())){
                    ItemStack stack = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta meta1 = (PotionMeta) stack.getItemMeta();
                    meta1.setColor(ToolClass.typeColorMap.get(DamageType.WATER));
                    stack.setItemMeta(meta1);
                    ThrownPotion potion = e.getPlayer().launchProjectile(ThrownPotion.class,e.getPlayer().getLocation().getDirection().multiply(1.5f));
                    potion.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("water_book"),PersistentDataType.BOOLEAN,true);
                    potion.setItem(stack);
                    ToolClass.book_tick.put(e.getPlayer().getUniqueId(),System.currentTimeMillis());
                }else{
                    Long now = System.currentTimeMillis();
                    if(now - ToolClass.book_tick.get(e.getPlayer().getUniqueId()) >= 400){
                        ItemStack stack = new ItemStack(Material.SPLASH_POTION);
                        PotionMeta meta1 = (PotionMeta) stack.getItemMeta();
                        meta1.setColor(ToolClass.typeColorMap.get(DamageType.WATER));
                        stack.setItemMeta(meta1);
                        ThrownPotion potion = e.getPlayer().launchProjectile(ThrownPotion.class,e.getPlayer().getLocation().getDirection().multiply(1.5f));
                        potion.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("water_book"),PersistentDataType.BOOLEAN,true);
                        potion.setItem(stack);
                        ToolClass.book_tick.put(e.getPlayer().getUniqueId(),System.currentTimeMillis());
                    }
                }

            }
            if(pdc.has(ToolClass.fromNamespaceKey("fire_book"))){
                if(!ToolClass.book_tick.containsKey(e.getPlayer().getUniqueId())){
                    Vector dir = e.getPlayer().getEyeLocation().getDirection().normalize();
                    LargeFireball fireball = e.getPlayer().launchProjectile(LargeFireball.class,dir.multiply(1.5f)) ;
                    fireball.setIsIncendiary(false);
                    fireball.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("fire_book"),PersistentDataType.BOOLEAN,true);
                    e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_GHAST_SHOOT,1,1);
                    if(ToolClass.fireBookSkillTick.containsKey(e.getPlayer().getUniqueId())){
                        Location center = e.getPlayer().getLocation().add(0,1,0);
                        for(int i = 0;i<6;i++){
                            double a = 2*Math.PI*i/6;
                            Vector dire = new Vector(Math.cos(a),0,Math.sin(a)).normalize();
                            Location spawn = center.clone().add(dire.clone().multiply(1.5f));
                            LargeFireball fireball1 = e.getPlayer().getWorld().spawn(spawn,LargeFireball.class);
                            fireball1.setInvulnerable(false);
                            fireball1.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("fire_book"),PersistentDataType.BOOLEAN,true);
                            fireball1.setDirection(dire.clone().multiply(1.5f));
                        }
                    }
                    ToolClass.book_tick.put(e.getPlayer().getUniqueId(),System.currentTimeMillis());
                }else{
                    Long now = System.currentTimeMillis();
                    if(now - ToolClass.book_tick.get(e.getPlayer().getUniqueId()) >= 400){
                        Vector dir = e.getPlayer().getEyeLocation().getDirection().normalize();
                        LargeFireball fireball = e.getPlayer().launchProjectile(LargeFireball.class,dir.multiply(1.5f)) ;
                        fireball.setIsIncendiary(false);
                        fireball.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("fire_book"),PersistentDataType.BOOLEAN,true);
                        e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_GHAST_SHOOT,1,1);
                        if(ToolClass.fireBookSkillTick.containsKey(e.getPlayer().getUniqueId())){
                            Location center = e.getPlayer().getLocation().add(0,1,0);
                            for(int i = 0;i<6;i++){
                                double a = 2*Math.PI*i/6;
                                Vector dire = new Vector(Math.cos(a),0,Math.sin(a)).normalize();
                                Location spawn = center.clone().add(dire.clone().multiply(1.5f));
                                LargeFireball fireball1 = e.getPlayer().getWorld().spawn(spawn,LargeFireball.class);
                                fireball1.setInvulnerable(false);
                                fireball1.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("fire_book"),PersistentDataType.BOOLEAN,true);
                                fireball1.setDirection(dire.clone().multiply(1.5f));
                            }
                        }
                        ToolClass.book_tick.put(e.getPlayer().getUniqueId(),System.currentTimeMillis());
                    }
                }
            }
            if(pdc.has(ToolClass.fromNamespaceKey("count"))){
                Integer k = pdc.get(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER);
                if(k!=null){
                    if(k>0 && !pdc.has(ToolClass.fromNamespaceKey("changing"))){
                        if(pdc.has(ToolClass.fromNamespaceKey("normal_gun"))){
                            if(ToolClass.Shoot_timer.containsKey(e.getPlayer().getUniqueId())){
                                if(now_time - ToolClass.Shoot_timer.get(e.getPlayer().getUniqueId()) >= 300){

                                    ToolClass.Shoot(e.getPlayer().getEyeLocation(),e.getPlayer().getWorld(),10f,35, t);
                                    ToolClass.Shoot_timer.put(e.getPlayer().getUniqueId(),now_time);

                                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
                                    k--;
                                    pdc.set(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER,k);
                                    Component ss = Component.text("剩余弹药：", NamedTextColor.YELLOW)
                                            .append(Component.text(k + "/" + 10, NamedTextColor.GREEN));

                                    List<Component> lore = meta.lore();
                                    if (lore == null) lore = new ArrayList<>();
                                    while (lore.size() <= 2) lore.add(Component.empty());
                                    lore.set(2, ss);
                                    meta.lore(lore);
                                    e.getItem().setItemMeta(meta);
                                }
                            }else{
                                ToolClass.Shoot_timer.put(e.getPlayer().getUniqueId(),now_time);
                                ToolClass.Shoot(e.getPlayer().getEyeLocation(),e.getPlayer().getWorld(),10f,35, t);
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
                                k--;
                                pdc.set(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER,k);
                                Component ss = Component.text("剩余弹药：", NamedTextColor.YELLOW)
                                        .append(Component.text(k + "/" + 10, NamedTextColor.GREEN));

                                List<Component> lore = meta.lore();
                                if (lore == null) lore = new ArrayList<>();
                                while (lore.size() <= 2) lore.add(Component.empty());
                                lore.set(2, ss);
                                meta.lore(lore);
                                e.getItem().setItemMeta(meta);
                            }
                        }
                        if(pdc.has(ToolClass.fromNamespaceKey("lightning_gun"))){
                            k--;
                            pdc.set(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER,k);
                            Component ss = Component.text("剩余弹药：", NamedTextColor.YELLOW)
                                    .append(Component.text(k + "/" + 1, NamedTextColor.GREEN));

                            List<Component> lore = meta.lore();
                            if (lore == null) lore = new ArrayList<>();
                            while (lore.size() <= 2) lore.add(Component.empty());
                            lore.set(2, ss);
                            meta.lore(lore);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE,1,1);
                            ToolClass.ShootLightningGun(e.getPlayer());
                            e.getItem().setItemMeta(meta);
                        }


                    }
                    if(k<=0){
                        if(!pdc.has(ToolClass.fromNamespaceKey("changing"))){
                            Integer load = pdc.get(ToolClass.fromNamespaceKey("load_tick"),PersistentDataType.INTEGER);
                            pdc.set(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER,load != null ? load : 100);
                            e.getItem().setItemMeta(meta);
                        }

                    }

                }

            }

        }

    }
}
