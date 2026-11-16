package plugin.xiguananshan.mCDungeon.data;

import plugin.xiguananshan.mCDungeon.ToolClass;

import java.io.*;
import java.nio.file.*;

public class StorageManager {
    private final File file;
    public StorageManager(String file){
        this.file = new File(ToolClass.PLUGIN.getDataFolder(),file);
    }

    public void save(CornerData data) throws IOException {
        if(!ToolClass.PLUGIN.getDataFolder().exists()){
            ToolClass.PLUGIN.getDataFolder().mkdirs();
        }
        Path target = file.toPath();
        Path tmp = target.resolveSibling(file.getName() + ".tmp");
        Path backup = target.resolveSibling(file.getName() + ".bak");

        // 先写临时文件，再原子替换，避免服务器在写一半时留下损坏的 map.dat
        try(ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(tmp,
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE))
        )){
            oos.writeObject(data);
        }

        if(Files.exists(target)){
            try{
                Files.copy(target, backup, StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException ignored){
            }
        }

        try{
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }catch(AtomicMoveNotSupportedException e){
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public CornerData load() throws IOException,ClassNotFoundException{
        File backup = new File(file.getParentFile(), file.getName() + ".bak");
        if(file.exists()){
            try{
                return read(file);
            }catch(Exception e){
                // 主文件损坏时尝试备份，避免清理被完全跳过
                if(!backup.exists()) throw e;
            }
        }
        if(backup.exists()){
            return read(backup);
        }
        return null;
    }

    private CornerData read(File f) throws IOException,ClassNotFoundException{
        try(ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(f))
        )){
            return (CornerData) ois.readObject();
        }
    }
}
