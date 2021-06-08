package pink.zak.api.wavybot.config;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import org.apache.hc.core5.http.ParseException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties("credentials.spotify")
public class SpotifyConfig {
    private String clientId;
    private String clientSecret;

    private SpotifyApi spotifyApi;

    @Bean
    public SpotifyApi generateSpotifyApi(ThreadPoolTaskScheduler scheduler) throws IOException, ParseException, SpotifyWebApiException {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .build();
        this.renewCredentials(scheduler);
        return spotifyApi;
    }

    protected void renewCredentials(ThreadPoolTaskScheduler scheduler) throws IOException, org.apache.hc.core5.http.ParseException, SpotifyWebApiException {
        ClientCredentials credentials = this.spotifyApi.clientCredentials().build().execute();
        this.spotifyApi.setAccessToken(credentials.getAccessToken());
        scheduler.schedule(() -> {
            try {
                this.renewCredentials(scheduler);
            } catch (IOException | ParseException | SpotifyWebApiException e) {
                e.printStackTrace();
            }
        }, Instant.now().plus(credentials.getExpiresIn(), ChronoUnit.MILLIS));
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
