set -o pipefail -o errexit -o nounset

export DEBIAN_FRONTEND=noninteractive
export APT_DEFAULT_OPTS="--assume-yes"

function system_update {
  apt-get update
  apt-get ${APT_DEFAULT_OPTS} dist-upgrade

  if [[ -f /var/run/reboot-required ]]; then
    shutdown -r now
  fi
}

# snippet[infrastructure-testing-testinfra_userdata-ssh]
function sshd_config {
cat <<-EOF
HostKey /etc/ssh/ssh_host_identity_key

LoginGraceTime 2m
PermitRootLogin yes

PasswordAuthentication no
PubkeyAuthentication yes
PermitEmptyPasswords no

ChallengeResponseAuthentication no
UsePAM yes
X11Forwarding no
PrintMotd no

AcceptEnv LANG LC_*
EOF
}

function sshd_setup() {
  local ssh_host_identity_key_base64="${1:-}"
  rm -rf /etc/ssh/ssh_host_*_key.pub

  touch /etc/ssh/ssh_host_identity_key
  chmod 600 /etc/ssh/ssh_host_identity_key
  echo "${ssh_host_identity_key_base64}" | base64 -d > /etc/ssh/ssh_host_identity_key

  sshd_config > /etc/ssh/sshd_config
  service ssh restart
}
# /snippet

function caddy_config {
cat <<-EOF
:80 {
	# Set this path to your site's directory.
	root * /www/public_html
	file_server
}
EOF
}

function index_html {
cat <<-EOF

<!doctype html>
<html lang=en>
<head>
<meta charset=utf-8>
<title>blah</title>
</head>
<body>
Hello world!
</body>
</html>
EOF
}

function caddy_setup() {
  apt-get ${APT_DEFAULT_OPTS} install caddy
  useradd  --create-home --gid caddy --home-dir /www www
  mkdir -p /www/public_html
  index_html > /www/public_html/index.html
  chown -R www:caddy /www/public_html

  caddy_config > /etc/caddy/Caddyfile
  systemctl restart caddy
}
