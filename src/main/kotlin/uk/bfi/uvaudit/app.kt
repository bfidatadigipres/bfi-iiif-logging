package uk.bfi.uvaudit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import uk.bfi.uvaudit.event.console.LoggingAuditEventWriter
import uk.bfi.uvaudit.security.AuditUserService


@SpringBootApplication
class ViewerAuditApplication : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var auditUserService: AuditUserService

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().oauth2Login().userInfoEndpoint {
                it.oidcUserService(auditUserService)
            }
            .and().csrf().disable()
    }

    @Bean
    fun auditUserService() = AuditUserService()

    // @TODO: Replace this with a MySQL backend.
    @Bean
    fun auditEventWriter() = LoggingAuditEventWriter()
}

fun main(args: Array<String>) {
    runApplication<ViewerAuditApplication>(*args)
}
