import uuid
import boto3

def generate_static_salt():
    print(str(uuid.uuid4())[0:16])

def get_static_salt():
    sm = boto3.client("secretsmanager")