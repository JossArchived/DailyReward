package josscoder.dailyreward.mongodb;

public enum Fields {
    UUID,
    CONSECUTIVE_DAYS,
    LAST_LOGIN_TIME,
    REWARD_DAYS_CLAIMED;

    public String id() {
        return name().toLowerCase();
    }
}
