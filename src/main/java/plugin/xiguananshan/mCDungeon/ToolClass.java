package plugin.xiguananshan.mCDungeon;

import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
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
import plugin.xiguananshan.mCDungeon.ticks.DamageTextTick;
import plugin.xiguananshan.mCDungeon.ticks.PlayerRockDefenceTickTimer;
import plugin.xiguananshan.mCDungeon.ticks.SeedLightningTickTimer;

import javax.naming.Name;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ToolClass {
    public static MCDungeon PLUGIN = MCDungeon.getPlugin(MCDungeon.class);
    public static ItemStack DefaultGun = new ItemStack(Material.GOLDEN_HOE);
    public static Map<UUID,Long> Shoot_timer = new HashMap<>();
    public static Map<UUID,DamageType> typeMap = new HashMap<>();
    public static Map<UUID, HashSet<DamageType>> entity_type = new HashMap<>();
    public static Map<UUID,Map<DamageType,Integer>> element_type = new HashMap<>();
    public static Map<UUID,Integer> freeze_tick = new HashMap<>();
    public static Map<UUID,Integer> grass_tick = new HashMap<>();
    public static Map<UUID,Integer> grass_burn = new HashMap<>();
    public static Map<UUID,Integer> physic_attack = new HashMap<>();
    public static Map<UUID,Integer> defence = new HashMap<>();
    public static Map<UUID, PlayerRockDefenceTickTimer> rockDefenceTickTimerMap = new HashMap<>();
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
        pdc.set(fromNamespaceKey("element"),PersistentDataType.STRING,"LIGHTNING");
        meta.setLore(List.of(ChatColor.YELLOW + "元素：" + ChatColorUtil.toChatColor(typeColorMap.get(DamageType.LIGHTNING)) + typeNameMap.get(DamageType.LIGHTNING)) );
        DefaultGun.setItemMeta(meta);
    }
    public static void SummonEntity(World world, Location location, List<ItemStack> equipments,boolean ai,int health,Class<? extends Mob> type){
        Mob entity = world.spawn(location, type);
        entity.setAI(ai);
        EntityEquipment equipment = entity.getEquipment();
        assert equipment != null;
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
        entity.setCustomName(ChatColor.YELLOW + "剩余血量：" + ChatColor.RED + health);

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
    public static void SpawnDamageEntity(int damage,World world,Location location,ChatColor color){
        TextDisplay entity = world.spawn(location, TextDisplay.class);
        entity.setText(color +"-" + damage);
        entity.setBillboard(Display.Billboard.CENTER);
        entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        entity.setShadowed(true);
        entity.setPersistent(false);
        BukkitRunnable runnable = new DamageTextTick(entity,0.5f);
        runnable.runTaskTimer(PLUGIN,0,1);
    }
    public static void SpawnTextEntity(String text,World world,Location location){
        TextDisplay entity = world.spawn(location, TextDisplay.class);
        entity.setText(text);
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
            for(Player player : players)    {game_players.add(player.getUniqueId());player.sendTitle(ChatColor.YELLOW + "正在生成地图","",10,40,10);}
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
                            s.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world,location.x+30,location.y,location.z),CrossStructureType.XA);
                            CrossData c2 = new CrossData(new Location(world,location.x,location.y,location.z-30),CrossStructureType.ZR);
                            CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z+30),CrossStructureType.ZA);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
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
                            s.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world,location.x-30,location.y,location.z),CrossStructureType.XR);
                            CrossData c2 = new CrossData(new Location(world,location.x,location.y,location.z-30),CrossStructureType.ZR);
                            CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z+30),CrossStructureType.ZA);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
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
                            s.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world,location.x-30,location.y,location.z),CrossStructureType.XR);
                            CrossData c2 = new CrossData(new Location(world,location.x+30,location.y,location.z),CrossStructureType.XA);
                            CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z+30),CrossStructureType.ZA);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
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
                            s.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                            CrossData c1 = new CrossData(new Location(world,location.x+30,location.y,location.z),CrossStructureType.XA);
                            CrossData c2 = new CrossData(new Location(world,location.x-30,location.y,location.z),CrossStructureType.XR);
                            CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z-30),CrossStructureType.ZR);
                            spawn.add(c1);
                            spawn.add(c2);
                            spawn.add(c3);
                            data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
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
                                    BOSS_ROOM_1_1.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world,location.x+30,location.y,location.z),CrossStructureType.XA);
                                    CrossData c2 = new CrossData(new Location(world,location.x,location.y,location.z-30),CrossStructureType.ZR);
                                    CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z+30),CrossStructureType.ZA);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));

                                }
                            }
                            case XR -> {
                                CROSS_ROOM_1_2.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX()-30,l.l.getBlockY(),l.l.getBlockZ());
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world,location.x-30,location.y,location.z),CrossStructureType.XR);
                                    CrossData c2 = new CrossData(new Location(world,location.x,location.y,location.z-30),CrossStructureType.ZR);
                                    CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z+30),CrossStructureType.ZA);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
                                }
                            }
                            case ZA -> {
                                CROSS_ROOM_1_1.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX(),l.l.getBlockY(),l.l.getBlockZ()+30);
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world,location.x-30,location.y,location.z),CrossStructureType.XR);
                                    CrossData c2 = new CrossData(new Location(world,location.x+30,location.y,location.z),CrossStructureType.XA);
                                    CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z+30),CrossStructureType.ZA);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
                                }
                            }
                            case ZR -> {
                                CROSS_ROOM_1_1.place(l.l,false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                EasyLocation location = new EasyLocation(l.l.getBlockX(),l.l.getBlockY(),l.l.getBlockZ()-30);
                                data.corners.add(new CornerData.Corner(finalL1.l.getBlockX(),finalL1.l.getBlockY(),finalL1.l.getBlockZ(),finalL1.l.getBlockX()+29,finalL1.l.getBlockY()+5,finalL1.l.getBlockZ()+29));
                                if(!rooms.contains(location)){
                                    rooms.add(location);
                                    BOSS_ROOM_1_1.place(new Location(world,location.x,location.y,location.z),false,StructureRotation.NONE,Mirror.NONE,0,1,rand);
                                    CrossData c1 = new CrossData(new Location(world,location.x+30,location.y,location.z),CrossStructureType.XA);
                                    CrossData c2 = new CrossData(new Location(world,location.x-30,location.y,location.z),CrossStructureType.XR);
                                    CrossData c3 = new CrossData(new Location(world,location.x,location.y,location.z-30),CrossStructureType.ZR);
                                    spawn.add(c1);
                                    spawn.add(c2);
                                    spawn.add(c3);
                                    boss++;
                                    data.corners.add(new CornerData.Corner(location.x, location.y,location.z, location.x+29, location.y+5, location.z+29));
                                }
                            }

                        }
                    }
                }
            }
            for(EasyLocation location : rooms){
                EasyLocation xamin,xamax,xrmin,xrmax,zamin,zamax,zrmin,zrmax;
                xamin = new EasyLocation(location.x+29,location.y+1, location.z+14);
                xamax = new EasyLocation(location.x+29, location.y+3,location.z+15);
                xrmin = new EasyLocation(location.x,location.y+1, location.z+14);
                xrmax = new EasyLocation(location.x, location.y+3, location.z+15);
                zamin = new EasyLocation(location.x+14, location.y+1, location.z+29);
                zamax = new EasyLocation(location.x+15, location.y+3, location.z+29);
                zrmin = new EasyLocation(location.x+14,location.y+1,location.z);
                zrmax = new EasyLocation(location.x+15, location.y+3,location.z);
                if(!data.corners.contains(new CornerData.Corner(location.x+30, location.y, location.z, location.x+59, location.y+5, location.z+29))){
                    for(int x = xamin.x;x<=xamax.x;x++){
                        for(int y = xamin.y;y<=xamax.y;y++){
                            for(int z = xamin.z;z<= xamax.z;z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
                if(!data.corners.contains(new CornerData.Corner(location.x-30, location.y, location.z, location.x-1, location.y+5, location.z+29))){
                    for(int x = xrmin.x;x<=xrmax.x;x++){
                        for(int y = xrmin.y;y<=xrmax.y;y++){
                            for(int z = xrmin.z;z<= xrmax.z;z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
                if(!data.corners.contains(new CornerData.Corner(location.x, location.y, location.z+30, location.x+29, location.y+5, location.z+59))){
                    for(int x = zamin.x;x<=zamax.x;x++){
                        for(int y = zamin.y;y<=zamax.y;y++){
                            for(int z = zamin.z;z<= zamax.z;z++){
                                world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS,false);
                            }
                        }
                    }
                }
                if(!data.corners.contains(new CornerData.Corner(location.x, location.y, location.z-30, location.x+29, location.y+5, location.z-1))){
                    for(int x = zrmin.x;x<=zrmax.x;x++){
                        for(int y = zrmin.y;y<=zrmax.y;y++){
                            for(int z = zrmin.z;z<= zrmax.z;z++){
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
            for(Player player : players)    {game_players.add(player.getUniqueId());player.sendTitle(ChatColor.GREEN + "地图生成完成","",10,40,40);}
        }

    }
}
