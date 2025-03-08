package ru.menshovanton.hoyosubstrakcer;

public class Date {
    public int id;
    public int dayOfMonth;
    public int status;
    public int subDaysRemaining;
    public int month;

    Date(int id, int dayOfMonth, int status, int subDaysRemaining, int month) {
        this.id = id;
        this.dayOfMonth = dayOfMonth;
        this.status = status;
        this.subDaysRemaining = subDaysRemaining;
        this.month = month;
    }
}
