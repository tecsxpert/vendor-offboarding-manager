from flask import Blueprint, request, jsonify
from services.groq_client import get_ai_response
from utils.sanitizer import sanitize_input, is_prompt_injection
from datetime import datetime, timezone
import json
import re
import logging

ai_bp = Blueprint("ai", __name__)

# Simple in-memory history
history = []


def current_time():
    return datetime.now(timezone.utc).isoformat()


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


# -------- DESCRIBE --------
@ai_bp.route("/describe", methods=["POST"])
def describe():
    logging.info("Describe API called")

    try:
        data = request.get_json()

        if not data:
            return jsonify({"success": False, "error": "No input provided", "timestamp": current_time()}), 400

        user_input = data.get("input")

        if not user_input:
            return jsonify({"success": False, "error": "Input is required", "timestamp": current_time()}), 400

        user_input = sanitize_input(user_input)

        if is_prompt_injection(user_input):
            return jsonify({"success": False, "error": "Malicious input detected", "timestamp": current_time()}), 400

        prompt = f"""
You are an AI assistant for vendor offboarding.

Given the vendor details below, generate a clear, professional, and concise description explaining why the vendor should be offboarded.

Focus on:
- Risks: security, compliance, performance
- Business impact
- Reason for offboarding

Vendor Details:
{user_input}

Return only the description in a professional tone.
"""

        result = get_ai_response(prompt)

        if "error" in result:
            return jsonify({
                "success": False,
                "description": "",
                "error": result["error"],
                "generated_at": current_time()
            }), 500

        response_data = {
            "description": result.get("response", ""),
            "generated_at": current_time()
        }

        history.append({
            "type": "describe",
            "input": user_input,
            "timestamp": current_time()
        })

        return jsonify({
            "success": True,
            "data": response_data
        })

    except Exception as e:
        logging.error(f"Describe API error: {str(e)}")
        return jsonify({"success": False, "error": str(e), "timestamp": current_time()}), 500


# -------- RECOMMEND --------
@ai_bp.route("/recommend", methods=["POST"])
def recommend():
    logging.info("Recommend API called")

    try:
        data = request.get_json()

        if not data:
            return jsonify({"success": False, "error": "No input provided", "timestamp": current_time()}), 400

        user_input = data.get("input")

        if not user_input:
            return jsonify({"success": False, "error": "Input is required", "timestamp": current_time()}), 400

        user_input = sanitize_input(user_input)

        if is_prompt_injection(user_input):
            return jsonify({"success": False, "error": "Malicious input detected", "timestamp": current_time()}), 400

        prompt = f"""
You are an AI assistant for vendor offboarding.

Return exactly 3 recommendations as a VALID JSON ARRAY.

STRICT RULES:
- Only JSON
- No explanation
- No markdown
- No extra text
- Each item must have: action_type, description, priority
- Priority must be: High, Medium, or Low

Vendor Details:
{user_input}
"""

        result = get_ai_response(prompt)

        if "error" in result:
            return jsonify({
                "success": False,
                "recommendations": [],
                "is_fallback": True,
                "error": result["error"],
                "timestamp": current_time()
            }), 500

        raw_text = result.get("response", "").strip()
        recommendations = extract_json_array(raw_text)

        if not recommendations or not isinstance(recommendations, list):
            return jsonify({
                "success": True,
                "recommendations": [],
                "generated_at": current_time(),
                "is_fallback": True,
                "raw_response": raw_text
            })

        recommendations = recommendations[:3]

        history.append({
            "type": "recommend",
            "input": user_input,
            "timestamp": current_time()
        })

        return jsonify({
            "success": True,
            "recommendations": recommendations,
            "generated_at": current_time(),
            "is_fallback": False
        })

    except Exception as e:
        logging.error(f"Recommend API error: {str(e)}")
        return jsonify({"success": False, "error": str(e), "timestamp": current_time()}), 500


# -------- GENERATE REPORT --------
@ai_bp.route("/generate-report", methods=["POST"])
def generate_report():
    logging.info("Generate Report API called")

    try:
        data = request.get_json()

        if not data:
            return jsonify({"success": False, "error": "No input provided", "timestamp": current_time()}), 400

        user_input = data.get("input")

        if not user_input:
            return jsonify({"success": False, "error": "Input is required", "timestamp": current_time()}), 400

        user_input = sanitize_input(user_input)

        if is_prompt_injection(user_input):
            return jsonify({"success": False, "error": "Malicious input detected", "timestamp": current_time()}), 400

        prompt = f"""
You are an AI assistant.

Generate a structured vendor offboarding report.

Format strictly:

1. Offboarding Reason:
2. Risk Summary:
3. Business Impact:
4. Recommended Actions:

Vendor Details:
{user_input}

Return clean professional text.
"""

        result = get_ai_response(prompt)

        if "error" in result:
            return jsonify({
                "success": False,
                "report": "",
                "error": result["error"],
                "generated_at": current_time()
            }), 500

        history.append({
            "type": "generate-report",
            "input": user_input,
            "timestamp": current_time()
        })

        return jsonify({
            "success": True,
            "report": result.get("response", ""),
            "generated_at": current_time()
        })

    except Exception as e:
        logging.error(f"Generate Report API error: {str(e)}")
        return jsonify({"success": False, "error": str(e), "timestamp": current_time()}), 500


# -------- HISTORY --------
@ai_bp.route("/history", methods=["GET"])
def get_history():
    logging.info("History API called")

    return jsonify({
        "success": True,
        "history": history,
        "count": len(history),
        "timestamp": current_time()
    })