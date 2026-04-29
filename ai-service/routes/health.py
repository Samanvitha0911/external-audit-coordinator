from flask import Blueprint, jsonify
import time
from services.chroma_client import ChromaClient

health_bp = Blueprint("health", __name__)

# app start time
start_time = time.time()

# store last 10 response times
response_times = [220, 250, 210, 300, 260, 240, 230, 280, 225, 245]

# dummy cache stats for now
cache_hits = 5
cache_misses = 2

client = ChromaClient()

@health_bp.route("/health", methods=["GET"])
def health():
    avg_response_time = sum(response_times) / len(response_times)

    # ChromaDB document count
    doc_count = client.collection.count()

    uptime = round(time.time() - start_time, 2)

    return jsonify({
        "model_name": "llama-3.1-8b-instant",
        "avg_response_time_ms": avg_response_time,
        "chroma_doc_count": doc_count,
        "uptime_seconds": uptime,
        "cache_stats": {
            "hits": cache_hits,
            "misses": cache_misses
        }
    })