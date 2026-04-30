from flask import Blueprint, request, jsonify
from services.chroma_client import ChromaClient
from services.groq_client import GroqClient
from services.cache_client import CacheClient

query_bp = Blueprint("query", __name__)

chroma_client = ChromaClient()
groq_client = GroqClient()
cache_client = CacheClient()


@query_bp.route("/query", methods=["POST"])
def query():
    import time
    start = time.time()

    data = request.get_json()

    if not data or "question" not in data:
        return jsonify({"error": "Question is required"}), 400

    question = data.get("question")
    fresh = data.get("fresh", False)

    # CHECK CACHE
    if not fresh:
        cached_response = cache_client.get(question)

        if cached_response:
            cached_response["meta"]["cached"] = True
            return jsonify(cached_response)

    # GET SOURCES
    sources = chroma_client.query(question, n_results=3)
    context = "\n".join(sources)

    prompt = f"""
You are an AI assistant for external audit support.

Answer ONLY using the provided context.
If answer is not available, say:
'Insufficient information available in the provided context.'

Context:
{context}

Question:
{question}
"""

    # LLM RESPONSE
    llm_response = groq_client.generate_response(prompt)

    confidence = 0.95 if "Insufficient" not in llm_response["answer"] else 0.70

    final_response = {
        "answer": llm_response["answer"],
        "sources": sources,
        "meta": {
            "confidence": confidence,
            "model_used": llm_response["model_used"],
            "tokens_used": llm_response["tokens_used"],
            "response_time_ms": llm_response["response_time_ms"],
            "cached": False,
            "is_fallback": llm_response["is_fallback"]
        }
    }

    cache_client.set(question, final_response)

    return jsonify(final_response)