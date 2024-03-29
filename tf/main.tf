terraform {
  required_version = ">= 0.14.0"
  required_providers {
    openstack = {
      source  = "terraform-provider-openstack/openstack"
      version = "~> 1.39.0"
    }
  }
}

provider "vault" {
}

data "vault_generic_secret" "openstack" {
  path = "secret/openstack"
}

provider "openstack" {
  auth_url = data.vault_generic_secret.openstack.data["url"]  
  tenant_id = data.vault_generic_secret.openstack.data["id"]
  tenant_name = data.vault_generic_secret.openstack.data["name"]
  user_domain_name = data.vault_generic_secret.openstack.data["domain"]
  user_name = data.vault_generic_secret.openstack.data["uname"]
  password = data.vault_generic_secret.openstack.data["password"]
  region = data.vault_generic_secret.openstack.data["region"]
}

resource "openstack_networking_secgroup_v2" "sg" {
  name        = "restaurant_finder_docker_sg"
}

resource "openstack_networking_secgroup_rule_v2" "sg_rule_ssh" {
  direction         = "ingress"
  ethertype         = "IPv4"
  protocol          = "tcp"
  port_range_min    = 22
  port_range_max    = 22
  remote_ip_prefix  = "0.0.0.0/0"
  security_group_id = openstack_networking_secgroup_v2.sg.id
}

resource "openstack_compute_instance_v2" "restaurant_finder_docker" {
  name        = "restaraunt_finder_docker"
  image_name  = var.image_name
  flavor_name = var.flavor_name
  key_pair = var.key_pair
  security_groups = [openstack_networking_secgroup_v2.sg.name]

  network {
    name = var.network_name
  }
}
