package plugin.xiguananshan.mCDungeon;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import java.util.HashMap;
import java.util.Map;

public class ChatColorUtil {

    private static final Map<Color, ChatColor> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(Color.fromRGB(0x000000), ChatColor.BLACK);
        COLOR_MAP.put(Color.fromRGB(0x0000AA), ChatColor.DARK_BLUE);
        COLOR_MAP.put(Color.fromRGB(0x00AA00), ChatColor.DARK_GREEN);
        COLOR_MAP.put(Color.fromRGB(0x00AAAA), ChatColor.DARK_AQUA);
        COLOR_MAP.put(Color.fromRGB(0xAA0000), ChatColor.DARK_RED);
        COLOR_MAP.put(Color.fromRGB(0xAA00AA), ChatColor.DARK_PURPLE);
        COLOR_MAP.put(Color.fromRGB(0xFFAA00), ChatColor.GOLD);
        COLOR_MAP.put(Color.fromRGB(0xAAAAAA), ChatColor.GRAY);
        COLOR_MAP.put(Color.fromRGB(0x555555), ChatColor.DARK_GRAY);
        COLOR_MAP.put(Color.fromRGB(0x5555FF), ChatColor.BLUE);
        COLOR_MAP.put(Color.fromRGB(0x55FF55), ChatColor.GREEN);
        COLOR_MAP.put(Color.fromRGB(0x55FFFF), ChatColor.AQUA);
        COLOR_MAP.put(Color.fromRGB(0xFF5555), ChatColor.RED);
        COLOR_MAP.put(Color.fromRGB(0xFF55FF), ChatColor.LIGHT_PURPLE);
        COLOR_MAP.put(Color.fromRGB(0xFFFF55), ChatColor.YELLOW);
        COLOR_MAP.put(Color.fromRGB(0xFFFFFF), ChatColor.WHITE);
    }


    public static ChatColor toChatColor(Color color) {

        ChatColor exact = COLOR_MAP.get(color);
        if (exact != null) return exact;


        ChatColor closest = ChatColor.WHITE;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Color, ChatColor> entry : COLOR_MAP.entrySet()) {
            Color candidate = entry.getKey();
            double dist = colorDistance(color, candidate);
            if (dist < minDistance) {
                minDistance = dist;
                closest = entry.getValue();
            }
        }
        return closest;
    }

    private static double colorDistance(Color a, Color b) {
        int dr = a.getRed() - b.getRed();
        int dg = a.getGreen() - b.getGreen();
        int db = a.getBlue() - b.getBlue();
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }
}