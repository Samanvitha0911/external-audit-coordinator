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
    text = data.get("text")

    if not text:
        return jsonify({"error": "Text is required"}), 400

    # ✅ Check cache first
    cached_response = cache_client.get(text)
    if cached_response:
        return jsonify({
            **cached_response,
            "meta": {
                "cached": True,
                "response_time_ms": round((time.time() - start_time) * 1000, 2)
            }
        })

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

        response = client.generate_response(prompt)

        try:
            parsed_response = json.loads(response)

            # ✅ Save response to cache
            cache_client.set(text, parsed_response)

            return jsonify({
                **parsed_response,
                "meta": {
                    "cached": False,
                    "response_time_ms": round((time.time() - start_time) * 1000, 2)
                }
            })

        except json.JSONDecodeError:
            return jsonify({
                "error": "Invalid JSON from AI",
                "raw_response": response
            }), 500

    except Exception as e:
        return jsonify({"error": str(e)}), 500
    if not text:
        return jsonify({"error": "Text is required"}), 400

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

        response = client.generate_response(prompt)

        # 🔥 Convert string → JSON
        try:
            parsed_response = json.loads(response)
            return jsonify(parsed_response)
        except json.JSONDecodeError:
            return jsonify({
                "error": "Invalid JSON from AI",
                "raw_response": response
            }), 500

    except Exception as e:
        return jsonify({"error": str(e)}), 500