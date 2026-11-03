package plugin.xiguananshan.mCDungeon.data;

import plugin.xiguananshan.mCDungeon.ToolClass;

import java.io.*;

public class StorageManager {
    private final File file;
    public StorageManager(String file){
        this.file = new File(ToolClass.PLUGIN.getDataFolder(),file);
    }

    public void save(CornerData data) throws IOException {
        if(!ToolClass.PLUGIN.getDataFolder().exists()){
            ToolClass.PLUGIN.getDataFolder().mkdirs();
        }
        try(ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file))
        )){
            oos.writeObject(data);
        }
    }

    public CornerData load() throws IOException,ClassNotFoundException{
        if(!file.exists()) return null;
        try(ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file))
        )){
            return (CornerData) ois.readObject();
        }
    }
}
