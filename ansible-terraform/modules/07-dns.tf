//  Notes: We could make the internal domain a variable, but not sure it is
//  really necessary.

//  Create the internal DNS.
resource "aws_route53_zone" "internal" {
  name = "hacep.local"
  comment = "HACEP Cluster Internal DNS"
  vpc_id = "${aws_vpc.hacep.id}"
  tags {
    Name    = "HACEP Internal DNS"
    Project = "hacep"
  }
}

//  Routes for 'master', 'node1' and 'node2'.
resource "aws_route53_record" "master-a-record" {
    zone_id = "${aws_route53_zone.internal.zone_id}"
    name = "master.hacep.local"
    type = "A"
    ttl  = 300
    records = [
        "${aws_instance.master.private_ip}"
    ]
}
resource "aws_route53_record" "node1-a-record" {
    zone_id = "${aws_route53_zone.internal.zone_id}"
    name = "node1.hacep.local"
    type = "A"
    ttl  = 300
    records = [
        "${aws_instance.node1.private_ip}"
    ]
}
resource "aws_route53_record" "node2-a-record" {
    zone_id = "${aws_route53_zone.internal.zone_id}"
    name = "node2.hacep.local"
    type = "A"
    ttl  = 300
    records = [
        "${aws_instance.node2.private_ip}"
    ]
}

resource "aws_route53_record" "node3-a-record" {
    zone_id = "${aws_route53_zone.internal.zone_id}"
    name = "node3.hacep.local"
    type = "A"
    ttl  = 300
    records = [
        "${aws_instance.node3.private_ip}"
    ]
}