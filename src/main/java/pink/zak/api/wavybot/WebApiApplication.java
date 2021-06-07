package pink.zak.api.wavybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import pink.zak.api.wavybot.models.server.Server;
import pink.zak.api.wavybot.models.user.User;
import pink.zak.api.wavybot.models.user.WavyUser;
import pink.zak.api.wavybot.models.user.music.MusicData;

import java.util.UUID;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class WebApiApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebApiApplication.class, args);
        //test(context);
    }

    public static void test(ApplicationContext context) {
        MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
        WavyUser wavyUser = new WavyUser(UUID.fromString("51c0787c-f111-4858-9eda-5be9864fe0f0"), "Zak", "gtzhk8yungah3aciviyhx5xo3", "Zak", 240721111174610945L);
        mongoTemplate.save(wavyUser);

        User user = new User(240721111174610945L);
        user.setWavyUuid(wavyUser.getWavyUuid());
        mongoTemplate.save(user);

        MusicData musicData = new MusicData(wavyUser.getWavyUuid(), user.getDiscordId());
        mongoTemplate.save(musicData);

        Server server = new Server(751886048623067186L);
        server.getLinkedUsers().add(user.getDiscordId());
        mongoTemplate.save(server);

        System.out.println(mongoTemplate.findById(240721111174610945L, User.class));
        System.out.println(mongoTemplate.findById(751886048623067186L, Server.class));

    }
}
