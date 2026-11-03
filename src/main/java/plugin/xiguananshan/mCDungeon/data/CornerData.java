package plugin.xiguananshan.mCDungeon.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class CornerData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public ArrayList<Corner> corners;
    public CornerData(){corners = new ArrayList<>();}

    public static class Corner implements Serializable{
        public int minx,miny,minz,maxx,maxy,maxz;
        @Serial
        private static final long serialVersionUID = 2L;
        public Corner(int minx,int miny,int minz,int maxx,int maxy,int maxz){this.minx = minx;this.miny = miny;this.minz = minz;this.maxx = maxx;this.maxy = maxy;this.maxz = maxz;};

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj instanceof Corner c){
                return c.minx == minx && c.maxx == maxx && c.miny == miny && c.maxy == maxy && c.maxz == maxz && c.minz == minz;
            }else{
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(minx,miny,minz,maxx,maxy,maxz);
        }
    }
}
