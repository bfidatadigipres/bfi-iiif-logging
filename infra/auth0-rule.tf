resource "auth0_rule" "enrich_userinfo" {
  name    = "Enrich-UserInfo"
  script  = file("${path.module}/scripts/enrich_userinfo.js")
  enabled = true
}
