terraform {
  required_version = "= 0.14.7" # Pin to a specific version to avoid accidental upgrading of the statefile

  backend "s3" {
    bucket = "bfi-iiif-logging-remote-state"
    key    = "terraform.tfstate"
    region = "eu-west-2"
  }
}
