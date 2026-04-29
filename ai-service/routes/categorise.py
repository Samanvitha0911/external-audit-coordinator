from flask import Blueprint, request, jsonify
from services.groq_client import GroqClient
import json

categorise_bp = Blueprint("categorise", __name__)

client = GroqClient()

@categorise_bp.route("/categorise", methods=["POST"])
def categorise():
    data = request.get_json()
    text = data.get("text")

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