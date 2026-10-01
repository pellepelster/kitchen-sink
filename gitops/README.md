# gitops

Minimal Argo CD setup: Argo CD runs on the `argocd` kind cluster and deploys
`ghcr.io/pellepelster/java-spring-wuphf` to `cluster-1` and `cluster-2`.

```
gitops/
├── bootstrap/                   # applied by hand, once
│   ├── register-cluster.sh      # registers a kind cluster as Argo CD destination
│   └── wuphf-appset.yaml        # ApplicationSet: one Application per registered cluster
└── apps/wuphf/
    ├── base/                    # Deployment + Service shared by all clusters
    └── overlays/
        ├── cluster-1/           # CLUSTER_NAME=cluster-1, YOLO=2000
        └── cluster-2/           # CLUSTER_NAME=cluster-2, FOO=BAR
```

The ApplicationSet generates `wuphf-<cluster>` for every registered cluster and syncs
`apps/wuphf/overlays/<cluster>`, so every registered cluster needs a matching overlay.

## Bootstrap

Prerequisites: kind clusters created as in the top-level `README.md`, and this repo pushed
to the `repoURL` in `bootstrap/wuphf-appset.yaml`. Argo CD only syncs from git, so local
changes have no effect until they are pushed.

```shell
mise run argocd:bootstrap            # installs Argo CD, registers cluster-1 and cluster-2
mise kubectl argocd -- apply -n argocd -f gitops/bootstrap/wuphf-appset.yaml
```

## Check

```shell
mise kubectl argocd -- -n argocd get applications
mise kubectl cluster-1 -- -n wuphf port-forward svc/wuphf 8080:80
curl localhost:8080
```

## Changes

- image version: bump the tag in `apps/wuphf/base/deployment.yaml`
- per-cluster env vars: edit `apps/wuphf/overlays/<cluster>/kustomization.yaml`
- new cluster: add an overlay, then `mise run argocd:bootstrap <cluster>`
