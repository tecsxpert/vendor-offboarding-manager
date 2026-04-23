from flask import Flask
from routes.ai_routes import ai_bp
import logging

app = Flask(__name__)

app.register_blueprint(ai_bp)

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s"
)

@app.route("/")
def home():
    return "AI Service Running"

@app.route("/health", methods=["GET"])
def health():
    return {"status": "ok", "service": "AI Service Running"}

if __name__ == "__main__":
    app.run(port=5000, debug=True)