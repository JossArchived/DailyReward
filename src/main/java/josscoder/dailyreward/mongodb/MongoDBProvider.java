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

import java.util.*;

@Getter
public class MongoDBProvider {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;

    public void init(String host, int port, String username, String databaseId, String password) {
        MongoCredential credential = MongoCredential.createCredential(username, databaseId, password.toCharArray());

        ServerAddress serverAddress = new ServerAddress(host, port);

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(serverAddress)))
                .build();

        client = MongoClients.create(settings);
        database = client.getDatabase(databaseId);

        usersCollection = database.getCollection("users");
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
                .append(Fields.LAST_LOGIN.id(), new Date())
                .append(Fields.DATES_CLAIMED.id(), new ArrayList<>());

        usersCollection.insertOne(userDoc);
    }

    public int getConsecutiveDays(UUID uuid) {
        return getUserDoc(uuid).getInteger(Fields.CONSECUTIVE_DAYS.id());
    }

    public Date getLastLogin(UUID uuid) {
        return getUserDoc(uuid).getDate(Fields.LAST_LOGIN.id());
    }

    public List<Date> getDatesClaimed(UUID uuid) {
        return getUserDoc(uuid).getList(Fields.DATES_CLAIMED.id(), Date.class, new ArrayList<>());
    }

    private void updateFields(UUID uuid, Bson fields) {
        usersCollection.updateOne(Filters.eq(Fields.UUID.id(), uuid), fields);
    }

    public void claimDate(Date date, UUID uuid) {
        updateFields(uuid, Updates.addToSet(Fields.DATES_CLAIMED.id(), date));
    }

    public void increaseConsecutiveDays(UUID uuid) {
        int increasedConsecutiveDays = getConsecutiveDays(uuid) + 1;
        updateFields(uuid, Updates.set(Fields.CONSECUTIVE_DAYS.id(), increasedConsecutiveDays));
    }

    public void updateLastLogin(UUID uuid) {
        updateFields(uuid, Updates.set(Fields.LAST_LOGIN.id(), new Date()));
    }

    public boolean connectedToday(UUID uuid) {
        return getLastLogin(uuid).equals(new Date());
    }
}
