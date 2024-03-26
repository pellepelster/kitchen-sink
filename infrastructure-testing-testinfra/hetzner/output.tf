output "ipv4_address" {
  value = hcloud_server.server.ipv4_address
}

resource "local_file" "ssh_client_identity" {
  content         = tls_private_key.ssh_client_identity.private_key_openssh
  filename        = "${path.module}/ssh/ssh_client_identity"
  file_permission = "0600"
}

resource "local_file" "ssh_host_identity" {
  content         = tls_private_key.ssh_host_identity.private_key_openssh
  filename        = "${path.module}/ssh/host_client_identity"
  file_permission = "0600"
}

resource "local_file" "known_hosts" {
  content  = data.template_file.known_hosts.rendered
  filename = "${path.module}/ssh/known_hosts"
}


resource "local_file" "ssh_config" {
  content  = data.template_file.ssh_config.rendered
  filename = "${path.module}/ssh/ssh_config"
}
