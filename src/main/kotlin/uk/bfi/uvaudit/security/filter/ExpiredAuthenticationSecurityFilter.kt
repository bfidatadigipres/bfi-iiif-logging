package uk.bfi.uvaudit.security.filter

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.nimbusds.openid.connect.sdk.claims.UserInfo
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
    @Value("#{environment.AUTH0_DOMAIN}") val domain: String,
    @Value("#{environment.AUTH0_USERINFO_TTL ?: 20}") val tokenTtl: Long
) : RequestMatcher {

    private val cache: Cache<String, UserInfo> = CacheBuilder
        .newBuilder()
        .expireAfterWrite(Duration.ofSeconds(tokenTtl))
        .build()

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
            val userInfoResponse = template.getForEntity<String>("https://${domain}/userinfo")
            if (userInfoResponse.statusCode != HttpStatus.OK) {
                cache.put(accessToken, UserInfo.parse(userInfoResponse.body))
                return true
            }

            return false
        } catch (ex: HttpClientErrorException) {
            return false
        }
    }
}
