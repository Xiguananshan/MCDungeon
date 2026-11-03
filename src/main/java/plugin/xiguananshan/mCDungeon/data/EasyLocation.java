package plugin.xiguananshan.mCDungeon.data;

import com.google.common.hash.HashCode;

import java.util.Objects;

public class EasyLocation {
    public final int x, y,z;
    public EasyLocation(int x,int y,int z){
        this.x = x;
        this.y =y;
        this.z =z;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj instanceof EasyLocation e){
            return e.x==this.x && e.y ==this.y && e.z == this.z;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y,z);
    }
}
