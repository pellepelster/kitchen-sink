data "template_file" "user_data" {
  template = file("${path.module}/templates/user_data.sh.template")

  vars = {
    ssh_host_identity_key_base64 = base64encode(tls_private_key.ssh_host_identity.private_key_openssh)
    user_data                    = file("${path.module}/templates/user_data.sh")
  }
}

# snippet[infrastructure-testing-testinfra_template_file_known_hosts]
data "template_file" "known_hosts" {
  template = file("${path.module}/templates/known_hosts.template")

  vars = {
    ip_address            = hcloud_server.server.ipv4_address
    ssh_host_identity_pub = tls_private_key.ssh_host_identity.public_key_openssh
  }
}
# /snippet

data "template_file" "ssh_config" {
  template = file("${path.module}/templates/ssh_config.template")
  vars = {
    ip_address           = hcloud_server.server.ipv4_address
    known_hosts_file     = abspath("${path.module}/${local_file.known_hosts.filename}")
    client_identity_file = abspath("${path.module}/${local_file.ssh_client_identity.filename}")
  }
}

