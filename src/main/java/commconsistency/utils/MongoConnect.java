package commconsistency.utils;

import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongoConnect {

	public static MongoClient getClient() {
		MongoClientOptions clientOptions = new MongoClientOptions.Builder().connectionsPerHost(50).maxWaitTime(2000)
				.build();
		List<MongoCredential> lstCredentials = Arrays
				.asList(MongoCredential.createMongoCRCredential("liuzhiyong", "admin", "123456".toCharArray()));
		MongoClient client = new MongoClient(new ServerAddress("192.168.1.60"), lstCredentials, clientOptions);
		return client;
	}
}
