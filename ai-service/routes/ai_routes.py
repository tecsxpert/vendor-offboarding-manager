from flask import Blueprint, request, jsonify
from services.groq_client import get_ai_response
from utils.sanitizer import sanitize_input, is_prompt_injection
from datetime import datetime, timezone

ai_bp = Blueprint("ai", __name__)

@ai_bp.route("/test", methods=["GET"])
def test():
    return jsonify({"message": "AI route working"})

@ai_bp.route("/describe", methods=["POST"])
def describe():
    data = request.get_json()

    if not data:
        return jsonify({"error": "No input provided"}), 400

    user_input = data.get("input")

    if not user_input:
        return jsonify({"error": "Input is required"}), 400

    user_input = sanitize_input(user_input)

    if is_prompt_injection(user_input):
        return jsonify({"error": "Malicious input detected"}), 400

    result = get_ai_response(user_input)

    if "error" in result:
        return jsonify(result), 500

    return jsonify({
        "description": result.get("response", ""),
        "generated_at": datetime.now(timezone.utc).isoformat()
    })