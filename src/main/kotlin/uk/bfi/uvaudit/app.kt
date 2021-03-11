package uk.bfi.uvaudit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import uk.bfi.uvaudit.event.jdbc.JdbcAuditEventWriter
import uk.bfi.uvaudit.security.AuditUserService
import javax.sql.DataSource

@SpringBootApplication
class ViewerAuditApplication : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var auditUserService: AuditUserService

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().exceptionHandling {
                it.defaultAuthenticationEntryPointFor(
                    HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    AntPathRequestMatcher("api/**")
                )
            }
            .oauth2Login().userInfoEndpoint {
                it.oidcUserService(auditUserService)
            }
            .and().logout().logoutSuccessUrl("/") // This will effectively trigger the auth flow again
            .and().csrf().disable()
    }

    @Bean
    fun auditUserService(dataSource: DataSource) = AuditUserService(dataSource)

    @Bean
    fun auditEventWriter(dataSource: DataSource) = JdbcAuditEventWriter(dataSource)
}

fun main(args: Array<String>) {
    runApplication<ViewerAuditApplication>(*args)
}
