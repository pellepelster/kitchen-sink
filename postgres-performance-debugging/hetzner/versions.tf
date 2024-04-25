terraform {
  required_providers {
    hcloud = {
      source  = "hetznercloud/hcloud"
      version = "1.46.1"
    }

    template = {
      source = "hashicorp/template"
    }
  }

  required_version = ">= 0.13"
}

provider "hcloud" {
}
