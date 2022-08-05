# syntax=docker/dockerfile:1

FROM python:3.8-slim-buster

ARG region
ARG access_key
ARG secret_key

ENV AWS_DEFAULT_REGION=$region
ENV AWS_ACCESS_KEY_ID=$access_key
ENV AWS_SECRET_ACCESS_KEY=$secret_key

RUN export SECRETS_MANAGER=$(aws cloudformation describe-stacks --stack-name InfrastructureStack --query "Stacks[?StackName=='InfrastructureStack'][].Outputs[?OutputKey=='QRCodeSMOP'].OutputValue" --no-paginate --output text)

WORKDIR /python-docker

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt

COPY . .

CMD [ "python3", "-m", "flask", "run", "--host=0.0.0.0" ]