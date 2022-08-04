import uuid
import csv
import boto3
from datetime import datetime


def store_id(id: str, target_url: str, email: str):
    db = boto3.resource("dynamodb")
    table = db.Table("QRCodeTable")

    response = table.put_item(
        Item = {
            "qrcode_id" : id,
            "email": email,
            "target_url" : target_url,
            "created_time" : str(datetime.utcnow())+" UTC"
        }
    )

def create_short_url(target_url: str, email: str):
    id = str(uuid.uuid4())[0:8]
    store_id(id, target_url, email)

    url = f"""http://3.89.207.185:5000/short/{id}"""
    return url

def get_target_url(id: str):
    db = boto3.client("dynamodb")
    response = db.get_item(
        TableName="QRCodeTable",
        Key={
            "qrcode_id": {"S": id}
        }
    )
    return response["Item"]["target_url"]["S"]

def add_new_invocation(id: str, agent: str):
    db = boto3.resource("dynamodb")
    table = db.Table("InvocationTable")

    response = table.put_item(
        Item = {
            "invocation_id" : str(uuid.uuid4())[0:12],
            "qrcode_id" : id,
            "agent": agent,
            "invocation_time" : str(datetime.utcnow())+" UTC"
        }
    )