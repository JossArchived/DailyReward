package josscoder.dailyreward.reward.data;

import lombok.Data;

import java.util.List;

@Data
public class Reward {

    private final int day;
    private final List<String> commands;
}
