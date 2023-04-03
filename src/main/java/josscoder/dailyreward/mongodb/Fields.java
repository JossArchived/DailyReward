package josscoder.dailyreward.mongodb;

public enum Fields {
    UUID,
    CONSECUTIVE_DAYS,
    LAST_LOGIN,
    DATES_CLAIMED;

    public String id() {
        return name().toLowerCase();
    }
}
