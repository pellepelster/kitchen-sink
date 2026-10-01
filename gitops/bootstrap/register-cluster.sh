#!/usr/bin/env bash
# Registers a kind cluster as a deploy target in the Argo CD running on the "argocd" kind cluster.
# All kind clusters share the "kind" docker network, so Argo CD reaches the target via its
# internal control-plane address (https://<cluster>-control-plane:6443).
set -euo pipefail

CLUSTER="${1:-cluster-1}"
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
ARGOCD_KUBECONFIG="${ROOT}/.k8s/argocd-context.yaml"

KUBECONFIG_INTERNAL="$(mktemp)"
trap 'rm -f "$KUBECONFIG_INTERNAL"' EXIT
kind get kubeconfig --internal --name "$CLUSTER" > "$KUBECONFIG_INTERNAL"

get() { kubectl --kubeconfig "$KUBECONFIG_INTERNAL" config view --raw -o "jsonpath={$1}"; }

SERVER="$(get '.clusters[0].cluster.server')"
CA_DATA="$(get '.clusters[0].cluster.certificate-authority-data')"
CERT_DATA="$(get '.users[0].user.client-certificate-data')"
KEY_DATA="$(get '.users[0].user.client-key-data')"

kubectl --kubeconfig "$ARGOCD_KUBECONFIG" --context kind-argocd -n argocd apply -f - <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: cluster-${CLUSTER}
  namespace: argocd
  labels:
    argocd.argoproj.io/secret-type: cluster
type: Opaque
stringData:
  name: ${CLUSTER}
  server: ${SERVER}
  config: |
    {"tlsClientConfig":{"caData":"${CA_DATA}","certData":"${CERT_DATA}","keyData":"${KEY_DATA}"}}
EOF
