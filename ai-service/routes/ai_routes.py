# Day 4 recommend endpoint added
from flask import Blueprint, request, jsonify
from services.groq_client import get_ai_response
from utils.sanitizer import sanitize_input, is_prompt_injection
from datetime import datetime, timezone
import json
import re

ai_bp = Blueprint("ai", __name__)

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

    return jsonify({
        "description": result.get("response", ""),
        "generated_at": datetime.now(timezone.utc).isoformat()
    })


def extract_json_array(text):
    try:
        return json.loads(text)
    except:
        pass

    match = re.search(r"\[.*\]", text, re.DOTALL)
    if match:
        try:
            return json.loads(match.group(0))
        except:
            pass

    return None


@ai_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()

    if not data:
        return jsonify({"error": "No input provided"}), 400

    user_input = data.get("input")

    if not user_input:
        return jsonify({"error": "Input is required"}), 400

    user_input = sanitize_input(user_input)

    if is_prompt_injection(user_input):
        return jsonify({"error": "Malicious input detected"}), 400

    prompt = f"""
You are an AI assistant for vendor offboarding.

Given the vendor details below, return exactly 3 recommendations as a JSON array.

Each recommendation must contain:
- action_type
- description
- priority

Priority must be one of: High, Medium, Low.

Vendor Details:
{user_input}

Return only valid JSON.
"""

    result = get_ai_response(prompt)

    raw_text = result.get("response", "")
    recommendations = extract_json_array(raw_text)

    if not recommendations:
        return jsonify({
            "recommendations": [],
            "generated_at": datetime.now(timezone.utc).isoformat(),
            "is_fallback": True
        })

    return jsonify({
        "recommendations": recommendations,
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "is_fallback": False
    })