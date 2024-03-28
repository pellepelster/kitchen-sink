import json
import time

import pytest
import requests


# snippet[infrastructure-testing-testinfra_wait-cloud-init]

def wait_until(predicate, timeout, period=2, *args, **kwargs):
    end = time.time() + timeout
    while time.time() < end:
        if predicate(*args, **kwargs):
            return True
        time.sleep(period)
    return False


def host_is_initialized(host):
    result_file = host.file("/run/cloud-init/result.json")
    return result_file.exists


@pytest.fixture(scope="module", autouse=True)
def wait_for_host_is_initialized(host):
    wait_until(lambda: host_is_initialized(host), 60)
    result = json.loads(host.file("/run/cloud-init/result.json").content)
    assert len(result['v1']['errors']) == 0, f"cloud init finished with errors '{result['v1']['errors']}'"


def test_hetzner_caddy_runs_as_non_root_user(host):
    caddy = host.process.get(comm="caddy")
    assert caddy.user == 'caddy'


# /snippet

def test_hetzner_caddy_reachable_from_outside(host):
    for address in host.interface("eth0", "inet").addresses:
        response = requests.get(f"http://{address}")
        assert response.status_code == 200, "unexpected status code: " + str(response.status_code)
