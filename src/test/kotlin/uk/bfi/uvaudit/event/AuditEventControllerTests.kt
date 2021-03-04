package uk.bfi.uvaudit.event

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AuditEventControllerTests {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    @Throws(Exception::class)
    fun `user should be loaded from AuditUserService`() {
        // TODO: need fake credentials to simulate oauth2 logins
    }
}
