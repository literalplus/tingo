package li.l1t.tingo.config;

import li.l1t.tingo.security.auth.JwtAuthenticationFilter;
import li.l1t.tingo.security.auth.TokenHandler;
import li.l1t.tingo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures Spring Security.
 *
 * @since 2016-02-09
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final TokenHandler tokenHandler;
    private final UserService userService;

    public WebSecurityConfiguration(TokenHandler tokenHandler, UserService userService) {
        this.tokenHandler = tokenHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/api/**", "/auth/status").authenticated() -> disabled for guest users
                .anyRequest().permitAll()
                .and().addFilterBefore(new JwtAuthenticationFilter(tokenHandler), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable(); // We use Bearer auth, not forms or anything
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
