resource "auth0_tenant" "tenant" {
  friendly_name = var.auth0_tenant_name
  picture_url   = "https://${aws_s3_bucket.assets.bucket_regional_domain_name}/${aws_s3_bucket_object.assets_images_bfi-147x150-png.key}"
  support_email = var.auth0_support_address
  support_url   = var.auth0_support_url

  universal_login {
    colors {
      primary         = var.auth0_universal_login_primary_colour
      page_background = var.auth0_universal_login_background_colour
    }
  }
}
