"""
External Audit Coordinator — Flask AI Microservice
Port: 5000
"""

import os
from flask import Flask
from flask_cors import CORS
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from dotenv import load_dotenv

from routes.health import health_bp
from routes.chat import chat_bp
from routes.rag import rag_bp

load_dotenv()

def create_app() -> Flask:
    app = Flask(__name__)

    # ── CORS: allow requests from the React frontend ──────────
    CORS(app, resources={r"/*": {"origins": "*"}})

    # ── Rate limiter: 30 req/min per IP ───────────────────────
    limiter = Limiter(
        get_remote_address,
        app=app,
        default_limits=[f"{os.getenv('RATE_LIMIT', '30')} per minute"],
        storage_uri="memory://",
    )

    # ── Register blueprints ───────────────────────────────────
    app.register_blueprint(health_bp)
    app.register_blueprint(chat_bp,   url_prefix="/api/ai")
    app.register_blueprint(rag_bp,    url_prefix="/api/rag")

    return app


if __name__ == "__main__":
    port = int(os.getenv("FLASK_PORT", 5000))
    create_app().run(host="0.0.0.0", port=port, debug=False)
