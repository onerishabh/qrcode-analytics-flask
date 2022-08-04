from audioop import add
from datetime import datetime
from urllib import response
import uuid
import hashlib
import boto3

def generate_static_salt():
    print(str(uuid.uuid4())[0:16])

def get_static_salt():
    sm = boto3.resource("secretsmanager")

def add_dynamic_salt(salt: str, email: str):
    db = boto3.resource("dynamodb")
    table = db.Table("DynamicSaltTable")

    response = table.put_item(
        Item = {
            "email": email,
            "salt": salt,
            "addded_on": str(datetime.utcnow())+" UTC"
        }
    )

def get_dynamic_salt(email: str):
    db = boto3.client("dynamodb")
    response = db.get_item(
        TableName="DynamicSaltTable",
        Key={
            "email": {"S": email}
        }
    )
    if "Item" in response:
        return response["Item"]["salt"]["S"]
    raise Exception(f"{email} not present")

def encrypt_password(password: str, email: str):
    dynamic_salt = str(uuid.uuid4())[0:16]
    add_dynamic_salt(dynamic_salt, email)
    salted = f"{password}{dynamic_salt}"
    hashed_pw = hashlib.sha256(str.encode(salted)).hexdigest()

    return hashed_pw


def addUser(name: str, email: str, password: str):
    db = boto3.resource("dynamodb")
    table = db.Table("UserTable")

    response = table.put_item(
        Item = {
            "name" : name,
            "email": email,
            "password": encrypt_password(password, email),
            "registration_time" : str(datetime.utcnow())+" UTC"
        }
    )

def authUser(email: str, password: str):
    dynamic_salt = get_dynamic_salt(email)
    input_pw = password++dynamic_salt

    db = boto3.client("dynamodb")
    response = db.get_item(
        TableName="UserTable",
        Key={
            "email": {"S": email}
        }
    )
    encrypt_pwd = response["Item"]["password"]["S"]

    if(hashlib.sha256(str.encode(input_pw)).hexdigest() == encrypt_pwd):
        return True
    
    return False
