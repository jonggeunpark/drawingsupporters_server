package com.drawing.drawing.config;

import com.drawing.drawing.jwt.JwtAccessDeniedHandler;
import com.drawing.drawing.jwt.JwtAuthenticationEntryPoint;
import com.drawing.drawing.jwt.JwtSecurityConfig;
import com.drawing.drawing.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/error"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //.and()
                //.headers()
                //.frameOptions()
                //.sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()
                .antMatchers("/api/mentee/signup").permitAll() // ?????? ????????????
                .antMatchers("/api/mentor/signup").permitAll() // ?????? ????????????
                .antMatchers("/api/user/login").permitAll() // ?????????
                .antMatchers("/api/user/check-email").permitAll() // ????????? ?????? ??????
                .antMatchers("/api/user/check-nickname").permitAll() // ????????? ?????? ??????
                .antMatchers("/api/feedback").permitAll() // ????????? ?????? ??????
                .antMatchers("/api/feedback/{feedback_id}").permitAll() // ????????? ?????? ??????
                .antMatchers("/api/drawing").permitAll() // ????????? ?????? ?????? ??????
                .antMatchers("/api/drawing/{drawing_id}").permitAll() // ????????? ?????? ?????? ??????
                .anyRequest().authenticated()


                .and()
                .cors()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}