package br.com.cellcertobox221.app;

import android.content.Context;
import android.graphics.Color;

public class ThemeUtil {
    public static boolean isDark(Context c) {
        return c.getSharedPreferences("cellcerto", Context.MODE_PRIVATE).getBoolean("dark_mode", false);
    }
    public static void setDark(Context c, boolean value) {
        c.getSharedPreferences("cellcerto", Context.MODE_PRIVATE).edit().putBoolean("dark_mode", value).apply();
    }
    public static int bg(Context c) { return Color.parseColor(isDark(c) ? "#071219" : "#EEF0F2"); }
    public static int surface(Context c) { return Color.parseColor(isDark(c) ? "#0E2029" : "#FFFFFF"); }
    public static int surface2(Context c) { return Color.parseColor(isDark(c) ? "#152C36" : "#DDE0E3"); }
    public static int text(Context c) { return Color.parseColor(isDark(c) ? "#F5F7F8" : "#202124"); }
    public static int muted(Context c) { return Color.parseColor(isDark(c) ? "#A8BDC5" : "#5E6266"); }
    public static int accent() { return Color.parseColor("#0796AA"); }
    public static int accentBright() { return Color.parseColor("#12B8C8"); }
    public static int green() { return Color.parseColor("#20C875"); }
    public static String hex(int c) { return String.format("#%06X", (0xFFFFFF & c)); }
}
