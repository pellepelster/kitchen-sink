output "ipv4_address" {
  value = hcloud_server.server.ipv4_address
}

# snippet[infrastructure-testing-testinfra_local_file_ssh_client_identity]
resource "local_file" "ssh_client_identity" {
  content         = tls_private_key.ssh_client_identity.private_key_openssh
  filename        = "${path.module}/ssh/ssh_client_identity"
  file_permission = "0600"
}
# /snippet

resource "local_file" "known_hosts" {
  content  = data.template_file.known_hosts.rendered
  filename = "${path.module}/ssh/known_hosts"
}

resource "local_file" "ssh_config" {
  content  = data.template_file.ssh_config.rendered
  filename = "${path.module}/ssh/ssh_config"
}
