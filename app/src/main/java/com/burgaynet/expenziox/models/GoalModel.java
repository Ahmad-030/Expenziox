package com.burgaynet.expenziox.models;

public class GoalModel {
    private String id;
    private String name;
    private double targetAmount;
    private double savedAmount;
    private String deadline;
    private String priority; // High, Medium, Low

    public GoalModel() {}

    public GoalModel(String id, String name, double targetAmount, double savedAmount,
                     String deadline, String priority) {
        this.id = id;
        this.name = name;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    public double getSavedAmount() { return savedAmount; }
    public void setSavedAmount(double savedAmount) { this.savedAmount = savedAmount; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public int getProgressPercent() {
        if (targetAmount <= 0) return 0;
        int pct = (int) ((savedAmount / targetAmount) * 100);
        return Math.min(pct, 100);
    }
}
