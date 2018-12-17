//  Setup the core provider information.
provider "aws" {
  region = "${var.region}"
}

//  Create the HACEP cluster using our module.
module "hacep" {
  source          = "./modules"
  region          = "${var.region}"
  amisize         = "t2.large"     
  vpc_cidr        = "10.0.0.0/16"             
  subnetaz        = "${var.subnetaz}"
  subnet_cidr     = "10.0.1.0/24"     
  key_name        = "hacep"
  public_key_path = "${var.public_key_path}"
  cluster_name    = "hacep-cluster"
  cluster_id      = "hacep-cluster-${var.region}"
}

