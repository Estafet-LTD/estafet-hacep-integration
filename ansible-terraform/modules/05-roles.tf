//  Create a role which OpenShift instances will assume.
//  This role has a policy saying it can be assumed by ec2
//  instances.
resource "aws_iam_role" "hacep-instance-role" {
  name = "hacep-instance-role"

  assume_role_policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": "sts:AssumeRole",
            "Principal": {
                "Service": "ec2.amazonaws.com"
            },
            "Effect": "Allow",
            "Sid": ""
        }
    ]
}
EOF
}

//  This policy allows an instance to forward logs to CloudWatch, and
//  create the Log Stream or Log Group if it doesn't exist.
resource "aws_iam_policy" "hacep-policy-forward-logs" {
  name        = "hacep-instance-forward-logs"
  path        = "/"
  description = "Allows an instance to forward logs to CloudWatch"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "logs:DescribeLogStreams"
      ],
      "Resource": [
        "arn:aws:logs:*:*:*"
      ]
    }
  ]
}
EOF
}

//  Attach the policies to the roles.
resource "aws_iam_policy_attachment" "hacep-attachment-forward-logs" {
  name       = "hacep-attachment-forward-logs"
  roles      = ["${aws_iam_role.hacep-instance-role.name}"]
  policy_arn = "${aws_iam_policy.hacep-policy-forward-logs.arn}"
}

//  Create a instance profile for the role.
resource "aws_iam_instance_profile" "hacep-instance-profile" {
  name  = "hacep-instance-profile"
  role = "${aws_iam_role.hacep-instance-role.name}"
}

//  Create a instance profile for the bastion. All profiles need a role, so use
//  our simple openshift instance role.
resource "aws_iam_instance_profile" "bastion-instance-profile" {
  name  = "bastion-instance-profile"
  role = "${aws_iam_role.hacep-instance-role.name}"
}

//  Create a user and access key for openshift-only permissions
resource "aws_iam_user" "hacep-aws-user" {
  name = "hacep-aws-user"
  path = "/"
}

resource "aws_iam_user_policy" "hacep-aws-user" {
  name = "hacep-aws-user-policy"
  user = "${aws_iam_user.hacep-aws-user.name}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:DescribeVolume*",
        "ec2:CreateVolume",
        "ec2:CreateTags",
        "ec2:DescribeInstance*",
        "ec2:AttachVolume",
        "ec2:DetachVolume",
        "ec2:DeleteVolume",
        "ec2:DescribeSubnets",
        "ec2:CreateSecurityGroup",
        "ec2:DescribeSecurityGroups",
        "elasticloadbalancing:DescribeTags",
        "elasticloadbalancing:CreateLoadBalancerListeners",
        "ec2:DescribeRouteTables",
        "elasticloadbalancing:ConfigureHealthCheck",
        "ec2:AuthorizeSecurityGroupIngress",
        "elasticloadbalancing:DeleteLoadBalancerListeners",
        "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
        "elasticloadbalancing:DescribeLoadBalancers",
        "elasticloadbalancing:CreateLoadBalancer",
        "elasticloadbalancing:DeleteLoadBalancer",
        "elasticloadbalancing:ModifyLoadBalancerAttributes",
        "elasticloadbalancing:DescribeLoadBalancerAttributes"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_access_key" "hacep-aws-user" {
  user    = "${aws_iam_user.hacep-aws-user.name}"
}
