package pink.zak.api.wavybot.config;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import org.apache.hc.core5.http.ParseException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties("credentials.spotify")
public class SpotifyConfig {
    private String clientId;
    private String clientSecret;

    private SpotifyApi spotifyApi;

    @Bean
    public SpotifyApi generateSpotifyApi(ScheduledExecutorService scheduler) throws IOException, ParseException, SpotifyWebApiException {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .build();
        this.renewCredentials(scheduler);
        return spotifyApi;
    }

    protected void renewCredentials(ScheduledExecutorService scheduler) throws IOException, org.apache.hc.core5.http.ParseException, SpotifyWebApiException {
        ClientCredentials credentials = this.spotifyApi.clientCredentials().build().execute();
        this.spotifyApi.setAccessToken(credentials.getAccessToken());
        scheduler.schedule(() -> {
            try {
                this.renewCredentials(scheduler);
            } catch (IOException | ParseException | SpotifyWebApiException e) {
                e.printStackTrace();
            }
        }, credentials.getExpiresIn(), TimeUnit.SECONDS);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
