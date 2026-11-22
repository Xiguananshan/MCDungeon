package plugin.xiguananshan.mCDungeon.data.blockgit;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockSnapShot {
    private final List<SerializedBlock> blocks;
    public BlockSnapShot(Block block){
        this.blocks = new ArrayList<>();
        this.blocks.add(new SerializedBlock(block));
    }
    public BlockSnapShot(List<SerializedBlock> blocks){
        this.blocks = blocks;
    }
}
