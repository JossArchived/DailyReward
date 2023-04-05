package josscoder.dailyreward.session.data;

import josscoder.dailyreward.mongodb.MongoDBProvider;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
public class UserSession {

    private final UUID uuid;
    private final int consecutiveDays;
    private final List<Integer> rewardDaysClaimed;

    public boolean hasDayClaimed(int day) {
        return rewardDaysClaimed.contains(day);
    }

    public void claimDayReward(int day) {
        CompletableFuture.runAsync(() -> MongoDBProvider.getInstance().claimDayReward(day, uuid))
                .whenComplete(((unused, throwable) -> {
                    if (throwable != null) {
                        rewardDaysClaimed.add(day);
                    }
                }));
    }
}
