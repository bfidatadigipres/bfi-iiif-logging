resource "auth0_action" "set_user_organisations" {
  name   = "Set User Organisations"
  code   = file("${path.module}/scripts/extract_user_orgs.js")
  deploy = true

  supported_triggers {
    id      = "pre-user-registration"
    version = "v2"
  }
}
