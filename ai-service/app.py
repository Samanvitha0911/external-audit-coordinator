import os
print("🔥 RUNNING FILE:", os.path.abspath(__file__))

from flask import Flask
from routes.categorise import categorise_bp
from routes.query import query_bp
from routes.health import health_bp
from routes.generate_report import report_bp   # Day 11 added

app = Flask(__name__)

# Register all routes
app.register_blueprint(categorise_bp)
app.register_blueprint(query_bp)
app.register_blueprint(health_bp)
app.register_blueprint(report_bp)   # Day 11 added

@app.route("/")
def home():
    return "Server working"

print("📌 ROUTES:")
for rule in app.url_map.iter_rules():
    print(rule)

if __name__ == "__main__":
    app.run(debug=True)