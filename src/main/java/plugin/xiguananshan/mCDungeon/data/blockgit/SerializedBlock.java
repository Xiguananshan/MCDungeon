package plugin.xiguananshan.mCDungeon.data.blockgit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

public class SerializedBlock {
    private final CompoundTag state;
    private CompoundTag entity;
    public SerializedBlock(Block block){
        CraftWorld world = (CraftWorld) block.getWorld();
        BlockPos pos = new BlockPos(block.getX(),block.getY(),block.getZ());
        BlockState blockState = world.getHandle().getBlockState(pos);
        this.state = NbtUtils.writeBlockState(blockState);
        BlockEntity blockEntity = world.getHandle().getBlockEntity(pos);
        if(blockEntity != null){
            this.entity = blockEntity.saveWithFullMetadata(world.getHandle().registryAccess());
        }
    }
    public CompoundTag getBlockStateTag(){
        return this.state;
    }
    public CompoundTag getBlockEntityTag(){
        return this.entity;
    }
}
