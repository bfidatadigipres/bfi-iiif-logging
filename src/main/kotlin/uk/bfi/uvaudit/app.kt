package uk.bfi.uvaudit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class ViewerAuditSecurity : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().oauth2Login()
            .and().csrf().disable()
    }
}

@SpringBootApplication
class ViewerAuditApplication

fun main(args: Array<String>) {
    runApplication<ViewerAuditApplication>(*args)
}
