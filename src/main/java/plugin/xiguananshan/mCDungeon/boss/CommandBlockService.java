package plugin.xiguananshan.mCDungeon.boss;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.CornerData;
import plugin.xiguananshan.mCDungeon.data.EasyLocation;
import plugin.xiguananshan.mCDungeon.ticks.BossNearProtectBarService;
import plugin.xiguananshan.mCDungeon.ticks.BossService;

import java.util.*;

public class CommandBlockService extends BossService {
    private int next_skill_ticks;
    private SkillType next_skill;
    public boolean in_skill;
    public CornerData.Corner room;
    public CommandBlockService(LivingEntity entity, CornerData.Corner room) {
        next_skill_ticks = -1;
        next_skill = null;
        in_skill = false;
        this.room = room;
        super(entity);
    }

    @Override
    public void run() {

        if(next_skill_ticks <=0 && !in_skill && next_skill == null){
            Random random = new Random();
            next_skill_ticks = random.nextInt(320)+100;
            next_skill = SkillType.values()[random.nextInt(SkillType.values().length)];
            in_skill = false;
        }
        if(next_skill != null && next_skill_ticks > 0){
            next_skill_ticks--;
        }
        if(next_skill_ticks<=0 && next_skill != null && !in_skill){
            in_skill = true;
            Random random = new Random();
            SkillType type = SkillType.values()[random.nextInt(SkillType.values().length)];
            /*
            switch (type){
                case KILL -> new KillService(this).runTaskTimer(ToolClass.PLUGIN,0,1);
                case LIGHTNING -> new LightningService(this).runTaskTimer(ToolClass.PLUGIN,0,1);
            }
            */
            new LightningService(this).runTaskTimer(ToolClass.PLUGIN,0,1);

            next_skill = null;
        }
        super.run();
    }

    enum SkillType{
        KILL,
        LIGHTNING,
        SPAWN,
        SHOOT
    }

    static class KillService extends BukkitRunnable {
        private final  CommandBlockService service;
        private int ticks;
        private boolean active;
        private boolean underLineActive;
        public KillService(CommandBlockService service){
            this.service = service;
            this.ticks = 0;
            this.active = true;
        }

        @Override
        public void run() {
            if(!service.getEntity().isValid()){
                cancel();
                return;
            }
            World world = Bukkit.getWorld("world");
            assert  world != null;
            service.getEntity().setAI(false);
            this.ticks++;
            if(ticks == 1){
                List<Entity> entities = Bukkit.selectEntities(Bukkit.getConsoleSender(),"@e");
                for(Entity entity : entities){
                    if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("CommandBlockSpawn"))){
                        entity.remove();
                    }
                }

