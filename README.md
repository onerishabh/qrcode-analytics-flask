# QR Code Analytics Flask Application

This is a flask app that lets user download QR Code for a given `targetURL`. This application helps user get analytics on when their QR Codes were scanned, device from which QR Code was scanned, location of scanning. Users can maintain as many QR Codes in the application as they want.

# API Documentation

## Add User
https://{public-ip-address}:5000/registerUser?name=<name-of-user>&email=<email-of-user>password=<password-of-user>

## Authenticate User
https://{public-ip-address}:5000/authUser?email=<email-of-user>&password=<password-of-user>

## Create QR Code
https://{public-ip-address}:5000/qrcode/<url-to-be-qrcodeified>

Application generated QR Code points to https://{public-ip-address}:5000/short/<qr-code-id> which redirects to target 

# GitHub Actions / CI CD Pipline
Make sure, `AWS_ACCESS_KEY` and `AWS_SECRET_KEY` are created/updated with valid credentials.

Go to Actions>> .github/workflows/aws-deploy.yml >> Run Workflow

# Infrastructure-as-Code
The entire application has two `AWS Cloudformation` stacks: `InfrastructureStack`, `QRCodeFlaskServerStack`. All three cloudformation stacks are deployed to `AWS` using [`AWS Cloud Development Kit`](https://aws.amazon.com/cdk/). 

This `cdk` application is a `Java` based. Make sure `node`, `aws-cdk`, `aws-cli`, `java`, `mvn` are installed on software release machine. 

# Future Plans/Improvements
- [ ] Add better front-end to interact with the application. 
- [ ] Integrate stripe payment gateway to the application. 
- [ ] Write test cases to acheive 100% coverage. 
 