"""
Health check blueprint — GET /health
Returns 200 with service status. Used by Docker and the backend scheduler.
"""

from flask import Blueprint, jsonify

health_bp = Blueprint("health", __name__)


@health_bp.get("/health")
def health():
    return jsonify({"status": "ok", "service": "ai-service"}), 200
