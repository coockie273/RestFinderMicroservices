output "servers" {
  value = {
    docker_server = openstack_compute_instance_v2.restaurant_finder_docker.access_ip_v4
  }
}
