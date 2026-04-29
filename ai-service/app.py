from flask import Flask
from routes.ai_routes import ai_bp
import logging
 
app = Flask(__name__)
 
# Register AI Blueprint
app.register_blueprint(ai_bp)
 
# Logging Config
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s"
)
 
# Home Route
@app.route("/")
def home():
    return "AI Service Running 🚀"
 
# Health Check Route
@app.route("/health", methods=["GET"])
def health():
    return {
        "status": "ok",
        "service": "AI Service Running"
    }
 
if __name__ == "__main__":
    app.run(port=5000, debug=True)