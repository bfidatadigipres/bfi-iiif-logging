package uk.bfi.uvaudit.security.filter

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
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

    private val cache: LoadingCache<String, Boolean> = CacheBuilder
        .newBuilder()
        .expireAfterWrite(Duration.ofSeconds(cacheTtl))
        .weakKeys() // Keys can be compared using '=='
        .removalListener<String, Boolean> { logger.info("Access token [${it.key}] was evicted from cache: [${it.cause}]") }
        .build(CacheLoader.from { accessToken: String? -> loadCacheEntry(accessToken) })

    private fun loadCacheEntry(accessToken: String?): Boolean {
        return try {
            val restTemplate = RestTemplate()
            restTemplate.interceptors.add { req, body, ctx ->
                req.headers["Authorization"] = "Bearer $accessToken"
                ctx.execute(req, body)
            }

            val userInfoResponse = restTemplate.getForEntity<String>("https://${auth0Domain}/userinfo")
            logger.info("Cache put for access token [$accessToken]")
            userInfoResponse.statusCode != HttpStatus.OK
        } catch (e: Exception) {
            logger.error("An error occurred loading cache entry for access token [${accessToken}]", e)
            true;
        }
    }

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
        return cache.get(accessToken);
    }
}
