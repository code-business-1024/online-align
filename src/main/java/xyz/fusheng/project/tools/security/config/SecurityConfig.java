package xyz.fusheng.project.tools.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import xyz.fusheng.project.tools.security.core.evaluator.CustomUserPermissionEvaluator;
import xyz.fusheng.project.tools.security.core.filter.JwtAuthenticationTokenFilter;
import xyz.fusheng.project.tools.security.core.handler.*;
import xyz.fusheng.project.tools.security.core.provider.CustomUserAuthenticationProvider;
import xyz.fusheng.project.tools.security.core.service.CustomUserDetailsService;

import javax.annotation.Resource;

/**
 * @FileName: SecurityConfig
 * @Author: code-fusheng
 * @Date: 2022/3/28 00:03
 * @Version: 1.0
 * @Description: Spring Security 核心配置文件
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义登录成功处理器
     */
    @Resource
    private CustomUserLoginSuccessHandler customUserLoginSuccessHandler;
    /**
     * 自定义登录失败处理器
     */
    @Resource
    private CustomUserLoginFailureHandler customUserLoginFailureHandler;
    /**
     * 自定义注销成功处理器
     */
    @Resource
    private CustomUserLogoutSuccessHandler customUserLogoutSuccessHandler;
    /**
     * 自定义暂无权限处理器
     */
    @Resource
    private CustomUserAuthAccessDeniedHandler customUserAuthAccessDeniedHandler;
    /**
     * 自定义未登录的处理器
     */
    @Resource
    private CustomUserAuthenticationEntryPointHandler customUserAuthenticationEntryPointHandler;
    /**
     * 自定义登录逻辑验证器
     */
    @Resource
    private CustomUserAuthenticationProvider customUserAuthenticationProvider;

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    /**
     * 配置地址栏不能识别 // 的情况  good nice 666
     *
     * @return
     */
    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    /**
     * 加密方式
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入自定义PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomUserPermissionEvaluator());
        return handler;
    }

    /**
     * 配置登录验证逻辑
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        //这里可启用我们自己的登陆验证逻辑
        auth.authenticationProvider(customUserAuthenticationProvider);
    }

    /**
     * 配置security的控制逻辑
     * @Author Sans
     * @CreateTime 2019/10/1 16:56
     * @Param  http 请求
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // 不进行权限验证的请求或资源(从配置文件中读取)
                // .antMatchers(JwtConfig.antMatchers.split(",")).permitAll()
                .antMatchers("/code/sms", "/**/login", "/github/login", "/login/github", "/authentication/mobile", "/push/websocket",
                        "/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/doc.html",
                        "/webjars/**", "/user/register", "/druid/login.html", "/druid/**",
                        "/category/getList", "/article/getByPage", "/article/read/**", "article/getLastAndNextArticleVo/**", "/comment/getByPage").permitAll()
                // 全开放
                .anyRequest().permitAll()
                // 其他的需要登陆后才能访问
                // .anyRequest().authenticated()
                .and()
                // 配置 X-Frame-Options 问题
                .headers().frameOptions().disable()
                .and()
                // 配置未登录自定义处理类
                .httpBasic().authenticationEntryPoint(customUserAuthenticationEntryPointHandler)
                .and()
                // 配置登录地址
                .formLogin().loginPage("/login")
                // 配置登录成功自定义处理类
                .successHandler(customUserLoginSuccessHandler)
                // 配置登录失败自定义处理类
                .failureHandler(customUserLoginFailureHandler)
                .and()
                // 配置登出地址
                .logout()
                .logoutUrl("/logout")
                // 配置用户登出自定义处理类
                .logoutSuccessHandler(customUserLogoutSuccessHandler)
                .and()
                // 配置没有权限自定义处理类
                .exceptionHandling().accessDeniedHandler(customUserAuthAccessDeniedHandler)
                .and()
                // 开启跨域
                .cors()
                .and()
                // 取消跨站请求伪造防护
                .csrf().disable();


        // 基于Token不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT过滤器
        http.addFilter(new JwtAuthenticationTokenFilter(authenticationManager()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**,/static/**", "/templates/**", "/img/**");
    }

}
