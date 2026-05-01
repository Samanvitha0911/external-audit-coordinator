import os

print("🔥 RUNNING FILE:", os.path.abspath(__file__))

from flask import Flask

# Blueprint imports
from routes.categorise import categorise_bp
from routes.query import query_bp
from routes.health import health_bp
from routes.generate_report import report_bp   # Day 11 added

# Create Flask app
app = Flask(__name__)

# Register blueprints
app.register_blueprint(categorise_bp)
app.register_blueprint(query_bp)
app.register_blueprint(health_bp)
app.register_blueprint(report_bp)

# Home route
@app.route("/")
def home():
    return {"message": "Server working"}

# Debug route listing (safe for development)
print("📌 ROUTES:")
with app.app_context():
    for rule in app.url_map.iter_rules():
        print(rule)

# Run app
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)