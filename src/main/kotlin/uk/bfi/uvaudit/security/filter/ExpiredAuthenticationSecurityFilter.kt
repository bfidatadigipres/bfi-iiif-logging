package uk.bfi.uvaudit.security.filter

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import uk.bfi.uvaudit.security.AuditUser
import javax.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.getForObject


@Component
class ExpiredAuthenticationSecurityFilter(
    val clients: OAuth2AuthorizedClientService,
    @Value("#{environment.AUTH0_DOMAIN}") val domain: String
) : RequestMatcher {

    override fun matches(request: HttpServletRequest): Boolean {
        if (request.pathInfo == "/logout") {
            return true;
        }

        val authentication = SecurityContextHolder.getContext().authentication ?: return false

        val token = authentication as OAuth2AuthenticationToken
        val client = clients.loadAuthorizedClient<OAuth2AuthorizedClient>(
            token.authorizedClientRegistrationId,
            token.name
        )

        val template = RestTemplate()
        template.interceptors.add { req, body, ctx ->
            req.headers["Authorization"] = "Bearer ${client.accessToken.tokenValue}"
            ctx.execute(req, body)
        }

        val userInfo = template.getForEntity<Any>("https://${domain}/userinfo")

        // Log the user out if fetching userinfo returned an error
        return userInfo.statusCode.isError
    }
}
