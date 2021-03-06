//  Output some useful variables for quick SSH access etc.

// master node
output "master-public_ip" {
  value = "${aws_eip.master_eip.public_ip}"
}
output "master-private_dns" {
  value = "${aws_instance.master.private_dns}"
}
output "master-private_ip" {
  value = "${aws_instance.master.private_ip}"
}

// node 1
output "node1-public_ip" {
  value = "${aws_eip.node1_eip.public_ip}"
}
output "node1-private_dns" {
  value = "${aws_instance.node1.private_dns}"
}
output "node1-private_ip" {
  value = "${aws_instance.node1.private_ip}"
}

// node 2
output "node2-public_ip" {
  value = "${aws_eip.node2_eip.public_ip}"
}
output "node2-private_dns" {
  value = "${aws_instance.node2.private_dns}"
}
output "node2-private_ip" {
  value = "${aws_instance.node2.private_ip}"
}

// node 3
output "node3-public_ip" {
  value = "${aws_eip.node3_eip.public_ip}"
}
output "node3-private_dns" {
  value = "${aws_instance.node3.private_dns}"
}
output "node3-private_ip" {
  value = "${aws_instance.node3.private_ip}"
}
