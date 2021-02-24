# Dummy client
# For local development / testing of the login flows

resource "auth0_client" "dummy_test" {
  name                 = "Dummy Test Client (${terraform.workspace})"
  app_type             = "regular_web"
  is_first_party       = true
  custom_login_page_on = true

  grant_types = [
    "authorization_code"
  ]

  callbacks = [
    "${local.auth0_base_uri}/login/callback"
  ]
}

## Platform

resource "auth0_client" "account_management_system" {
  name                 = "Account Management System (${terraform.workspace})"
  app_type             = "regular_web"
  is_first_party       = true
  custom_login_page_on = true

  grant_types = [
    "authorization_code"
  ]

  callbacks = [
    "${local.platform_base_uri}/callback"
  ]

  allowed_logout_urls = [
    local.platform_base_uri
  ]

  lifecycle {
    ignore_changes = [
      custom_login_page_preview,
      custom_login_page
    ]
  }
}
