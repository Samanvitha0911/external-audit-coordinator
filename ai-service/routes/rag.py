"""
RAG blueprint — ChromaDB vector search pipeline
POST /api/rag/ingest  — embed and store a document chunk
POST /api/rag/query   — semantic search + Groq answer generation
"""

from flask import Blueprint, request, jsonify
from services.rag_service import ingest_document, query_rag

rag_bp = Blueprint("rag", __name__)


@rag_bp.post("/ingest")
def ingest():
    body = request.get_json(silent=True) or {}
    doc_id: str   = body.get("docId", "")
    text: str     = body.get("text", "").strip()
    metadata: dict = body.get("metadata", {})

    if not doc_id or not text:
        return jsonify({"error": "docId and text are required"}), 400

    try:
        ingest_document(doc_id, text, metadata)
        return jsonify({"message": f"Document '{doc_id}' ingested successfully"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@rag_bp.post("/query")
def query():
    body = request.get_json(silent=True) or {}
    question: str = body.get("question", "").strip()
    top_k: int    = int(body.get("topK", 5))

    if not question:
        return jsonify({"error": "question is required"}), 400

    try:
        answer, sources = query_rag(question, top_k)
        return jsonify({"answer": answer, "sources": sources}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
