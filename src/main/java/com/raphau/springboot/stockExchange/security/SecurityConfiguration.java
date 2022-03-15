package com.raphau.springboot.stockExchange.security;


import com.raphau.springboot.stockExchange.security.jwt.AuthEntryPointJwt;
import com.raphau.springboot.stockExchange.security.jwt.AuthTokenFilter;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebSecurity
@EnableSwagger2
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    public AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Queue queueRequestBuyOffer() {
        return new Queue("buy-offer-request", false);
    }

    @Bean
    Queue queueRequestSellOffer() {
        return new Queue("sell-offer-request", false);
    }

    @Bean
    Queue queueRequestCompany() {
        return new Queue("company-request", false);
    }

    @Bean
    Queue queueTestDetails() {
        return new Queue("test-details-response", false);
    }

    @Bean
    Queue queueCpuData() {
        return new Queue("cpu-data-response", false);
    }

    @Bean
    Queue queueTimeData() {
        return new Queue("time-data-response", false);
    }

    @Bean
    Queue queueUserData() {
        return new Queue("user-data-request", false);
    }

    @Bean
    Queue queueStockData() {
        return new Queue("stock-data-request", false);
    }

    @Bean
    Queue queueUserDataResponse() {
        return new Queue("user-data-response", false);
    }

    @Bean
    Queue queueStockDataResponse() {
        return new Queue("stock-data-response", false);
    }

    @Bean
    Queue queueRegisterRequest() {
        return new Queue("register-request", false);
    }

    @Bean
    Queue queueRegisterResponse() {
        return new Queue("register-response", false);
    }

    @Bean
    Queue queueTradeRequest() {
        return new Queue("trade-request", false);
    }

    @Bean
    Queue queueTradeResponse() {
        return new Queue("trade-response", false);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory co) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(co);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/**",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
