package uk.bfi.uvaudit.event

import org.springframework.security.core.AuthenticationException


/**
 * Thrown if user authenticated but email address has not been verified
 */
class EmailNotVerifiedException : AuthenticationException("Email address not validated")