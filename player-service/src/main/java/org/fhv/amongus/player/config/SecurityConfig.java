package org.fhv.amongus.player.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.JwtTokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.
                                frameOptions(Customizer.withDefaults()).
                                disable()
                )
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/**","/ws/**", "/h2-console/**")
                                .permitAll()
                                .requestMatchers(new MovementServiceRequestMatcher())
                                .permitAll()
                                .requestMatchers(new GameRoomServiceRequestMatcher())
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    public static class MovementServiceRequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String remoteHost = request.getRemoteHost();

            // Log the remote host for debugging purposes
            System.out.println("Remote Host: " + remoteHost);

            // Assuming Movement Service is running on localhost (127.0.0.1)
            return "127.0.0.1".equals(remoteHost) || "localhost".equals(remoteHost);
        }
    }

    public static class GameRoomServiceRequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String remoteHost = request.getRemoteHost();

            // Log the remote host for debugging purposes
            System.out.println("Remote Host: " + remoteHost);

            // Assuming Movement Service is running on localhost (127.0.0.1)
            return "127.0.0.1".equals(remoteHost) || "localhost".equals(remoteHost);
        }
    }
}

