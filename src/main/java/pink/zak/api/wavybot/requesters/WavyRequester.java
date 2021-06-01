package pink.zak.api.wavybot.requesters;

import com.google.common.collect.Sets;
import com.mongodb.lang.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import pink.zak.api.wavybot.models.builder.ModelBuilder;
import pink.zak.api.wavybot.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.wavybot.models.dto.wavy.music.listens.WavyListenPage;
import pink.zak.api.wavybot.models.dto.wavy.user.WavyUserDto;
import pink.zak.api.wavybot.models.task.Task;
import pink.zak.api.wavybot.services.TaskService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class WavyRequester {
    private final ModelBuilder modelBuilder;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Executor executor;
    private final TaskService taskService;

    private static final String SONG_HISTORY_BASE_URL = "https://wavy.fm/api/internal/legacy/profile/listens/%s?live=false&page=%s&size=100";
    private static final String SONG_HISTORY_SINCE_BASE_URL = "https://wavy.fm/api/internal/legacy/profile/listens/%s?live=false&page=%s&since=%s&size=100";
    private static final String PROFILE_DATA_BASE_URL = "https://wavy.fm/api/internal/legacy/profiles?username=%s";

    @Autowired
    public WavyRequester(ModelBuilder modelBuilder, Executor executor, TaskService taskService) {
        this.modelBuilder = modelBuilder;
        this.executor = executor;
        this.taskService = taskService;
    }

    @NonNull
    @Async
    public ListenableFuture<WavyUserDto> retrieveWavyUser(@NonNull String username) {
        System.out.println("Getting wavy user");
        Request request = new Request.Builder()
                .url(String.format(PROFILE_DATA_BASE_URL, username))
                .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody == null)
                throw new RuntimeException("Response body null for retrieveUser request");
            return new AsyncResult<>(this.modelBuilder.createWavyUserDto(responseBody.string()));
        } catch (IOException e) {
            e.printStackTrace();
            return new AsyncResult<>(null);
        }
    }

    public Task<Set<WavyListenDto>> retrieveAllListens(UUID uuid) {
        Task<Set<WavyListenDto>> task = Task.create();
        task.setFuture(CompletableFuture.supplyAsync(() -> {
            Set<WavyListenDto> listens = Sets.newConcurrentHashSet();
            this.retrieveListenPage(uuid, 0).thenAccept(wavyListenPage -> {
                listens.addAll(wavyListenPage.getTracks());
                int totalTracks = wavyListenPage.getTotalTracks();
                task.setRequiredProgress(totalTracks);
                task.updateProgress(current -> current + wavyListenPage.getTracks().size());
                if (totalTracks > 100) {
                    int requiredPages = (int) Math.ceil(totalTracks / 100.0);
                    Set<CompletableFuture<?>> futures = new HashSet<>();
                    for (int page = 1; page < requiredPages; page++) {
                        futures.add(this.retrieveListenPage(uuid, page).thenAccept(wavyPage -> {
                            task.updateProgress(current -> current + wavyPage.getTracks().size());
                            listens.addAll(wavyPage.getTracks());
                        }));
                    }
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).join();
                }
            }).join();
            return listens;
        }, this.executor));
        this.taskService.addTask(task);
        return task;
    }

    public CompletableFuture<WavyListenPage> retrieveListenPage(UUID uuid, int page) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(String.format(SONG_HISTORY_BASE_URL, uuid, page))
                    .build();
            try (Response response = this.httpClient.newCall(request).execute()) {
                ResponseBody responseBody = response.body();
                if (responseBody == null)
                    throw new RuntimeException("Response body null for retrieveUser request");
                return this.modelBuilder.createListenPage(responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, this.executor);
    }

    public Task<Set<WavyListenDto>> retrieveListensSince(UUID uuid, long since) {
        Task<Set<WavyListenDto>> task = Task.create();
        task.setFuture(CompletableFuture.supplyAsync(() -> {
            Set<WavyListenDto> listens = Sets.newConcurrentHashSet();
            this.retrieveListensSincePage(uuid, 0, since).thenAccept(wavyListenPage -> {
                listens.addAll(wavyListenPage.getTracks());
                int totalTracks = wavyListenPage.getTotalTracks();
                task.setRequiredProgress(totalTracks);
                task.updateProgress(current -> current + wavyListenPage.getTracks().size());
                if (totalTracks > 100) {
                    int requiredPages = (int) Math.ceil(totalTracks / 100.0);
                    Set<CompletableFuture<?>> futures = new HashSet<>();
                    for (int page = 1; page <= requiredPages; page++) {
                        futures.add(this.retrieveListensSincePage(uuid, page, since).thenAccept(wavyPage -> {
                            task.updateProgress(current -> current + wavyPage.getTracks().size());
                            listens.addAll(wavyPage.getTracks());
                        }));
                    }
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).join();
                }
            }).join();
            return listens;
        }, this.executor));
        this.taskService.addTask(task);
        return task;
    }

    public CompletableFuture<WavyListenPage> retrieveListensSincePage(UUID uuid, int page, long since) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(String.format(SONG_HISTORY_SINCE_BASE_URL, uuid, page, since))
                    .build();
            try (Response response = this.httpClient.newCall(request).execute()) {
                ResponseBody responseBody = response.body();
                if (responseBody == null)
                    throw new RuntimeException("Response body null for retrieveListensSincePage request");
                return this.modelBuilder.createListenPage(responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, this.executor);
    }
}
