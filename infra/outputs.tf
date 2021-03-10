output "auth0_domain" {
  value = local.auth0_hostnames[terraform.workspace]
}

output "dummy_client" {
  value = <<EOF
Client Name: ${auth0_client.dummy_test.name}
Client ID: ${auth0_client.dummy_test.client_id}
Client Secret: ${auth0_client.dummy_test.client_secret}"
EOF
}

output "universal_viewer" {
  value = <<EOF
Client Name: ${auth0_client.universal_viewer.name}
Client ID: ${auth0_client.universal_viewer.client_id}
Client Secret: ${auth0_client.universal_viewer.client_secret}"
EOF
}
