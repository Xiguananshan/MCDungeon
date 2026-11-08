package plugin.xiguananshan.mCDungeon.data;


public record EasyLocation(int x, int y, int z) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof EasyLocation(int x1, int y1, int z1)) {
            return x1 == this.x && y1 == this.y && z1 == this.z;
        } else {
            return false;
        }
    }

}
