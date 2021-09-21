package pink.zak.api.riptide.models.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;
import pink.zak.api.riptide.config.serialization.TimestampDeserializer;
import pink.zak.api.riptide.models.dto.wavy.music.listens.WavyListenPage;
import pink.zak.api.riptide.models.dto.wavy.user.WavyUserDto;

import java.io.IOException;
import java.util.Date;

@Component
public class ModelBuilder {
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new SimpleModule()
                    .addDeserializer(Date.class, new TimestampDeserializer())
            );

    public WavyUserDto createWavyUserDto(String json) {
        try {
            return this.objectMapper.readValue(json, WavyUserDto[].class)[0];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public WavyListenPage createListenPage(String json) {
        try {
            return this.objectMapper.readValue(json, WavyListenPage.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
