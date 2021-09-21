package pink.zak.api.riptide.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@ConfigurationProperties("credentials")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private String username;
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
    }

    @Bean
    @Profile("dev")
    public UserDetailsService generateDevUserDetailsService() {
        return new InMemoryUserDetailsManager(User
                .withUsername("admin")
                .passwordEncoder(str -> PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(str))
                .password("admin")
                .roles("ADMIN")
                .build());
    }

    @Bean
    @Profile("prod")
    public UserDetailsService generateProdUserDetailsService() {
        return new InMemoryUserDetailsManager(User
                .withUsername(this.username)
                .passwordEncoder(str -> PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(str))
                .password(this.password)
                .roles("USER")
                .build());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
