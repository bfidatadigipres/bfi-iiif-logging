# Dummy client
# For local development / testing of the login flows

resource "auth0_client" "dummy_test" {
  name                 = "Dummy Test Client${local.environment_qualifier}"
  app_type             = "regular_web"
  is_first_party       = true
  custom_login_page_on = true

  grant_types = [
    "authorization_code"
  ]

  callbacks = [
    "${local.auth0_base_uri}/login/callback",
    "https://oauthdebugger.com/debug",
    "http://localhost:8080/login/oauth2/code/auth0"
  ]

  jwt_configuration {
    alg = "RS256"
  }

  lifecycle {
    ignore_changes = [
      custom_login_page_preview,
      custom_login_page
    ]
  }
}

# Universal Viewer

resource "auth0_client" "universal_viewer" {
  name                 = "BFI National Archive Universal Viewer${local.environment_qualifier}"
  app_type             = "regular_web"
  logo_uri             = "https://${aws_s3_bucket.assets.bucket}.s3.${aws_s3_bucket.assets.region}.amazonaws.com/${aws_s3_bucket_object.assets_images_bfi-147x150-png.key}"
  is_first_party       = true
  custom_login_page_on = true

  grant_types = [
    "authorization_code"
  ]

  callbacks = [
    "${local.viewer_base_uri}/login/oauth2/code/auth0"
  ]

  allowed_logout_urls = [
    local.viewer_base_uri
  ]

  jwt_configuration {
    alg = "RS256"
  }

  lifecycle {
    ignore_changes = [
      custom_login_page_preview,
      custom_login_page
    ]
  }
}

# Media Player

resource "auth0_client" "media_player" {
  name                 = "BFI National Archive Media Player${local.environment_qualifier}"
  app_type             = "regular_web"
  logo_uri             = "https://${aws_s3_bucket.assets.bucket}.s3.${aws_s3_bucket.assets.region}.amazonaws.com/${aws_s3_bucket_object.assets_images_bfi-147x150-png.key}"
  is_first_party       = true
  custom_login_page_on = true

  grant_types = [
    "authorization_code"
  ]

  callbacks = [
    "${local.player_base_uri}/login/oauth2/code/auth0"
  ]

  allowed_logout_urls = [
    local.player_base_uri
  ]

  jwt_configuration {
    alg = "RS256"
  }

  lifecycle {
    ignore_changes = [
      custom_login_page_preview,
      custom_login_page
    ]
  }
}
