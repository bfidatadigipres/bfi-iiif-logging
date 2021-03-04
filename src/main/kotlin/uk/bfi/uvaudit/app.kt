package uk.bfi.uvaudit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
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
            .and().oauth2Login().userInfoEndpoint {
                it.oidcUserService(auditUserService)
            }
            .and().csrf().disable()
    }

    @Bean
    fun auditUserService(ds: DataSource) = AuditUserService(ds)

    @Bean
    fun auditEventWriter(ds: DataSource) = JdbcAuditEventWriter(ds)
}

fun main(args: Array<String>) {
    runApplication<ViewerAuditApplication>(*args)
}
