output "ipv4_address" {
  value = module.postgresql.ipv4_address
}

resource "local_file" "ssh_client_identity" {
  content         = tls_private_key.ssh_client_identity.private_key_openssh
  filename        = "${path.module}/ssh/ssh_client_identity"
  file_permission = "0600"
}

resource "local_file" "ssh_config" {
  content  = data.template_file.ssh_config.rendered
  filename = "${path.module}/ssh/ssh_config"
}
