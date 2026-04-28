from flask import Blueprint, request, jsonify
from services.groq_client import get_ai_response
from utils.sanitizer import sanitize_input, is_prompt_injection
from datetime import datetime, timezone
import json
import re
import logging

ai_bp = Blueprint("ai", __name__)


# -------- DESCRIBE --------
@ai_bp.route("/describe", methods=["POST"])
def describe():
    logging.info("Describe API called")

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
Explain why this vendor should be offboarded:

{user_input}
"""

    result = get_ai_response(prompt)

    if "error" in result:
        return jsonify({
            "description": "",
            "generated_at": datetime.now(timezone.utc).isoformat(),
            "error": result["error"]
        }), 500

    return jsonify({
        "description": result.get("response", ""),
        "generated_at": datetime.now(timezone.utc).isoformat()
    })


# -------- HELPER --------
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


# -------- RECOMMEND --------
@ai_bp.route("/recommend", methods=["POST"])
def recommend():
    logging.info("Recommend API called")

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

Return exactly 3 recommendations as a VALID JSON ARRAY.

STRICT RULES:
- Only JSON (no explanation)
- No markdown
- No extra text
- Each item must have:
  action_type, description, priority

Priority must be: High, Medium, or Low

Vendor Details:
{user_input}
"""

    result = get_ai_response(prompt)

    if "error" in result:
        return jsonify({
            "recommendations": [],
            "is_fallback": True,
            "error": result["error"]
        }), 500

    raw_text = result.get("response", "").strip()
    recommendations = extract_json_array(raw_text)

    if not recommendations or not isinstance(recommendations, list):
        return jsonify({
            "recommendations": [],
            "generated_at": datetime.now(timezone.utc).isoformat(),
            "is_fallback": True,
            "raw_response": raw_text
        })

    recommendations = recommendations[:3]

    return jsonify({
        "recommendations": recommendations,
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "is_fallback": False
    })


# -------- GENERATE REPORT --------
@ai_bp.route("/generate-report", methods=["POST"])
def generate_report():
    logging.info("Generate Report API called")

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
Generate a professional vendor offboarding report for the following case:

{user_input}

Include:
- Offboarding Reason
- Risk Summary
- Recommended Next Steps
"""

    result = get_ai_response(prompt)

    if "error" in result:
        return jsonify({
            "report": "",
            "generated_at": datetime.now(timezone.utc).isoformat(),
            "error": result["error"]
        }), 500

    return jsonify({
        "report": result.get("response", ""),
        "generated_at": datetime.now(timezone.utc).isoformat()
    })