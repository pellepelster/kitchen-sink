terraform {
  required_providers {
    hcloud = {
      source  = "hetznercloud/hcloud"
      version = "1.45.0"
    }

    template = {
      source = "hashicorp/template"
    }
  }

  required_version = ">= 0.13"
}

provider "hcloud" {
}
