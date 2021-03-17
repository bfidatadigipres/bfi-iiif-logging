package uk.bfi.uvaudit.event

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import uk.bfi.uvaudit.security.AuditUser
import java.time.Duration
import java.time.Instant
import javax.sql.DataSource

val auditUser = AuditUser(
    1, DefaultOidcUser(
        emptyList(),
        OidcIdToken(
            "test",
            Instant.now(),
            Instant.now().plus(Duration.ofDays(1)),
            mapOf("test_claim" to "test", "sub" to "test@user.com")
        )
    )
)

@Configuration
internal class AuditEventControllerTestsConfig {
    @Bean
    @Primary
    fun expiredAuthenticationSecurityFilter(): RequestMatcher {
        return RequestMatcher { false }
    }

}

@SpringBootTest(
    properties = [
        "spring.flyway.enabled=true"
    ]
)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
internal class AuditEventControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var ds: DataSource

    @BeforeEach
    fun setup() {
        val template = NamedParameterJdbcTemplate(ds)
        template.update(
            "INSERT INTO user (sub, email, department) VALUES ('auth0|abc123', 'test@example.com', 'IT') ON DUPLICATE KEY UPDATE id = id",
            emptyMap<String, Any>()
        )
    }

    @Test
    fun `should return bad request on invalid audit event`() {
        mockMvc
            .post("/api/event") {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                        "type": "invalid_event_type"
                    }
                """

                with(oauth2Login().attributes {
                    mapOf("sub" to "auth0|abc123")
                })
            }
            .andExpect { status { isBadRequest } }
    }

    @Test
    fun `should store valid audit event`() {
        mockMvc
            .post("/api/event") {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                        "type": "resource_loaded",
                        "uri": "https://127.0.0.1/test-resource"
                    }
                """

                with(
                    oauth2Login().oauth2User(
                        auditUser
                    )
                )
            }
            .andExpect { status { `is`(204) } }
    }
}
