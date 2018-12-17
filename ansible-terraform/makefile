infrastructure:
	# Get the modules, create the infrastructure.
	terraform init && terraform get && terraform apply

# Destroy the infrastructure.
destroy:
	terraform init && terraform destroy -auto-approve

# Installs hacep on the cluster.
hacep:
	# Add our identity for ssh, add the host key to avoid having to accept the
	# the host key manually. Also add the identity of each node to the bastion.
	ssh-add ~/.ssh/id_rsa

# SSH onto the bastion
ssh-bastion:
	ssh -t -A ec2-user@$$(terraform output bastion-public_ip)

# SSH onto the master
ssh-master:
	ssh -t -A ec2-user@$$(terraform output master-public_ip)

# SSH onto the node 1
ssh-node1:
	ssh -t -A ec2-user@$$(terraform output node1-public_ip)

# SSH onto the node 2	
ssh-node2:
	ssh -t -A ec2-user@$$(terraform output node2-public_ip)

# SSH onto the node 3	
ssh-node3:
	ssh -t -A ec2-user@$$(terraform output node3-public_ip)


