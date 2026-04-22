from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from flask import Flask, request, jsonify
from services.groq_client import get_ai_response
from utils.sanitizer import sanitize_input, is_prompt_injection

app = Flask(__name__)

limiter = Limiter(
    get_remote_address,
    app=app,
    default_limits=["30 per minute"]
)

@app.route("/")
def home():
    return "AI Service Running 🚀"

@app.route("/describe", methods=["POST"])
@limiter.limit("30 per minute")
def describe():
    data = request.json

    user_input = data.get("input")

    if not user_input:
        return jsonify({"error": "Input is required"}), 400

    # 🔐 sanitize input
    user_input = sanitize_input(user_input)

    # 🔐 prompt injection check
    if is_prompt_injection(user_input):
        return jsonify({"error": "Malicious input detected"}), 400

    result = get_ai_response(user_input)

    return jsonify(result)

# 🔥 THIS IS REQUIRED
if __name__ == "__main__":
    app.run(debug=True, port=5000)