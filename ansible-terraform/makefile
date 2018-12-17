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
	ssh-keyscan -t rsa -H $$(terraform output bastion-public_dns) >> ~/.ssh/known_hosts
	ssh -A ec2-user@$$(terraform output bastion-public_dns) "ssh-keyscan -t rsa -H master.hacep.local >> ~/.ssh/known_hosts"
	ssh -A ec2-user@$$(terraform output bastion-public_dns) "ssh-keyscan -t rsa -H node1.hacep.local >> ~/.ssh/known_hosts"
	ssh -A ec2-user@$$(terraform output bastion-public_dns) "ssh-keyscan -t rsa -H node2.hacep.local >> ~/.ssh/known_hosts"
	ssh -A ec2-user@$$(terraform output bastion-public_dns) "ssh-keyscan -t rsa -H node3.hacep.local >> ~/.ssh/known_hosts"

	# Copy our inventory to the bastion and run the install script.
	scp ./inventory.cfg ec2-user@$$(terraform output bastion-public_dns):~
	cat install-from-bastion.sh | ssh -tt -o StrictHostKeyChecking=no -A ec2-user@$$(terraform output bastion-public_dns)

# SSH onto the bastion
ssh-bastion:
	ssh -t -A ec2-user@$$(terraform output bastion-public_dns)

# SSH onto the master
ssh-master:
	ssh -t -A ec2-user@$$(terraform output bastion-public_dns) ssh master.hacep.local

# SSH onto the node 1
ssh-node1:
	ssh -t -A ec2-user@$$(terraform output bastion-public_dns) ssh node1.hacep.local

# SSH onto the node 2	
ssh-node2:
	ssh -t -A ec2-user@$$(terraform output bastion-public_dns) ssh node2.hacep.local

# SSH onto the node 3	
ssh-node3:
	ssh -t -A ec2-user@$$(terraform output bastion-public_dns) ssh node3.hacep.local


