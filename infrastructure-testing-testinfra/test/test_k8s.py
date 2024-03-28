from kubernetes import client, config


# snippet[infrastructure-testing-testinfra_test_k8s_environment_variables]
def test_k8s_environment_variables(host):
    environment = host.environment()
    assert environment['DEBUG'] == "false"
# /snippet


def test_k8s_labels(host):

    # snippet[infrastructure-testing-testinfra_test_k8s_environment_variables_hostname_namespace]
    hostname = host.environment()['HOSTNAME']
    namespace = host.run('cat /run/secrets/kubernetes.io/serviceaccount/namespace').stdout
    # /snippet

    # snippet[infrastructure-testing-testinfra_test_k8s_environment_variables_assert_labels]
    config.load_kube_config()
    v1 = client.CoreV1Api()
    pod = v1.read_namespaced_pod(name=hostname, namespace=namespace)
    assert pod.metadata.labels['log-service-name'] == 'infrastructure-testing-testinfra'
    # /snippet
