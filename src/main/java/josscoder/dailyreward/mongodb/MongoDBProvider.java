package josscoder.dailyreward.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.*;

@Getter
public class MongoDBProvider {

    @Getter
    private static MongoDBProvider instance;

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> usersCollection;

    public MongoDBProvider(String host, int port, String username, String databaseId, String password) {
        MongoCredential credential = MongoCredential.createCredential(username, databaseId, password.toCharArray());

        ServerAddress serverAddress = new ServerAddress(host, port);

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(serverAddress)))
                .build();

        client = MongoClients.create(settings);
        database = client.getDatabase(databaseId);

        usersCollection = database.getCollection("users");

        instance = this;
    }

    public static void make(String host, int port, String username, String databaseId, String password) {
        new MongoDBProvider(host, port, username, databaseId, password);
    }

    public Document getUserDoc(UUID uuid) {
        return usersCollection.find(Filters.eq(Fields.UUID.id(), uuid.toString())).first();
    }

    public boolean existsUserDoc(UUID uuid) {
        return getUserDoc(uuid) != null;
    }

    public void createUserDoc(UUID uuid) {
        Document userDoc = new Document(Fields.UUID.id(), uuid.toString())
                .append(Fields.CONSECUTIVE_DAYS.id(), 1)
                .append(Fields.LAST_LOGIN_TIME.id(), LocalDate.now().toString())
                .append(Fields.REWARD_DAYS_CLAIMED.id(), new ArrayList<>());

        usersCollection.insertOne(userDoc);
    }

    public int getConsecutiveDays(UUID uuid) {
        return getUserDoc(uuid).getInteger(Fields.CONSECUTIVE_DAYS.id());
    }

    public List<Integer> getRewardsClaimed(UUID uuid) {
        return getUserDoc(uuid).getList(Fields.REWARD_DAYS_CLAIMED.id(), Integer.class, new ArrayList<>());
    }

    public boolean hasDayClaimed(int day, UUID uuid) {
        return getRewardsClaimed(uuid).contains(day);
    }

    private void updateField(UUID uuid, Bson field) {
        usersCollection.updateOne(Filters.eq(Fields.UUID.id(), uuid.toString()), field);
    }

    public void claimDayReward(Integer rewardDay, UUID uuid) {
        updateField(uuid, Updates.addToSet(Fields.REWARD_DAYS_CLAIMED.id(), rewardDay));
    }

    public void increaseConsecutiveDays(UUID uuid) {
        updateField(uuid, Updates.inc(Fields.CONSECUTIVE_DAYS.id(), 1));
    }

    public void updateLastLogin(UUID uuid) {
        updateField(uuid, Updates.set(Fields.LAST_LOGIN_TIME.id(), LocalDate.now().toString()));
    }

    public boolean connectedToday(UUID uuid) {
        Document userDoc = getUserDoc(uuid);
        if (userDoc == null) {
            return false;
        }
        String lastLogin = userDoc.getString(Fields.LAST_LOGIN_TIME.id());
        return lastLogin != null && lastLogin.equalsIgnoreCase(LocalDate.now().toString());
    }
}
