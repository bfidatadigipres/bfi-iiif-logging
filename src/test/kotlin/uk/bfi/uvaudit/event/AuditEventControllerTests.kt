package uk.bfi.uvaudit.event

import org.flywaydb.core.internal.jdbc.JdbcTemplate
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.context.event.annotation.BeforeTestClass
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

@WebMvcTest(AuditEventController::class)
@AutoConfigureTestDatabase
internal class AuditEventControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var ds: DataSource

    @BeforeEach
    fun setup() {
        val template = NamedParameterJdbcTemplate(ds)
        template.update("""INSERT INTO user (sub) VALUES ("test@user.com") ON DUPLICATE KEY UPDATE id = id""", emptyMap<String, Any>())
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
                    mapOf("sub" to "test@user.com")
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
