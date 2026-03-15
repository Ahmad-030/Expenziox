package com.burgaynet.expenziox.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.burgaynet.expenziox.models.GoalModel;
import com.burgaynet.expenziox.models.TransactionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String PREFS_NAME       = "ExpenzioXPrefs";
    private static final String KEY_TRANSACTIONS = "transactions";
    private static final String KEY_GOALS        = "goals";
    private static final String KEY_BUDGETS      = "budgets";
    private static final String KEY_USER_NAME    = "user_name";
    private static final String KEY_ONBOARDED    = "onboarded";
    private static final String KEY_THEME        = "theme_dark";
    private static final String KEY_PIN          = "pin_code";
    private static final String KEY_PIN_ENABLED  = "pin_enabled";
    private static final String KEY_NOTIFICATIONS= "notifications";
    private static final String KEY_AUTO_RESET   = "auto_reset";

    private final SharedPreferences prefs;

    public DataManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // ─── USER ────────────────────────────────────────────────
    public String getUserName() { return prefs.getString(KEY_USER_NAME, ""); }
    public void setUserName(String name) { prefs.edit().putString(KEY_USER_NAME, name).apply(); }
    public boolean isOnboarded() { return prefs.getBoolean(KEY_ONBOARDED, false); }
    public void setOnboarded(boolean v) { prefs.edit().putBoolean(KEY_ONBOARDED, v).apply(); }

    // ─── SETTINGS ────────────────────────────────────────────
    public boolean isDarkTheme() { return prefs.getBoolean(KEY_THEME, true); }
    public void setDarkTheme(boolean v) { prefs.edit().putBoolean(KEY_THEME, v).apply(); }
    public String getPin() { return prefs.getString(KEY_PIN, ""); }
    public void setPin(String pin) { prefs.edit().putString(KEY_PIN, pin).apply(); }
    public boolean isPinEnabled() { return prefs.getBoolean(KEY_PIN_ENABLED, false); }
    public void setPinEnabled(boolean v) { prefs.edit().putBoolean(KEY_PIN_ENABLED, v).apply(); }
    public boolean isNotificationsEnabled() { return prefs.getBoolean(KEY_NOTIFICATIONS, true); }
    public void setNotifications(boolean v) { prefs.edit().putBoolean(KEY_NOTIFICATIONS, v).apply(); }
    public boolean isAutoReset() { return prefs.getBoolean(KEY_AUTO_RESET, false); }
    public void setAutoReset(boolean v) { prefs.edit().putBoolean(KEY_AUTO_RESET, v).apply(); }

    // ─── TRANSACTIONS ─────────────────────────────────────────
    public void addTransaction(TransactionModel t) {
        List<TransactionModel> list = getTransactions();
        list.add(0, t);
        saveTransactions(list);
    }

    public void deleteTransaction(String id) {
        List<TransactionModel> list = getTransactions();
        list.removeIf(t -> t.getId().equals(id));
        saveTransactions(list);
    }

    public List<TransactionModel> getTransactions() {
        List<TransactionModel> list = new ArrayList<>();
        String json = prefs.getString(KEY_TRANSACTIONS, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                TransactionModel t = new TransactionModel();
                t.setId(o.optString("id"));
                t.setAmount(o.optDouble("amount", 0));
                t.setCategory(o.optString("category"));
                t.setType(o.optString("type"));
                t.setDate(o.optString("date"));
                t.setNote(o.optString("note"));
                t.setTimestamp(o.optLong("timestamp", 0));
                list.add(t);
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return list;
    }

    private void saveTransactions(List<TransactionModel> list) {
        JSONArray arr = new JSONArray();
        for (TransactionModel t : list) {
            try {
                JSONObject o = new JSONObject();
                o.put("id", t.getId());
                o.put("amount", t.getAmount());
                o.put("category", t.getCategory());
                o.put("type", t.getType());
                o.put("date", t.getDate());
                o.put("note", t.getNote());
                o.put("timestamp", t.getTimestamp());
                arr.put(o);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        prefs.edit().putString(KEY_TRANSACTIONS, arr.toString()).apply();
    }

    public double getTotalExpenseThisMonth() {
        double total = 0;
        String monthKey = getCurrentMonthKey();
        for (TransactionModel t : getTransactions()) {
            if (t.isExpense() && t.getDate().startsWith(monthKey)) total += t.getAmount();
        }
        return total;
    }

    public double getTotalIncomeThisMonth() {
        double total = 0;
        String monthKey = getCurrentMonthKey();
        for (TransactionModel t : getTransactions()) {
            if (!t.isExpense() && t.getDate().startsWith(monthKey)) total += t.getAmount();
        }
        return total;
    }

    public double getCategoryExpense(String category) {
        double total = 0;
        String monthKey = getCurrentMonthKey();
        for (TransactionModel t : getTransactions()) {
            if (t.isExpense() && t.getCategory().equals(category)
                    && t.getDate().startsWith(monthKey)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    private String getCurrentMonthKey() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return String.format(java.util.Locale.getDefault(), "%04d-%02d",
                cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1);
    }

    // ─── BUDGETS ─────────────────────────────────────────────
    public void setBudget(String category, double limit) {
        try {
            JSONObject budgets = getBudgetsJson();
            JSONObject cat = new JSONObject();
            cat.put("limit", limit);
            budgets.put(category, cat);
            prefs.edit().putString(KEY_BUDGETS, budgets.toString()).apply();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    public double getBudgetLimit(String category) {
        try {
            JSONObject budgets = getBudgetsJson();
            if (budgets.has(category)) return budgets.getJSONObject(category).optDouble("limit", 0);
        } catch (JSONException e) { e.printStackTrace(); }
        return 0;
    }

    private JSONObject getBudgetsJson() {
        try { return new JSONObject(prefs.getString(KEY_BUDGETS, "{}")); }
        catch (JSONException e) { return new JSONObject(); }
    }

    // ─── GOALS ───────────────────────────────────────────────
    public void addGoal(GoalModel g) {
        List<GoalModel> list = getGoals();
        list.add(g);
        saveGoals(list);
    }

    public void updateGoal(GoalModel updated) {
        List<GoalModel> list = getGoals();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(updated.getId())) { list.set(i, updated); break; }
        }
        saveGoals(list);
    }

    public void deleteGoal(String id) {
        List<GoalModel> list = getGoals();
        list.removeIf(g -> g.getId().equals(id));
        saveGoals(list);
    }

    public List<GoalModel> getGoals() {
        List<GoalModel> list = new ArrayList<>();
        String json = prefs.getString(KEY_GOALS, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                GoalModel g = new GoalModel();
                g.setId(o.optString("id"));
                g.setName(o.optString("name"));
                g.setTargetAmount(o.optDouble("targetAmount", 0));
                g.setSavedAmount(o.optDouble("savedAmount", 0));
                g.setDeadline(o.optString("deadline"));
                g.setPriority(o.optString("priority"));
                list.add(g);
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return list;
    }

    private void saveGoals(List<GoalModel> list) {
        JSONArray arr = new JSONArray();
        for (GoalModel g : list) {
            try {
                JSONObject o = new JSONObject();
                o.put("id", g.getId());
                o.put("name", g.getName());
                o.put("targetAmount", g.getTargetAmount());
                o.put("savedAmount", g.getSavedAmount());
                o.put("deadline", g.getDeadline());
                o.put("priority", g.getPriority());
                arr.put(o);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        prefs.edit().putString(KEY_GOALS, arr.toString()).apply();
    }
}
