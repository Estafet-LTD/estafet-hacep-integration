#!/usr/bin/env bash

# This script template is expected to be populated during the setup of a
# HACEP  node. It runs on host startup.

set -x

# Elevate priviledges, retaining the environment.
sudo -E su

# Install dev tools.
yum install -y "@Development Tools" python2-pip openssl-devel python-devel gcc libffi-devel

# Install ansible
pip install -I ansible==2.6.5

# Install Java 1.8
yum install -y java-1.8.0

# Remove Java 1.7
yum remove -y java-1.7.0-openjdk

# Allow the ec2-user to sudo without a tty, which is required when we run post
# install scripts on the server.
echo Defaults:ec2-user \!requiretty >> /etc/sudoers
