resource "tls_private_key" "ssh_client_identity" {
  algorithm = "RSA"
}

resource "hcloud_ssh_key" "ssh_client_identity" {
  name       = "cloud-provider-pgbench"
  public_key = tls_private_key.ssh_client_identity.public_key_openssh
}

resource "tls_private_key" "ssh_host_identity" {
  algorithm = "RSA"
}

resource "hcloud_volume" "data" {
  name     = "rds-postgresql-data"
  size     = 128
  format   = "ext4"
  location = var.location
}

resource "hcloud_volume" "backup" {
  name     = "rds-postgresql-backup"
  format   = "ext4"
  size     = 256
  location = var.location
}

# snippet[postgres-performance-debugging-postgresql]
module "postgresql" {
  source  = "pellepelster/solidblocks-rds-postgresql/hcloud"
  version = "0.2.5"

  name     = "postgresql"
  location = var.location
  ssh_keys = [hcloud_ssh_key.ssh_client_identity.id]

  server_type = var.instance_type

  backup_volume = hcloud_volume.backup.id
  data_volume   = hcloud_volume.data.id

  public_net_ipv4_enabled = true

  databases = [
    { id : "pgbench", user : "pgbench", password : "pgbench" }
  ]
}
# /snippet

resource "hcloud_server" "pgbench" {
  name        = "pgbench"
  server_type = "cx11"
  image       = "debian-11"
  location    = var.location
  user_data = <<EOT
#!/usr/bin/env bash
apt-get update
apt-get install -y postgresql-contrib
EOT
  ssh_keys    = [hcloud_ssh_key.ssh_client_identity.id]
}