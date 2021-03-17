package uk.bfi.uvaudit.security.filter

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.nimbusds.openid.connect.sdk.claims.UserInfo
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.time.Duration
import javax.servlet.http.HttpServletRequest

@Component
class ExpiredAuthenticationSecurityFilter(
    val clients: OAuth2AuthorizedClientService,
    @Value("#{environment.AUTH0_DOMAIN}") val auth0Domain: String,
    @Value("#{environment.AUTH0_USERINFO_TTL ?: 20}") val cacheTtl: Long
) : RequestMatcher {

    private val logger = KotlinLogging.logger {}

    private val cache: Cache<String, UserInfo> = CacheBuilder
        .newBuilder()
        .expireAfterWrite(Duration.ofSeconds(cacheTtl))
        .build()

    override fun matches(request: HttpServletRequest): Boolean {
        if (request.servletPath == "/logout") {
            return true;
        }

        val authentication = SecurityContextHolder.getContext().authentication ?: return false
        val token = authentication as OAuth2AuthenticationToken
        val client = clients.loadAuthorizedClient<OAuth2AuthorizedClient>(
            token.authorizedClientRegistrationId,
            token.name
        )

        val accessToken = client.accessToken.tokenValue
        if (cache.getIfPresent(accessToken) != null) {
            // Token is still valid and in the cache, don't log the user out
            return false
        }

        val template = RestTemplate()
        template.interceptors.add { req, body, ctx ->
            req.headers["Authorization"] = "Bearer ${client.accessToken.tokenValue}"
            ctx.execute(req, body)
        }

        try {
            val userInfoResponse = template.getForEntity<String>("https://${auth0Domain}/userinfo")
            if (userInfoResponse.statusCode != HttpStatus.OK) {
                return true
            }

            cache.put(accessToken, UserInfo.parse(userInfoResponse.body))
            return false
        } catch (ex: HttpClientErrorException) {
            logger.warn("Unable to fetch UserInfo for access token [${client.accessToken.tokenValue}]", ex.message)
            return true
        }
    }
}
