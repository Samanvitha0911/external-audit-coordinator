from flask import Blueprint, request, jsonify
from services.groq_client import GroqClient
from services.cache_client import cache_client
import json
import time

categorise_bp = Blueprint("categorise", __name__)

client = GroqClient()


@categorise_bp.route("/categorise", methods=["POST"])
def categorise():
    start_time = time.time()

    data = request.get_json()
    text = data.get("text") if data else None

    if not text:
        return jsonify({"error": "Text is required"}), 400

    # ✅ CHECK CACHE
    cached_response = cache_client.get(text)
    if cached_response:
        cached_response["meta"]["cached"] = True
        cached_response["meta"]["response_time_ms"] = round((time.time() - start_time) * 1000, 2)
        return jsonify(cached_response)

    try:
        prompt = f"""
Classify the following text into one of these categories:
- Account Issues
- Billing
- Technical Support
- General Inquiry

Also provide:
- category
- confidence (0 to 1)
- reasoning

Text: {text}

Return JSON only.
"""

        # ✅ LLM CALL
        llm_response = client.generate_response(prompt)

        # ✅ Parse JSON from AI answer
        try:
            parsed_response = json.loads(llm_response["answer"])

            final_response = {
                **parsed_response,
                "meta": {
                    "cached": False,
                    "response_time_ms": round((time.time() - start_time) * 1000, 2),
                    "is_fallback": llm_response["is_fallback"]
                }
            }

            # ✅ SAVE TO CACHE
            cache_client.set(text, final_response)

            return jsonify(final_response)

        except json.JSONDecodeError:
            return jsonify({
                "error": "Invalid JSON from AI",
                "raw_response": llm_response["answer"],
                "meta": {
                    "is_fallback": llm_response["is_fallback"]
                }
            }), 500

    except Exception as e:
        return jsonify({
            "error": str(e),
            "meta": {
                "is_fallback": True
            }
        }), 500