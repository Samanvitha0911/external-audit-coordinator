from flask import Flask
from routes.categorise import categorise_bp

app = Flask(__name__)

# Register route
app.register_blueprint(categorise_bp)

if __name__ == "__main__":
    app.run(debug=True)