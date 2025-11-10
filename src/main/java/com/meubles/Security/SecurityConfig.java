package com.meubles.Security;

import com.meubles.Service.CustomUserDetailsService; // Assurez-vous que le chemin est correct
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Utilise votre configuration CORS (CorsConfig.java)
                .csrf(csrf -> csrf.disable())

                // --- RÈGLES CORRIGÉES ---
                .authorizeHttpRequests(auth -> auth
                        // Autoriser les routes publiques
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()

                        // Sécuriser /me (il faut un token)
                        .requestMatchers("/api/auth/me").authenticated()
                        // Sécurise /products

                        .requestMatchers("/api/products", "/api/products/**").permitAll()
                        // Sécurise les statics
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/api/products/*/buy").authenticated()

                        // Sécuriser tout le reste !!!
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )


                .authenticationProvider(authenticationProvider())

                // AJOUTE LE FILTRE "GARDIEN" QUI LIT LES TOKENS
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // (Nécessaire pour le login dans AuthService)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // (Fait le lien entre Spring, notre UserDetailsService et le PasswordEncoder)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Utilise CustomUserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}