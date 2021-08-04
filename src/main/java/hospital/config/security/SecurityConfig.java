package hospital.config.security;

import hospital.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String NURSE = "NURSE";
    public static final String PATIENT = "PATIENT";

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers( "/", "/login", "/css/*").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority(ADMIN)
                .antMatchers("/doctor/**").hasAnyAuthority(DOCTOR)
                .antMatchers("/nurse/**").hasAnyAuthority(NURSE)
                .antMatchers("/patient/**").hasAnyAuthority(PATIENT)
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .successHandler((request, response, authentication) -> {
                    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                    if (roles.contains(ADMIN)) {
                        response.sendRedirect("/admin/patients");
                    }
                    if (roles.contains(DOCTOR)) {
                        response.sendRedirect("/doctor");
                    }
                    if (roles.contains(NURSE)) {
                        response.sendRedirect("/nurse" );
                    }
                    if (roles.contains(PATIENT)) {
                        response.sendRedirect("/patient");
                    }
                })
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(bcryptPasswordEncoder());
    }
}
