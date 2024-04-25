data "template_file" "ssh_config" {
  template = file("${path.module}/templates/ssh_config.template")
  vars     = {
    postgresql_ip_address = module.postgresql.ipv4_address
    pgbench_ip_address    = hcloud_server.pgbench.ipv4_address
    client_identity_file  = abspath("${path.module}/${local_file.ssh_client_identity.filename}")
  }
}

