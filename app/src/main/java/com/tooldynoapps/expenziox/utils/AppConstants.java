package com.tooldynoapps.expenziox.utils;

public class AppConstants {
    public static final String[] CATEGORIES = {"Food", "Bills", "Transport", "Entertainment", "Shopping", "Health", "Others"};
    public static final String[] PRIORITIES  = {"High", "Medium", "Low"};

    public static final int[] CATEGORY_COLORS = {
        0xFFFF6B6B, // Food - coral red
        0xFF4ECDC4, // Bills - teal
        0xFF45B7D1, // Transport - sky blue
        0xFFFFBE0B, // Entertainment - amber
        0xFFA8DADC, // Shopping - light blue
        0xFF95D5B2, // Health - mint green
        0xFFB5838D  // Others - mauve
    };

    public static final String[] CATEGORY_EMOJIS = {"🍔", "💡", "🚗", "🎬", "🛍️", "💊", "📦"};

    public static int getCategoryColor(String category) {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(category)) return CATEGORY_COLORS[i];
        }
        return 0xFF6C757D;
    }

    public static String getCategoryEmoji(String category) {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(category)) return CATEGORY_EMOJIS[i];
        }
        return "📦";
    }

    public static int getPriorityColor(String priority) {
        switch (priority) {
            case "High":   return 0xFFFF6B6B;
            case "Medium": return 0xFFFFBE0B;
            default:       return 0xFF95D5B2;
        }
    }

    public static String formatAmount(double amount) {
        if (amount >= 1_000_000) return String.format("%.1fM", amount / 1_000_000);
        if (amount >= 1_000) return String.format("%.1fK", amount / 1_000);
        return String.format("%.0f", amount);
    }
}
