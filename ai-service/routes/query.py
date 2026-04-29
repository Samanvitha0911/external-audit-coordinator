from flask import Blueprint, request, jsonify
from services.chroma_client import ChromaClient
from services.groq_client import GroqClient
from services.cache_client import CacheClient   # ✅ NEW

query_bp = Blueprint("query", __name__)

chroma_client = ChromaClient()
groq_client = GroqClient()
cache_client = CacheClient()   # ✅ NEW


@query_bp.route("/query", methods=["POST"])
def query():
    data = request.get_json()

    if not data or "question" not in data:
        return jsonify({"error": "Question is required"}), 400

    question = data.get("question")
    fresh = data.get("fresh", False)   # ✅ skip cache if true

    # ✅ CHECK CACHE FIRST
    if not fresh:
        cached_response = cache_client.get(question)

        if cached_response:
            return jsonify({
                "answer": cached_response["answer"],
                "sources": cached_response["sources"],
                "cache": "hit"
            })

    # ✅ CACHE MISS → NORMAL FLOW
    sources = chroma_client.query(question, n_results=3)

    context = "\n".join(sources)

    prompt = f"""
You are an AI assistant for external audit support.

Answer ONLY using the provided context.
Do not assume, guess, or speculate beyond the context.

If the answer is not clearly available in the context, respond with:
'Insufficient information available in the provided context.'

Provide a direct, concise, and accurate answer.

Context:
{context}

Question:
{question}
"""

    answer = groq_client.generate_response(prompt)

    response_data = {
        "answer": answer,
        "sources": sources
    }

    # ✅ STORE IN CACHE (15 min TTL)
    cache_client.set(question, response_data)

    return jsonify({
        **response_data,
        "cache": "miss"
    })