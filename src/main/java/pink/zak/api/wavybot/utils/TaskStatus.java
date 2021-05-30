package pink.zak.api.wavybot.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskStatus<T> {
    private final AtomicInteger progress = new AtomicInteger();
    private CompletableFuture<T> future;
    private int requiredProgress;

    private Runnable requiredProgressTask;

    public CompletableFuture<T> getFuture() {
        return this.future;
    }

    public void setFuture(CompletableFuture<T> future) {
        this.future = future;
    }

    public AtomicInteger getProgress() {
        return this.progress;
    }

    public int getRequiredProgress() {
        return this.requiredProgress;
    }

    public void setRequiredProgress(int requiredProgress) {
        this.requiredProgress = requiredProgress;
        if (this.requiredProgressTask != null)
            this.requiredProgressTask.run();
    }

    public void setRequiredProgressTask(Runnable requiredProgressTask) {
        this.requiredProgressTask = requiredProgressTask;
    }
}
