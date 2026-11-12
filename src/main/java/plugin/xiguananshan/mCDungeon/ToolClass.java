package plugin.xiguananshan.mCDungeon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.structure.Structure;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.data.CornerData;
import plugin.xiguananshan.mCDungeon.data.CrossData;
import plugin.xiguananshan.mCDungeon.data.EasyLocation;
import plugin.xiguananshan.mCDungeon.data.StorageManager;
import plugin.xiguananshan.mCDungeon.enums.CrossStructureType;
import plugin.xiguananshan.mCDungeon.enums.DamageType;
import plugin.xiguananshan.mCDungeon.events.GunShotEvent;
import plugin.xiguananshan.mCDungeon.ticks.*;

import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ToolClass {
    public static MCDungeon PLUGIN = MCDungeon.getPlugin(MCDungeon.class);
    public static ItemStack DefaultGun = new ItemStack(Material.GOLDEN_HOE);
    public static ItemStack WindSword = new ItemStack(Material.DIAMOND_SWORD);
    public static ItemStack StoneBook = new ItemStack(Material.BOOK);
    public static ItemStack LightningGun = new ItemStack(Material.DIAMOND_HOE);
    public static ItemStack GrassSword = new ItemStack(Material.WOODEN_SWORD);
    public static ItemStack WaterBook = new ItemStack(Material.BOOK);
    public static ItemStack FireBook = new ItemStack(Material.BOOK);
    public static ItemStack Null = new ItemStack(Material.BARRIER);
    public static ItemStack IceBow = new ItemStack(Material.BOW);
    public static ItemStack IceBowArrow = new ItemStack(Material.ARROW);
    public static ItemStack PhysicSword = new ItemStack(Material.IRON_SWORD);
    public static ItemStack GodSpawner = new ItemStack(Material.COMMAND_BLOCK);
    public static ItemStack ElementStone = new ItemStack(Material.AMETHYST_SHARD);
    public static Map<ItemStack,Float> itemPool = new HashMap<>();
    public static Map<UUID,Long> Shoot_timer = new HashMap<>();
    public static Map<UUID,DamageType> typeMap = new HashMap<>();
    public static Map<UUID,Map<DamageType,Integer>> entity_defence = new HashMap<>();
    public static Map<UUID, HashSet<DamageType>> entity_type = new HashMap<>();
    public static Map<UUID,Map<DamageType,Integer>> element_type = new HashMap<>();
    public static Map<UUID,Integer> freeze_tick = new HashMap<>();
    public static Map<UUID,Integer> grass_tick = new HashMap<>();
    public static Map<UUID,Integer> grass_burn = new HashMap<>();
    public static Map<UUID,Integer> physic_attack = new HashMap<>();
    public static Map<UUID,Integer> defence = new HashMap<>();
    public static Map<UUID,Integer> grass_sword_skill_tick= new HashMap<>();
    public static Map<UUID, PlayerGunService> gunServiceMap = new HashMap<>();
    public static Map<UUID, PlayerRockDefenceTickTimer> rockDefenceTickTimerMap = new HashMap<>();
    public static Map<UUID, PlayerSkillService> skillServiceMap = new HashMap<>();
    public static Map<UUID,PlayerLightningGunService> lightningGunServiceMap = new HashMap<>();
    public static Map<UUID,Integer> fireBookSkillTick = new HashMap<>();
    public static Map<UUID,Long> book_tick = new HashMap<>();
    public static Map<UUID,Integer> IceBowSkillTick = new HashMap<>();
    public static Integer max_item_id = 0;
    public static List<UUID> game_players = new ArrayList<>();
    public static Structure CROSS_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("cross_north"));
    public static Structure CROSS_ROOM_1_2 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("cross_east"));
    public static Structure ACTIVITY_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("activite_room"));
    public static Structure BOSS_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("boss_room"));
    public static Structure START_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("start_room"));
    public static Structure CENTER_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("center_room"));
    public static Structure CENTER_ROOM_1_2 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("center_room1"));
    public static Structure SHOP_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("shop_room"));
    public static Structure Box_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("box_room"));
    public static StorageManager storageManager = new StorageManager("map.dat");
    public static Map<UUID,Long> sword_tick = new HashMap<>();
    public static Map<DamageType,Color> typeColorMap = new HashMap<>(){{
        put(DamageType.PHYSIC,Color.fromARGB(255,255,255,255));
        put(DamageType.AIR,Color.fromRGB(0x00AAAA));
        put(DamageType.WATER,Color.fromARGB(255,74,74,255));
        put(DamageType.ICE,Color.fromARGB(255,0,255,255));
        put(DamageType.GRASS,Color.fromARGB(255,0,255,70));
        put(DamageType.FIRE,Color.fromARGB(255,255,100,0));
        put(DamageType.ROCK,Color.fromRGB(0xFFFF55));
        put(DamageType.LIGHTNING,Color.fromARGB(255,200,0,255));
        put(DamageType.EXPLODE,Color.fromRGB(0xFF55FF));
    }};
    public static Map<DamageType,String> typeNameMap = new HashMap<>(){{
        put(DamageType.PHYSIC,"物理");
        put(DamageType.AIR,"空气");
        put(DamageType.WATER,"水");
        put(DamageType.ICE,"冰");
        put(DamageType.GRASS,"草");
        put(DamageType.FIRE,"火");
        put(DamageType.ROCK,"岩");
        put(DamageType.LIGHTNING,"雷电");
    }};
    static{
        ItemMeta meta = DefaultGun.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("normal_gun"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"PHYSIC");
        pdc.set(fromNamespaceKey("count"),PersistentDataType.INTEGER,10);
        pdc.set(fromNamespaceKey("max_count"),PersistentDataType.INTEGER,10);
        pdc.set(fromNamespaceKey("load_tick"),PersistentDataType.INTEGER,100);
        Component ss = Component.text("剩余弹药：",NamedTextColor.YELLOW).append(Component.text("10/10",NamedTextColor.GREEN));
        Component name = Component.text("普通枪", NamedTextColor.WHITE);
        meta.displayName(name);
        meta.lore(List.of(Component.text("只是一把再普通不过的枪，射击敌人造成35伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.PHYSIC),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.PHYSIC).asRGB()))))
                ,ss,
                Component.text("左键换弹",NamedTextColor.YELLOW)))
        ;
        DefaultGun.setItemMeta(meta);
    }
    static{
        ItemMeta meta = WindSword.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("wind_sword"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"AIR");
        pdc.set(fromNamespaceKey("sword"),PersistentDataType.INTEGER,160);
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,600);
        Component name = Component.text("狂风怒号之刃", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("一把汲取了浓烈空气元素力量的剑，见证了风在星球上的千年历史，攻击敌人造成160点伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.AIR),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.AIR).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这把剑的技能",NamedTextColor.YELLOW),Component.text("技能：在上方生成一个持续10秒的风洞，风洞会不断吸收周围5格内的敌人，并对周围2格内的敌人每秒造成2次40点空气元素伤害",NamedTextColor.YELLOW)));
        WindSword.setItemMeta(meta);
    }
    static{
        ItemMeta meta = StoneBook.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("stone_book"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"ROCK");
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,300);
        Component name = Component.text("粉骨碎身之法", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("一本上面写着《岩国通史》的书，后来因为其拥有者得到了岩的祝福而变成了强力的法器，右键向前投出一块石头，击中敌人造成80点岩元素伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.ROCK),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.ROCK).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这本法器的技能",NamedTextColor.YELLOW)
                ,Component.text("技能：从上方砸下一块石头，落地时给周围3*3范围内的敌人造成700岩元素伤害，落下的石头在接下来8秒内进行4次岩共鸣，每次共鸣对周围4格范围内的敌人造成100点岩元素伤害",NamedTextColor.YELLOW)));

        StoneBook.setItemMeta(meta);
    }
    static{
        ItemMeta meta = LightningGun.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("lightning_gun"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"LIGHTNING");
        pdc.set(fromNamespaceKey("count"),PersistentDataType.INTEGER,1);
        pdc.set(fromNamespaceKey("max_count"),PersistentDataType.INTEGER,1);
        pdc.set(fromNamespaceKey("load_tick"),PersistentDataType.INTEGER,60);
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,1200);
        Component ss = Component.text("剩余弹药：",NamedTextColor.YELLOW).append(Component.text("1/1",NamedTextColor.GREEN));
        Component name = Component.text("雷光展露之炮", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("大人，时代变了，当该武器弹药不为0时手持该武器可以蓄力，蓄力越久伤害越高，最高可达500点",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.LIGHTNING),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.LIGHTNING).asRGB()))))
                ,ss
                ,Component.text("左键换弹",NamedTextColor.YELLOW)
                ,Component.text("按住Shift+右键可以释放这门炮的技能",NamedTextColor.YELLOW)
                ,Component.text("技能：向前挥出5格宽的雷元素剑气，可穿透墙壁和敌人，造成1200点雷元素伤害",NamedTextColor.YELLOW)));

        LightningGun.setItemMeta(meta);
    }
    static{
        ItemMeta meta = GrassSword.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("grass_sword"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"GRASS");
        pdc.set(fromNamespaceKey("sword"),PersistentDataType.INTEGER,220);
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,400);
        Component name = Component.text("蔓草丛生之剑", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("草木哺育生命，也能扼杀生命，攻击造成220点草元素伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.GRASS),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.GRASS).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这把剑的技能",NamedTextColor.YELLOW),Component.text("技能：在附近4*4的敌人的位置生成藤蔓使其无法动弹，并造成400点草元素伤害，降低240点草元素抗性",NamedTextColor.YELLOW)));

        GrassSword.setItemMeta(meta);
    }
    static{
        ItemMeta meta = WaterBook.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("water_book"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"WATER");
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,400);
        Component name = Component.text("静水翻涌之典", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("水是生命之源，但愿是。右键投出一瓶水，落地产生一滩能对敌人持续每秒造成65点水元素伤害的水，持续3秒",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.WATER),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.WATER).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这本法器的技能",NamedTextColor.YELLOW)
                ,Component.text("技能：在周围形成一个持续10秒的水圈，在水圈内部的敌人会每秒受到2次120点的水元素伤害，并被排开，每有一个敌人受到来自水圈的伤害，你都有40%的概率恢复1HP",NamedTextColor.YELLOW)));

        WaterBook.setItemMeta(meta);
    }
    static{
        ItemMeta meta = FireBook.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("fire_book"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"FIRE");
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,800);
        Component name = Component.text("野火焚尽之契", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("如果一切都为时已晚，便让火来重置世间一切。右键发射一枚火球，击中敌人造成70点火元素伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.FIRE),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.FIRE).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这本法器的技能",NamedTextColor.YELLOW)
                ,Component.text("技能：接下来的10秒内，所有普通攻击都会向四周发射发射火球，并获得1000抗性和速度II效果",NamedTextColor.YELLOW)));

        FireBook.setItemMeta(meta);
    }
    static{
        ItemMeta meta = Null.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("null"),PersistentDataType.BOOLEAN,true);
        Component name = Component.text("滚木", NamedTextColor.LIGHT_PURPLE);
        meta.displayName(name);
        meta.lore(List.of(Component.text("很遗憾，这个箱子里没有任何东西，但又话说回来，极致的非酋有何尝不是另一种意义上的欧皇呢",NamedTextColor.YELLOW),Component.text("一次性用品，攻击任意一个非Boss敌人可以将其抹杀",NamedTextColor.YELLOW)));
        Null.setItemMeta(meta);
    }
    static{
        ItemMeta meta = GodSpawner.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("god_spawner"),PersistentDataType.BOOLEAN,true);
        Component name = Component.text("神临", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("神的降临从来不需要理由",NamedTextColor.YELLOW),Component.text("一次性用品，召唤一个400血的僵尸为你而战，僵尸穿戴下界合金套装并备有锋利V的下界合金剑，每一秒损失5点生命值",NamedTextColor.YELLOW)));
        GodSpawner.setItemMeta(meta);
    }
    static{
        ItemMeta meta = IceBow.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.INFINITY,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("ice_bow"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"ICE");
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,1000);
        Component name = Component.text("雪国冰霜之弓", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("寒风呼啸而过，雪中打猎的猎人已经拉满的弦。射出箭矢对敌人造成300冰元素伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.ICE),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.ICE).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这把弓的技能",NamedTextColor.YELLOW)
                ,Component.text("技能：在接下来的5秒内，无需拉弓，只需要右键就能射出一发箭，每发箭造成150点冰元素伤害（没有击退）",NamedTextColor.YELLOW)));

        IceBow.setItemMeta(meta);
    }
    static{
        ItemMeta meta = IceBowArrow.getItemMeta();
        assert meta != null;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("ice_bow_arrow"),PersistentDataType.BOOLEAN,true);
        meta.displayName(Component.text("箭", NamedTextColor.WHITE));
        meta.lore(List.of(Component.text("与弓绑定的箭矢",NamedTextColor.YELLOW)
                ,Component.text("射出的箭无法拾取，自身也不会被消耗",NamedTextColor.YELLOW)));
        meta.setEnchantmentGlintOverride(true);
        IceBowArrow.setItemMeta(meta);
    }
    static {
        ItemMeta meta = ElementStone.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("element_stone"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("random_element"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("no_element"),PersistentDataType.BOOLEAN,true);
        Component name = Component.text("元素晶石", NamedTextColor.BLUE);
        meta.displayName(name);
        meta.lore(List.of(Component.text("一个汲取了元素力精华的晶石，可以用来强化武器",NamedTextColor.YELLOW),Component.text("消耗品，将要进行元素附魔的武器放置在副手，右键此道具可以对其进行元素附魔",NamedTextColor.YELLOW)));
        ElementStone.setItemMeta(meta);
    }
    static{
        ItemMeta meta = PhysicSword.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.SHARPNESS,1,true);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("physic_sword"),PersistentDataType.BOOLEAN,true);
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"PHYSIC");
        pdc.set(fromNamespaceKey("sword"),PersistentDataType.INTEGER,280);
        pdc.set(fromNamespaceKey("skill"),PersistentDataType.INTEGER,1000);
        pdc.set(fromNamespaceKey("no_element"),PersistentDataType.BOOLEAN,true);
        Component name = Component.text("遗愿终成之刀", NamedTextColor.GOLD);
        meta.displayName(name);
        meta.lore(List.of(Component.text("得到元素祝福的愿望终会达成，一定会的。攻击造成280点物理伤害",NamedTextColor.YELLOW)
                ,Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.PHYSIC),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(DamageType.PHYSIC).asRGB()))))
                ,Component.text("按住Shift+右键可以释放这把剑的技能",NamedTextColor.YELLOW),Component.text("技能：在上方生成一把铁剑，1秒后落下，落地产生冲击波，造成1500点物理伤害",NamedTextColor.YELLOW)
                ,Component.text("该武器无法被元素晶石强化",NamedTextColor.RED)
                        )
                );


        PhysicSword.setItemMeta(meta);
    }
    static {
        itemPool.put(DefaultGun, 0.90f);
        itemPool.put(ElementStone, 0.65f);
        itemPool.put(WindSword, 0.015f);
        itemPool.put(StoneBook, 0.015f);
        itemPool.put(GrassSword, 0.015f);
        itemPool.put(LightningGun, 0.015f);
        itemPool.put(WaterBook, 0.015f);
        itemPool.put(FireBook, 0.015f);
        itemPool.put(IceBow, 0.015f);
        itemPool.put(PhysicSword, 0.015f);
        itemPool.put(GodSpawner, 0.015f);
    }
    public static boolean isIceBow(ItemStack stack){
        if(stack == null || stack.getType() != Material.BOW) return false;
        ItemMeta meta = stack.getItemMeta();
        return meta != null && meta.getPersistentDataContainer()
                .has(fromNamespaceKey("ice_bow"),PersistentDataType.BOOLEAN);
    }
    public static boolean isIceBowArrow(ItemStack stack){
        if(stack == null || stack.getType() != Material.ARROW) return false;
        ItemMeta meta = stack.getItemMeta();
        return meta != null && meta.getPersistentDataContainer()
                .has(fromNamespaceKey("ice_bow_arrow"),PersistentDataType.BOOLEAN);
    }
    public static boolean giveIceBowArrow(Player p){
        NamespacedKey granted = fromNamespaceKey("ice_bow_arrow_granted");
        PersistentDataContainer pdc = p.getPersistentDataContainer();
        if(pdc.has(granted,PersistentDataType.BOOLEAN)) return false;
        for(ItemStack stack : p.getInventory().getContents()){
            if(isIceBowArrow(stack)){
                pdc.set(granted,PersistentDataType.BOOLEAN,true);
                return false;
            }
        }
        int slot = p.getInventory().firstEmpty();
        if(slot < 0) return false;
        p.getInventory().setItem(slot,IceBowArrow.clone());
        pdc.set(granted,PersistentDataType.BOOLEAN,true);
        return true;
    }
    public static ItemStack fromItem(ItemStack stack){
        ItemStack s = stack.clone();
        ItemMeta meta = s.getItemMeta();
        assert meta != null;
        meta.getPersistentDataContainer().set(fromNamespaceKey("id"),PersistentDataType.INTEGER,max_item_id);
        if(meta.getPersistentDataContainer().has(fromNamespaceKey("random_element"))){
            Random random = new Random();
            DamageType t = DamageType.PHYSIC;
            while(t == DamageType.PHYSIC || t == DamageType.EXPLODE){
                t = DamageType.values()[random.nextInt(DamageType.values().length)];
            }
            meta.getPersistentDataContainer().set(fromNamespaceKey("element"),PersistentDataType.STRING,t.toString());
            List<Component> lore = meta.lore();
            if(lore!=null){
                lore.add(Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(t),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(t).asRGB())))));
            }else{
                lore = List.of(Component.text("元素：",NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(t),NamedTextColor.nearestTo(TextColor.color(typeColorMap.get(t).asRGB())))));
            }
            meta.lore(lore);
        }
        max_item_id++;
        s.setItemMeta(meta);
        return s;
    }
    public static void SummonEntity(World world, Location location, List<ItemStack> equipments,boolean ai,int health,Class<? extends Mob> type){
        Mob entity = world.spawn(location, type);
        entity.setAI(ai);
        EntityEquipment equipment = entity.getEquipment();
        equipment.setHelmetDropChance(0.0F);
        equipment.setChestplateDropChance(0.0F);
        equipment.setLeggingsDropChance(0.0F);
        equipment.setBootsDropChance(0.0F);
        equipment.setItemInMainHandDropChance(0.0F);
        entity.setRemoveWhenFarAway(false);
        equipment.setHelmet(equipments.get(0));
        equipment.setChestplate(equipments.get(1));
        equipment.setLeggings(equipments.get(2));
        equipment.setBoots(equipments.get(3));
        equipment.setItemInMainHand(equipments.get(4));
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        pdc.set(fromNamespaceKey("health"), PersistentDataType.INTEGER,health);
        pdc.set(fromNamespaceKey("entity"),PersistentDataType.BOOLEAN,true);
        entity.setCustomNameVisible(true);
        entity.customName(Component.text("剩余血量",NamedTextColor.YELLOW).append(Component.text(String.valueOf(health),NamedTextColor.RED)));

    }
    public static NamespacedKey fromNamespaceKey(String name){
        return new NamespacedKey(PLUGIN,name);
    }
    public static ItemStack FromMobItemstack(Material material){
        ItemStack ans = new ItemStack(material);
        ItemMeta meta = ans.getItemMeta();
        if (meta == null) return ans;
        meta.setUnbreakable(true);
        ans.setItemMeta(meta);
        return ans;
    }
    public static void SpawnDamageEntity(int damage,World world,Location location,NamedTextColor color){
        if(damage<=0){
            return;
        }
        TextDisplay entity = world.spawn(location, TextDisplay.class);
        entity.text(Component.text("-" + damage,color));
        entity.setBillboard(Display.Billboard.CENTER);
        entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        entity.setShadowed(true);
        entity.setPersistent(false);
        BukkitRunnable runnable = new DamageTextTick(entity,0.5f);
        runnable.runTaskTimer(PLUGIN,0,1);
    }
    public static void SpawnTextEntity(Component text,World world,Location location){
        TextDisplay entity = world.spawn(location, TextDisplay.class);
        entity.text(text);
        entity.setBillboard(Display.Billboard.CENTER);
        entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        entity.setShadowed(true);
        entity.setPersistent(false);
        BukkitRunnable runnable = new DamageTextTick(entity,0.5f);
        runnable.runTaskTimer(PLUGIN,0,1);
    }
    public static void Shoot(Location location,World world,float max_distance,int damage,DamageType type){
        Vector direction = location.getDirection();
        RayTraceResult block = world.rayTraceBlocks(
                location,
                direction,
                max_distance,
                FluidCollisionMode.NEVER,
                true
        );
        RayTraceResult entity = world.rayTraceEntities(
                location,
                direction,
                max_distance,
                0.0,
                entity1 -> {
                    PersistentDataContainer pdc = entity1.getPersistentDataContainer();
                    return (pdc.has(fromNamespaceKey("entity")) || (entity1 instanceof Item && pdc.has(fromNamespaceKey("grass")) && (type == DamageType.LIGHTNING || type == DamageType.FIRE)));
                }
        );
        RayTraceResult r = null;
        if(entity != null){
            double e = entity.getHitPosition().distance(location.toVector());
            double b = (block != null) ? block.getHitPosition().distance(location.toVector()) : Double.MAX_VALUE;
            if(e < b){
                Entity entity1 = entity.getHitEntity();
                assert entity1 != null;
                if(entity1 instanceof LivingEntity l){
                    Vector n_direction = direction.normalize();
                    n_direction.setY(0);
                    l.setVelocity(n_direction.multiply(0.5f).setY(0.3f));
                    typeMap.put(l.getUniqueId(),type);
                    Bukkit.getPluginManager().callEvent(new GunShotEvent(damage,type,l,false));

                }else if(entity1.getPersistentDataContainer().has(fromNamespaceKey("grass"))){
                    if(type == DamageType.LIGHTNING){
                        Collection<Entity> entities = entity1.getNearbyEntities(8,8,8);
                        LivingEntity goal = null;
                        double min = Double.MAX_VALUE;
                        for(Entity entity2 : entities){
                            if(entity2 instanceof LivingEntity ll){
                                if(ll.getPersistentDataContainer().has(fromNamespaceKey("entity"))){
                                    if(ll.getLocation().distanceSquared(entity1.getLocation())<=min){
                                        min = ll.getLocation().distanceSquared(entity1.getLocation());
                                        goal = ll;

                                    }
                                }
                            }
                        }
                        if(goal != null){
                            Integer d = entity1.getPersistentDataContainer().get(fromNamespaceKey("grass"),PersistentDataType.INTEGER);
                            if(d != null)
                                new SeedLightningTickTimer(entity1.getLocation(),goal,goal.getWorld(),d).runTaskTimer(PLUGIN,0,1);
                        }
                        ToolClass.fillCubeWithParticles(entity1.getWorld(),entity1.getLocation(),0.8,10,new Particle.DustOptions(Color.GREEN,3));
                        entity1.getWorld().playSound(entity1.getLocation(),Sound.BLOCK_GRASS_BREAK,1,1);
                        entity1.remove();


                    }else if(type == DamageType.FIRE){
                        Collection<Entity> entities = entity1.getNearbyEntities(1,1,1);
                        for(Entity entity2 : entities){
                            if(entity2 instanceof LivingEntity ll){
                                if(ll.getPersistentDataContainer().has(fromNamespaceKey("entity"))){
                                    ToolClass.typeMap.put(ll.getUniqueId(),DamageType.FIRE);
                                    ll.damage(30);
                                }
                            }
                        }
                        ToolClass.fillCubeWithParticles(entity1.getWorld(),entity1.getLocation(),0.8,10,new Particle.DustOptions(typeColorMap.get(DamageType.FIRE),3));
                        entity1.getWorld().playSound(entity1.getLocation(),Sound.BLOCK_GRASS_BREAK,1,1);
                        entity1.remove();
                    }
                }
                r = entity;
            }else{
                r = block;
            }
        }else{
            if(block != null){
                r = block;
            }
        }
        DrawRay(r,world,location,max_distance,typeColorMap.get(type));
    }
    public static void DrawRay(RayTraceResult ray, World world, Location start, float max_distance,Color color){
        Vector end;
        if(ray != null){
            end = ray.getHitPosition();
        }else{
            end = start.toVector().add(start.getDirection().clone().normalize().multiply(max_distance));
        }
        Particle.DustOptions DustData = new Particle.DustOptions(color, 1.0f);
        Vector startVec = start.toVector();
        Vector step = end.clone().subtract(startVec);
        Vector now = startVec.clone();
        double totalDistance = step.length();
        step.normalize().multiply(0.2f);
        for(double t = 0;t<totalDistance;t+=0.2f){
            world.spawnParticle(Particle.DUST,now.getX(),now.getY(),now.getZ(),1,0,0,0,0,DustData);
            now.add(step);
        }
    }
    public static void fillCubeWithParticles(World world, Location center, double halfSize,
                                      int particleCount, Particle.DustOptions dust) {
        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < particleCount; i++) {
            double x = center.getX() + (random.nextDouble() * 2 - 1) * halfSize;
            double y = center.getY() + (random.nextDouble() * 2 - 1) * halfSize;
            double z = center.getZ() + (random.nextDouble() * 2 - 1) * halfSize;

            world.spawnParticle(Particle.DUST, x, y, z, 1, 0, 0, 0, 0, dust);
        }
    }

    public static void GenerateMap(List<Player> players,int type) {
        if(type == 1){
            game_players.clear();
            Title title = Title.title(Component.text("正在生成地图",NamedTextColor.YELLOW),
                    Component.text().build(),
                    Title.Times.times(
                    Duration.ofMillis(500),
                    Duration.ofMillis(2000),
                    Duration.ofMillis(500)
            ));
            for(Player player : players)    {game_players.add(player.getUniqueId());player.showTitle(title);}
            List<Structure> spawnable = List.of(CENTER_ROOM_1_2,CENTER_ROOM_1_1, SHOP_ROOM_1_1, ACTIVITY_ROOM_1_1,  Box_ROOM_1_1);
            World world = Bukkit.getWorld("world");
            assert  world != null;

            try{
                CornerData data = storageManager.load();
                if(data != null){
                    for(CornerData.Corner corner : data.corners){
                        for(int x = corner.minx;x<= corner.maxx;x++){
                            for(int y = corner.miny;y<=corner.maxy;y++){
                                for(int z = corner.minz;z<=corner.maxz;z++){
                                    world.getBlockAt(x,y,z).setType(Material.AIR,false);
                                }
                            }
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            int center=0,shop=0,box=0;
            Random rand = new Random();
            int count = rand.nextInt(5)+6;
            int c = 0;
            List<CrossData> spawn = new ArrayList<>();
            HashSet<EasyLocation> rooms = new HashSet<>();
            CornerData data = new CornerData();
            data.corners = new ArrayList<>();
            START_ROOM_1_1.place(new Location(world,0,100,0),false, StructureRotation.NONE, Mirror.NONE,0,1,rand);
            rooms.add(new EasyLocation(0,100,0));
            data.corners.add(new CornerData.Corner(0,100,0,29,105,29));
            spawn.add(new CrossData(new Location(world,30,100,0), CrossStructureType.XA));
            while(center < 3 || shop < 1 || box < 1 || c < count){
                Structure s = spawnable.get(rand.nextInt(spawnable.size()));
                CrossData l = spawn.get(rand.nextInt(spawn.size()));
                spawn.remove(l);
                CrossData finalL = l;
                switch (l.type){
                    case XA -> {
                        CROSS_ROOM_1_2.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                        EasyLocation location = new EasyLocation(l.l.getBlockX()+30,l.l.getBlockY(),l.l.getBlockZ());
                        data.corners.add(new CornerData.Corner(finalL.l.getBlockX(),finalL.l.getBlockY(),finalL.l.getBlockZ(),finalL.l.getBlockX()+29,finalL.l.getBlockY()+5,finalL.l.getBlockZ()+29));
                        if(!rooms.contains(location)){
                            rooms.add(location);
                            s.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world, location.x() +30, location.y(), location.z()),CrossStructureType.XA);
                            CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() -30),CrossStructureType.ZR);
                            CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() +30),CrossStructureType.ZA);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                            if(s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2){
                                center++;
                            }
                            if(s==SHOP_ROOM_1_1){
                                shop++;
                            }
                            if(s==Box_ROOM_1_1){
                                box++;
                            }
                        }
                    }
                    case XR -> {
                        CROSS_ROOM_1_2.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                        EasyLocation location = new EasyLocation(l.l.getBlockX()-30,l.l.getBlockY(),l.l.getBlockZ());
                        data.corners.add(new CornerData.Corner(finalL.l.getBlockX(),finalL.l.getBlockY(),finalL.l.getBlockZ(),finalL.l.getBlockX()+29,finalL.l.getBlockY()+5,finalL.l.getBlockZ()+29));
                        if(!rooms.contains(location)){
                            rooms.add(location);
                            s.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world, location.x() -30, location.y(), location.z()),CrossStructureType.XR);
                            CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() -30),CrossStructureType.ZR);
                            CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() +30),CrossStructureType.ZA);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                            if(s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2){
                                center++;
                            }
                            if(s==SHOP_ROOM_1_1){
                                shop++;
                            }
                            if(s==Box_ROOM_1_1){
                                box++;
                            }
                        }
                    }
                    case ZA -> {
                        CROSS_ROOM_1_1.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                        EasyLocation location = new EasyLocation(l.l.getBlockX(),l.l.getBlockY(),l.l.getBlockZ()+30);
                        data.corners.add(new CornerData.Corner(finalL.l.getBlockX(),finalL.l.getBlockY(),finalL.l.getBlockZ(),finalL.l.getBlockX()+29,finalL.l.getBlockY()+5,finalL.l.getBlockZ()+29));
                        if(!rooms.contains(location)){
                            rooms.add(location);
                            s.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world, location.x() -30, location.y(), location.z()),CrossStructureType.XR);
                            CrossData c2 = new CrossData(new Location(world, location.x() +30, location.y(), location.z()),CrossStructureType.XA);
                            CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() +30),CrossStructureType.ZA);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                            if(s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2){
                                center++;
                            }
                            if(s==SHOP_ROOM_1_1){
                                shop++;
                            }
                            if(s==Box_ROOM_1_1){
                                box++;
                            }
                        }
                    }
                    case ZR -> {
                        CROSS_ROOM_1_1.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                        EasyLocation location = new EasyLocation(l.l.getBlockX(),l.l.getBlockY(),l.l.getBlockZ()-30);
                        data.corners.add(new CornerData.Corner(finalL.l.getBlockX(),finalL.l.getBlockY(),finalL.l.getBlockZ(),finalL.l.getBlockX()+29,finalL.l.getBlockY()+5,finalL.l.getBlockZ()+29));
                        if(!rooms.contains(location)){
                            rooms.add(location);
                            s.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world, location.x() +30, location.y(), location.z()),CrossStructureType.XA);
                            CrossData c2 = new CrossData(new Location(world, location.x() -30, location.y(), location.z()),CrossStructureType.XR);
                            CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() -30),CrossStructureType.ZR);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                            if(s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2){
                                center++;
                            }
                            if(s==SHOP_ROOM_1_1){
                                shop++;
                            }
                            if(s==Box_ROOM_1_1){
                                box++;
                            }
                        }
                    }

                }
                c++;
                if(!(center < 3 || shop < 1 || box < 1 || c < count)){
                    int boss = 0;
                    while(boss < 1){
                        l = spawn.get(rand.nextInt(spawn.size()));
                        spawn.remove(l);
                        CrossData finalL1 = l;
                        switch (l.type){
                            case XA -> {
                                CROSS_ROOM_1_2.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX()+30,l.l.getBlockY(),l.l.getBlockZ());
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world, location.x() +30, location.y(), location.z()),CrossStructureType.XA);
                                    CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() -30),CrossStructureType.ZR);
                                    CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() +30),CrossStructureType.ZA);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));

                                }
                            }
                            case XR -> {
                                CROSS_ROOM_1_2.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX()-30,l.l.getBlockY(),l.l.getBlockZ());
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world, location.x() -30, location.y(), location.z()),CrossStructureType.XR);
                                    CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() -30),CrossStructureType.ZR);
                                    CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() +30),CrossStructureType.ZA);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                                }
                            }
                            case ZA -> {
                                CROSS_ROOM_1_1.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX(),l.l.getBlockY(),l.l.getBlockZ()+30);
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world, location.x() -30, location.y(), location.z()),CrossStructureType.XR);
                                    CrossData c2 = new CrossData(new Location(world, location.x() +30, location.y(), location.z()),CrossStructureType.XA);
                                    CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() +30),CrossStructureType.ZA);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                                }
                            }
                            case ZR -> {
                                CROSS_ROOM_1_1.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX(),l.l.getBlockY(),l.l.getBlockZ()-30);
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world, location.x() +30, location.y(), location.z()),CrossStructureType.XA);
                                    CrossData c2 = new CrossData(new Location(world, location.x() -30, location.y(), location.z()),CrossStructureType.XR);
                                    CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() -30),CrossStructureType.ZR);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() +29, location.y() +5, location.z() +29));
                                }
                            }

                        }
                    }
                }
            }
            for(EasyLocation location : rooms){
                EasyLocation xamin,xamax,xrmin,xrmax,zamin,zamax,zrmin,zrmax;
                xamin = new EasyLocation(location.x() +29, location.y() +1, location.z() +14);
                xamax = new EasyLocation(location.x() +29, location.y() +3, location.z() +15);
                xrmin = new EasyLocation(location.x(), location.y() +1, location.z() +14);
                xrmax = new EasyLocation(location.x(), location.y() +3, location.z() +15);
                zamin = new EasyLocation(location.x() +14, location.y() +1, location.z() +29);
                zamax = new EasyLocation(location.x() +15, location.y() +3, location.z() +29);
                zrmin = new EasyLocation(location.x() +14, location.y() +1, location.z());
                zrmax = new EasyLocation(location.x() +15, location.y() +3, location.z());
                if(!data.corners.contains(new CornerData.Corner(location.x() +30, location.y(), location.z(), location.x() +59, location.y() +5, location.z() +29))){
                    for(int x = xamin.x(); x<= xamax.x(); x++){
                        for(int y = xamin.y(); y<= xamax.y(); y++){
                            for(int z = xamin.z(); z<= xamax.z(); z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
                if(!data.corners.contains(new CornerData.Corner(location.x() -30, location.y(), location.z(), location.x() -1, location.y() +5, location.z() +29))){
                    for(int x = xrmin.x(); x<= xrmax.x(); x++){
                        for(int y = xrmin.y(); y<= xrmax.y(); y++){
                            for(int z = xrmin.z(); z<= xrmax.z(); z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
                if(!data.corners.contains(new CornerData.Corner(location.x(), location.y(), location.z() +30, location.x() +29, location.y() +5, location.z() +59))){
                    for(int x = zamin.x(); x<= zamax.x(); x++){
                        for(int y = zamin.y(); y<= zamax.y(); y++){
                            for(int z = zamin.z(); z<= zamax.z(); z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
                if(!data.corners.contains(new CornerData.Corner(location.x(), location.y(), location.z() -30, location.x() +29, location.y() +5, location.z() -1))){
                    for(int x = zrmin.x(); x<= zrmax.x(); x++){
                        for(int y = zrmin.y(); y<= zrmax.y(); y++){
                            for(int z = zrmin.z(); z<= zrmax.z(); z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
            }
            try{
                storageManager.save(data);
            }catch (Exception e){
                e.printStackTrace();
            }
            Title title1 = Title.title(Component.text("地图生成完成",NamedTextColor.GREEN),
                    Component.text().build(),
                    Title.Times.times(
                            Duration.ofMillis(500),
                            Duration.ofMillis(2000),
                            Duration.ofMillis(500)
                    ));
            for(Player player : players)    {game_players.add(player.getUniqueId());player.showTitle(title1);}
        }

    }
    public static void ShootLightningGun(Player p){
        if(!lightningGunServiceMap.containsKey(p.getUniqueId())) return;
        PlayerLightningGunService service = lightningGunServiceMap.get(p.getUniqueId());
        int damage =  (int)Math.ceil(100 + 400* service.process);
        Vector core = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize().multiply(0.7)).toVector();
        Vector dir = p.getEyeLocation().getDirection().normalize();
        Vector reference = Math.abs(dir.getY()) > 0.99
                ? new Vector(1, 0, 0)
                : new Vector(0, 1, 0);

        Vector u = dir.clone().crossProduct(reference).normalize();
        Vector v = dir.clone().crossProduct(u).normalize();
        World world = p.getWorld();
        for(double x = 0;x<=6;x+=0.2){
            Vector offset1 = u.clone().multiply(0.8*Math.sin(x)).add(v.clone().multiply(0.8*Math.cos(x)));
            Vector p1 = core.clone().add(offset1).add(dir.clone().multiply(x));
            Vector offset2 = u.clone().multiply(0.8*Math.sin(x+2*Math.PI/3)).add(v.clone().multiply(0.8*Math.cos(x+2*Math.PI/3)));
            Vector p2 = core.clone().add(offset2).add(dir.clone().multiply(x));
            Vector offset3 = u.clone().multiply(0.8*Math.sin(x+4*Math.PI/3)).add(v.clone().multiply(0.8*Math.cos(x+4*Math.PI/3)));
            Vector p3 = core.clone().add(offset3).add(dir.clone().multiply(x));
            world.spawnParticle(Particle.DUST,p1.toLocation(world),1,new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING),1));
            world.spawnParticle(Particle.DUST,p2.toLocation(world),1,new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING),1));
            world.spawnParticle(Particle.DUST,p3.toLocation(world),1,new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING),1));
            Vector axisPoint = core.clone().add(dir.clone().multiply(x));
            world.spawnParticle(Particle.DUST, axisPoint.toLocation(world), 1,
                    new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING), 3));
        }
        List<Entity> entities = GetCyEntity(core,world,dir,6,1.2);
        for(Entity entity : entities){
            if(entity instanceof LivingEntity l){
                if(l.getPersistentDataContainer().has(fromNamespaceKey("entity"))){

                        typeMap.put(l.getUniqueId(),DamageType.LIGHTNING);
                        l.damage(damage);
                        l.setVelocity(dir.clone().multiply(0.6).setY(0.3));

                }
            }
            if(entity.getPersistentDataContainer().has(fromNamespaceKey("grass"))){
                Collection<Entity> entities1 = entity.getNearbyEntities(8,8,8);
                LivingEntity goal = null;
                double min = Double.MAX_VALUE;
                for(Entity entity2 : entities1){
                    if(entity2 instanceof LivingEntity ll){
                        if(ll.getPersistentDataContainer().has(fromNamespaceKey("entity"))){
                            if(ll.getLocation().distanceSquared(entity2.getLocation())<=min){
                                min = ll.getLocation().distanceSquared(entity2.getLocation());
                                goal = ll;

                            }
                        }
                    }
                }
                if(goal != null){
                    Integer d = entity.getPersistentDataContainer().get(fromNamespaceKey("grass"),PersistentDataType.INTEGER);
                    if(d != null)
                        new SeedLightningTickTimer(entity.getLocation(),goal,goal.getWorld(),d).runTaskTimer(PLUGIN,0,1);
                }
                ToolClass.fillCubeWithParticles(entity.getWorld(),entity.getLocation(),0.8,10,new Particle.DustOptions(Color.GREEN,3));
                entity.getWorld().playSound(entity.getLocation(),Sound.BLOCK_GRASS_BREAK,1,1);
                entity.remove();
            }
        }
    }
    public static List<Entity> GetCyEntity(Vector core, World world, Vector dire,double length,double r){
        Vector axis = dire.clone().normalize();
        double boxR = Math.sqrt(r*r+(length/2)*(length/2));
        Location boxC = core.clone().add(axis.clone().multiply(length/2)).toLocation(world);
        Collection<Entity> ans = world.getNearbyEntities(boxC,boxR,boxR,boxR);
        List<Entity> result = new ArrayList<>();
        for(Entity entity : ans){
            Location center = entity.getBoundingBox().getCenter().toLocation(world);
            Vector vel = center.toVector().subtract(core);
            double along = vel.dot(axis);
            if(along<0||along>length) continue;
            double rq = vel.lengthSquared() - along*along;
            if(rq<=r*r){
                result.add(entity);
            }
        }
        return result;
    }
}
