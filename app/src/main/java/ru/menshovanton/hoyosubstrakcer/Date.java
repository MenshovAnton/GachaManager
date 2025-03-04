package ru.menshovanton.hoyosubstrakcer;

public class Date {
    public int id;
    public int status;
    public int subDaysRemaining;

    Date(int _id, int _status, int _subDaysRemaining) {
        this.id = _id;
        this.status = _status;
        this.subDaysRemaining = _subDaysRemaining;
    }
}
