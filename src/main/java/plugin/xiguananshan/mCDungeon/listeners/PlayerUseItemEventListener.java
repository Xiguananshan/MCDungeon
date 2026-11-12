package plugin.xiguananshan.mCDungeon.listeners;

import com.destroystokyo.paper.entity.ai.GoalType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;
import plugin.xiguananshan.mCDungeon.goals.FriendAttackGoal;
import plugin.xiguananshan.mCDungeon.goals.FriendRandomGoal;
import plugin.xiguananshan.mCDungeon.ticks.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
                            if(pdc.has(ToolClass.fromNamespaceKey("ice_bow"))){
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.BLOCK_GLASS_BREAK,3,1);
                                ToolClass.IceBowSkillTick.put(e.getPlayer().getUniqueId(),100);
                            }
                            if(pdc.has(ToolClass.fromNamespaceKey("physic_sword"))){
                                Location spawn = e.getPlayer().getLocation().clone().add(0,1.5f,0);
                                spawn.setPitch(0f);
                                ItemDisplay display = e.getPlayer().getWorld().spawn(spawn,ItemDisplay.class);
                                display.setItemStack(new ItemStack(Material.IRON_SWORD));
                                Transformation transformation = display.getTransformation();

                                transformation.getRightRotation().set(new AxisAngle4f(
                                        (float) Math.toRadians(135), 0f, 0f, 1f
                                ));
                                transformation.getScale().set(new Vector3f(0.8f,0.8f,0.8f));
                                display.setTransformation(transformation);
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.ENTITY_ILLUSIONER_CAST_SPELL,3,1);
                                new PhysicSwordSkillService(display).runTaskTimer(ToolClass.PLUGIN,0,1);
                            }

                            service.addCold(id,skill);
                            return;
                        }
                    }
                }
            }
            if(pdc.has(ToolClass.fromNamespaceKey("god_spawner"))){
                e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                e.setCancelled(true);
                Zombie zombie = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Zombie.class);
                Bukkit.getMobGoals().removeAllGoals(zombie, GoalType.TARGET);
                Bukkit.getMobGoals().addGoal(zombie,0,new FriendAttackGoal(zombie));
                Bukkit.getMobGoals().addGoal(zombie,2,new FriendRandomGoal(zombie,1));
                zombie.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET){{
                    ItemMeta meta1 = getItemMeta();
                    assert meta1 != null;
                    meta1.setUnbreakable(true);
                    setItemMeta(meta1);
                }});
                zombie.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE){{
                    ItemMeta meta1 = getItemMeta();
                    assert meta1 != null;
                    meta1.setUnbreakable(true);
                    setItemMeta(meta1);
                }});
                zombie.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS){{
                    ItemMeta meta1 = getItemMeta();
                    assert meta1 != null;
                    meta1.setUnbreakable(true);
                    setItemMeta(meta1);
                }});
                zombie.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS){{
                    ItemMeta meta1 = getItemMeta();
                    assert meta1 != null;
                    meta1.setUnbreakable(true);
                    setItemMeta(meta1);
                }});
                zombie.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD){{
                    ItemMeta meta1 = getItemMeta();
                    assert meta1 != null;
                    meta1.setUnbreakable(true);
                    meta1.addEnchant(Enchantment.SHARPNESS,5,true);
                    setItemMeta(meta1);
                }});
                zombie.getEquipment().setChestplateDropChance(0);
                zombie.getEquipment().setLeggingsDropChance(0);
                zombie.getEquipment().setHelmetDropChance(0);
                zombie.getEquipment().setBootsDropChance(0);
                zombie.getEquipment().setItemInMainHandDropChance(0);
                zombie.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("friend"),PersistentDataType.BOOLEAN,true);
                zombie.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("health"),PersistentDataType.INTEGER,400);
                zombie.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("limit_live"),PersistentDataType.BOOLEAN,true);
                zombie.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("hurt"),PersistentDataType.INTEGER,5);
                Objects.requireNonNull(zombie.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.5);
                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(),Sound.ENTITY_WITHER_SPAWN,3,1);
            }
            if(pdc.has(ToolClass.fromNamespaceKey("element_stone"))){
                ItemStack stack = e.getPlayer().getInventory().getItemInOffHand();
                if(stack.getType() != Material.AIR) {
                    ItemMeta m  = stack.getItemMeta();
                    assert m!=null;
                    if(m.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("element"),PersistentDataType.STRING)){
                        if(m.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("no_element"))){
                            e.getPlayer().sendMessage(Component.text("无法附魔此物品",NamedTextColor.RED));
                            e.getPlayer().playSound(e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
                            return;
                        }
                        String type = m.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("element"),PersistentDataType.STRING);
                        assert type != null;
                        if(type.equals ("PHYSIC")){
                            String stone_type = meta.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("element"),PersistentDataType.STRING);
                            assert stone_type != null;
                            m.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("element"),PersistentDataType.STRING,stone_type);
                            List<Component> lore = m.lore();
                            DamageType damageType = DamageType.valueOf(stone_type);
                            if(lore == null){
                                lore = List.of(
                                        Component.text("元素：",NamedTextColor.YELLOW)
                                                .append(
                                                        Component.text(ToolClass.typeNameMap.get(damageType)
                                                                ,NamedTextColor.nearestTo(TextColor.color(ToolClass.typeColorMap.get(damageType).asRGB())))));

                            }else{
                                if(lore.size()<2){
                                    lore.add(Component.text("元素：",NamedTextColor.YELLOW)
                                            .append(
                                                    Component.text(ToolClass.typeNameMap.get(damageType)
                                                            ,NamedTextColor.nearestTo(TextColor.color(ToolClass.typeColorMap.get(damageType).asRGB())))));

                                }else{
                                    lore.set(1,Component.text("元素：",NamedTextColor.YELLOW)
                                            .append(
                                                    Component.text(ToolClass.typeNameMap.get(damageType)
                                                            ,NamedTextColor.nearestTo(TextColor.color(ToolClass.typeColorMap.get(damageType).asRGB())))));
                                }
                            }
                            m.lore(lore);
                            stack.setItemMeta(m);
                            e.getPlayer().getInventory().setItemInMainHand(stack);
                            e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                            e.getPlayer().sendMessage(Component.text("附魔成功",NamedTextColor.GREEN));
                            e.getPlayer().playSound(e.getPlayer(),Sound.BLOCK_ENCHANTMENT_TABLE_USE,1,1);
                        }else{
                            e.getPlayer().sendMessage(Component.text("该物品已被附魔",NamedTextColor.RED));
                            e.getPlayer().playSound(e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
                        }
                    }else{
                        e.getPlayer().sendMessage(Component.text("无法附魔此物品",NamedTextColor.RED));
                        e.getPlayer().playSound(e.getPlayer(),Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
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
