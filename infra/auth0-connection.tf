resource "auth0_connection" "username_password" {
  name     = "Username-Password-Connection"
  strategy = "auth0"

  enabled_clients = [
    auth0_client.dummy_test.id,
    auth0_client.account_management_system.id
  ]

  options {
    brute_force_protection = true
    disable_signup         = true
    requires_username      = false

    password_policy = var.auth0_password_policy

    password_complexity_options {
      min_length = var.auth0_password_min_length
    }

    password_dictionary {
      enable     = var.auth0_password_dictionary_enabled
      dictionary = []
    }

    password_no_personal_info {
      enable = var.auth0_no_personal_info_enabled
    }

    password_history {
      enable = var.auth0_password_history_enabled
      size   = var.auth0_password_history_size
    }
  }


}