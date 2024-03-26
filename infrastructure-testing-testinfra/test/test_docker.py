# snippet[test_caddy_runs_as_non_root_user]
def test_caddy_runs_as_non_root_user(host):
    caddy = host.process.get(comm="caddy")
    assert caddy.user == 'www'
# /snippet


def test_caddy_listening_on_8080(host):
    assert host.socket("tcp://:::8080").is_listening


def test_www_user(host):
    assert host.user("www").uid == 10000
    assert host.user("www").gid == 10000


def test_www_public_html(host):
    public_html = host.file("/www/public_html")
    assert public_html.is_directory
    assert public_html.user == "www"
    assert public_html.group == "www"
