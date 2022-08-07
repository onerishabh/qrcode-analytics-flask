import boto3
import json

def get_cluster_name(cluster_arn: str):
    return cluster_arn.split("/")[1]

cf_client = boto3.client("cloudformation")
stackname = "QRCodeFlaskServerStack"

response = cf_client.describe_stacks(StackName=stackname)
outputs = response["Stacks"][0]["Outputs"]

for output in outputs:
    if output["OutputKey"] == "ClusterName":
        cluster = get_cluster_name(output["OutputValue"])

client = boto3.client('ecs')
response = client.list_tasks(cluster=cluster)

task_arn = response["taskArns"][0]

response = client.describe_tasks(cluster=cluster,tasks=[task_arn])

attachments = response["tasks"][0]["attachments"][0]["details"]

for attachment in attachments:
    if(attachment["name"]=="networkInterfaceId"):
        eni_id = attachment["value"]

ec2 = boto3.client("ec2")

response = ec2.describe_network_interfaces(
    NetworkInterfaceIds=[eni_id]
)


domain_name = response["NetworkInterfaces"][0]["Association"]["PublicIp"]
print(f"DOMAIN NAME : {domain_name}")

cf_client = boto3.client("cloudformation")
stackname = "InfrastructureStack"

response = cf_client.describe_stacks(StackName=stackname)
outputs = response["Stacks"][0]["Outputs"]

for output in outputs:
    if output["OutputKey"] == "QRCodeSMOP":
        secret_arn = output["OutputValue"]

client = boto3.client('secretsmanager')
ss = "{\"APP_DOMAIN\": \""+ domain_name + "\"}" 
response = client.put_secret_value(SecretId=secret_arn, SecretString=ss)
