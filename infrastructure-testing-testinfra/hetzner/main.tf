# snippet[infrastructure-testing-testinfra_ssh_client_identity]
resource "tls_private_key" "ssh_client_identity" {
  algorithm = "RSA"
}

resource "hcloud_ssh_key" "ssh_key" {
  name       = "infrastructure-testing-testinfra"
  public_key = tls_private_key.ssh_client_identity.public_key_openssh
}
# /snippet

# snippet[infrastructure-testing-testinfra_ssh_host_identity]
resource "tls_private_key" "ssh_host_identity" {
  algorithm = "RSA"
}
# /snippet

# snippet[infrastructure-testing-testinfra_server]
resource "hcloud_server" "server" {
  name        = "infrastructure-testing-testinfra"
  image       = "debian-12"
  server_type = "cx11"
  location    = var.location
  user_data   = data.template_file.user_data.rendered

  ssh_keys = [
    hcloud_ssh_key.ssh_key.id
  ]
}
# /snippet