                for(UUID uuid : ToolClass.game_players){

                    if(!ToolClass.died_players.contains(uuid)){
                        Player p = Bukkit.getPlayer(uuid);
                        if(p != null){
                            p.teleport(new Location(world,service.room.minx+1,service.room.miny+2,service.room.minz+14));
                            p.sendMessage(Component.text("CommandBlock正在输入kill指令，请在输入完成之前把他的护盾打掉，否则会有随机有一个玩家倒地！", NamedTextColor.RED));
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HARP,3,1);
                        }
                    }
                }
                service.getEntity().teleport(new Location(world,service.room.minx+28,service.room.miny+1,service.room.minz+14,90,0));
                service.getEntity().getPersistentDataContainer().set(ToolClass.fromNamespaceKey("NearProtect"),PersistentDataType.INTEGER,1000);
                service.getEntity().getPersistentDataContainer().set(ToolClass.fromNamespaceKey("MaxNearProtect"),PersistentDataType.INTEGER,1000);
                new BossNearProtectBarService(service.getEntity(),Component.text("近战护盾",NamedTextColor.WHITE), BossBar.Color.WHITE).runTaskTimer(ToolClass.PLUGIN,0,1);
                return;
            }

            Integer protect = service.getEntity().getPersistentDataContainer().get(ToolClass.fromNamespaceKey("NearProtect"),PersistentDataType.INTEGER);
            assert protect != null;
            if(ticks <30*20 && protect <= 0 && this.active){
                this.active  = false;
                this.ticks = 1;
                for(UUID uuid : ToolClass.game_players){
                    if(!ToolClass.died_players.contains(uuid)){
                        Player p = Bukkit.getPlayer(uuid);
                        if(p != null){
                            p.sendMessage(Component.text("CommandBlock瘫痪了！趁现在抓紧攻击！", NamedTextColor.GREEN));
                            p.playSound(p,Sound.ENTITY_SKELETON_DEATH,3,1);
                        }
                    }
                }
            }
            if(ticks > 15*20 && !this.active){
                service.getEntity().setAI(true);
                service.in_skill = false;
                world.spawnParticle(Particle.EXPLOSION,service.getEntity().getLocation().clone().add(0,1,0),20);
                world.playSound(service.getEntity(),Sound.ENTITY_GENERIC_EXPLODE,3,1);
                service.getEntity().setAI(true);
                cancel();
                return;
            }
            if(ticks <=30*20 && this.active){
                if(ticks%10 == 0){
                    for(double x = service.room.minz ; x<= service.room.maxz;x+=1.0){
                        world.spawnParticle(Particle.DUST,service.room.minx+27,service.room.miny+1,x,1,new Particle.DustOptions(Color.GREEN,1.5f));
                        world.spawnParticle(Particle.DUST,service.room.minx+2,service.room.miny+1,x,1,new Particle.DustOptions(Color.RED,1.5f));
                    }
                }
                if(ticks%20==0 && !underLineActive){
                    underLineActive = true;
                    new UnderLineService(service.room.minx+27,service.room.minx+2).runTaskTimer(ToolClass.PLUGIN,0,1);
                }
            }
            if(ticks>= 30*20 && this.active){
                for(UUID uuid : ToolClass.game_players){
                    if(!ToolClass.died_players.contains(uuid)){
                        Player p = Bukkit.getPlayer(uuid);
                        if(p != null){
                            p.sendMessage(Component.text("CommandBlock：受死吧！", NamedTextColor.DARK_RED, TextDecoration.BOLD));

                        }
                    }
                }
                UUID alive_player;
                Random random = new Random();
                if(ToolClass.died_players.size() < ToolClass.game_players.size()){
                    do{
                        alive_player = ToolClass.game_players.get(random.nextInt(ToolClass.game_players.size()));
                    }while (ToolClass.died_players.contains(alive_player));
                    Player p = Bukkit.getPlayer(alive_player);
                    if(p!=null){
                        p.setHealth(0);
                    }
                }
                service.in_skill = false;
                service.getEntity().setAI(true);
                cancel();


            }


        }

        class UnderLineService extends BukkitRunnable{
            private static final double ROW_STEP = 2.0;
            private static final double PARTICLE_STEP = 1.0;
            private final double end;
            private double now;
            private int ticks;
            public UnderLineService(double start,double end){
                this.end = end;
                this.ticks = 0;
                this.now = start;
            }
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                assert world != null;
                Collection<Entity> entities = world.getNearbyEntities(BoundingBox.of(new Vector(now -1,service.room.miny+1,service.room.minz),new Vector(now + 1,service.room.miny+1.5,service.room.maxz)));
                for(Entity entity : entities){
                    if(entity instanceof Player p){
                        if(ToolClass.game_players.contains(p.getUniqueId()) && !ToolClass.died_players.contains(p.getUniqueId())){
                            p.setVelocity(new Vector(-3,0.5,0));
                        }
                    }
                }
                if(this.ticks % 5 == 0){
                    for(double z = service.room.minz; z <= service.room.maxz; z += PARTICLE_STEP){
                        world.spawnParticle(Particle.DUST,this.now,service.room.miny+1,z,1,new Particle.DustOptions(Color.YELLOW,1.5f));
                    }

                    this.now -= ROW_STEP;
                }
                if(this.now < this.end){
                    underLineActive = false;
                    cancel();
                }
                this.ticks++;
            }
        }

    }
    static class LightningService extends BukkitRunnable{
        private final CommandBlockService service;
        private final Map<EasyLocation,Material> blocks;
        private int ticks;
        private int times;
        public LightningService(CommandBlockService service){
            this.service = service;
            this.blocks = new HashMap<>();
            this.ticks = 0;
            this.times = 0;
        }

        @Override
        public void run() {
            World world = Bukkit.getWorld("world");

            assert world != null;
            this.ticks++;
            if(times > 3){
                service.in_skill = false;
                cancel();
                return;
            }
            if(ticks==1){
                blocks.clear();
                Random random = new Random();
                int count = random.nextInt(31) + 10;
                CornerData.Corner room = service.room;

                while(blocks.size() < count){
                    int x,z;
                    x = random.nextInt(room.maxx - room.minx) + room.minx;
                    z = random.nextInt(room.maxz - room.minz) + room.minz;
                    Block block = world.getBlockAt(x,100,z);
                    blocks.put(new EasyLocation(x,100,z),block.getType());
                }
                for(EasyLocation loc : blocks.keySet()){
                    world.getBlockAt(loc.x(),loc.y(),loc.z()).setType(Material.BEDROCK);
                }
            }
            if(ticks == 80){
                for(EasyLocation loc : blocks.keySet()){
                    LightningStrike strike = world.spawn(new Location(world,loc.x(),loc.y(),loc.z()), LightningStrike.class);
                    strike.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("spawn"),PersistentDataType.BOOLEAN,true);
                    world.getBlockAt(loc.x(),loc.y(), loc.z()).setType(blocks.get(loc));
                }
                times++;
                ticks = 0;
            }
        }
    }
}
