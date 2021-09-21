package pink.zak.api.riptide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Primary
    @Bean("executor")
    public ThreadPoolTaskExecutor generateExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(100);
        executor.setKeepAliveSeconds(120);
        executor.setThreadNamePrefix("wb-executor-");
        return executor;
    }

    @Bean("scheduler")
    public ThreadPoolTaskScheduler generateScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setThreadNamePrefix("wb-scheduled-");
        executor.initialize();
        return executor;
    }
}
