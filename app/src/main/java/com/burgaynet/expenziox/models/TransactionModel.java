package com.burgaynet.expenziox.models;

public class TransactionModel {
    private String id;
    private double amount;
    private String category;
    private String type; // "expense" or "income"
    private String date;
    private String note;
    private long timestamp;

    public TransactionModel() {}

    public TransactionModel(String id, double amount, String category, String type,
                            String date, String note, long timestamp) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        this.note = note;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public boolean isExpense() { return "expense".equalsIgnoreCase(type); }
}
