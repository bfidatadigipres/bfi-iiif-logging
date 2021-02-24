terraform {
  required_providers {
    auth0 = {
      source  = "alexkappa/auth0"
      version = "~> 0.19.0" # Any non-beta version >= 0.16.0 and <0.17.0, i.e. 0.16.X
    }
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.29.0" # Any non-beta version >= 3.29.0 and <3.30.0, i.e. 3.29.X
    }
  }
}
