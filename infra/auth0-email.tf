
resource "auth0_email" "email" {
  name                 = "smtp"
  enabled              = true
  default_from_address = local.auth0_email_address

  credentials {
    smtp_host = var.auth0_email_smtp_host
    smtp_port = var.auth0_email_smtp_port
    smtp_user = var.auth0_email_smtp_username
    smtp_pass = var.auth0_email_smtp_password
  }
}
