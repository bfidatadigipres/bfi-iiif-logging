# Locals

locals {
  # Common tags to be assigned to all resources
  common_tags = {
    "Project"     = var.tag_project
    "Environment" = terraform.workspace
    "ManagedBy"   = var.tag_managed_by
  }

  # Universal Viewer
  platform_hostnames = {
    dev  = "bfinationalarchiveviewer-dev.bfi.org.uk"
    prod = "bfinationalarchiveviewer.bfi.org.uk"
  }
  platform_base_uri = "https://${local.platform_hostnames[terraform.workspace]}"

  # Auth0
  auth0_hostnames = {
    dev  = "bfi-iiif-dev.eu.auth0.com",
    prod = "bfi-iiif.eu.auth0.com"
  }
  auth0_base_uri      = "https://${local.auth0_hostnames[terraform.workspace]}"
  auth0_email_address = "${var.auth0_email_from_user}@${var.auth0_email_from_domain}"
}

# Tags

variable "tag_project" {
  default = "Identity"
}

variable "tag_managed_by" {
  default = "Terraform"
}

# Auth0

variable "auth0_tenant_name" {
  default = "BFI IIIF"
}

variable "auth0_support_address" {
  default = "help@bfi.org.uk"
}

variable "auth0_support_url" {
  default = "https://www.bfi.org.uk/contact-us"
}

variable "auth0_universal_login_primary_colour" {
  default = "#000000"
}

variable "auth0_universal_login_background_colour" {
  default = "#FFFFFF"
}

variable "auth0_email_from_user" {
  default = "collectionssystems"
}

variable "auth0_email_from_domain" {
  default = "bfi.org.uk"
}

variable "auth0_email_smtp_host" {

}

variable "auth0_email_smtp_port" {
  default = 587
}

variable "auth0_email_smtp_username" {

}

variable "auth0_email_smtp_password" {

}

variable "auth0_password_policy" {
  default = "excellent" # none, low, fair, good, excellent
}

variable "auth0_password_min_length" {
  default = 12
}

variable "auth0_password_dictionary_enabled" {
  default = true
}

variable "auth0_password_dictionary" {
  default = [
    "bfi", "british", "film", "institute"
  ]
}

variable "auth0_no_personal_info_enabled" {
  default = true
}

variable "auth0_password_history_enabled" {
  default = true
}

variable "auth0_password_history_size" {
  default = 5
}
