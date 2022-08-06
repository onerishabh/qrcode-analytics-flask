from io import BytesIO
from flask import Flask, request, send_file, redirect, session
import qrcode as qr
import src.qrcode as qrcode
import src.customer as customer
import os

app = Flask(__name__)

def serve_pil_image(pil_img):
    img_io = BytesIO()
    pil_img.save(img_io, 'JPEG', quality=70)
    img_io.seek(0)
    return send_file(img_io, mimetype='image/jpeg')

def hello():
    if "email" in session:
        return f"<h1>Hello, {session['email']}</h1>"
    salt=os.environ.get("STATIC_SALT")
    return f'<h1>Hello, World! {salt}</h1>'

def qr_code():
    if "email" not in session:
        return "Auth User First"
    link = request.args.get("val")

    short_url = qrcode.create_short_url(link, session["email"])

    print(request.headers.get('User-Agent'))

    img = qr.make(short_url)

    return serve_pil_image(img)

def redirect_func(id: str):
    target_url = qrcode.get_target_url(id)
    
    qrcode.add_new_invocation(id, request.headers.get('User-Agent'))
    
    return redirect(target_url, code=302)

def registerUser():
    name = request.args.get("name")
    email = request.args.get("email")
    password = request.args.get("password")
    customer.addUser(name, email, password)

    return "User Added"

def authUser():
    email = request.args.get("email")
    password = request.args.get("password")

    if customer.authUser(email, password):
        session["email"] = email
        return "User Authenticated"
    
    session.pop("email")
    return "User Not Authenticated"


app = Flask(__name__)

app.add_url_rule("/", "hello", hello)
app.add_url_rule("/qrcode", "qr_code", qr_code)
app.add_url_rule("/registerUser", "registerUser", registerUser)
app.add_url_rule("/authUser", "authUser", authUser)
app.add_url_rule("/short/<string:id>", "redirect_func", redirect_func)
app.secret_key = ".."

if __name__ == "__main__":
    app.debug = True
    
    app.run()