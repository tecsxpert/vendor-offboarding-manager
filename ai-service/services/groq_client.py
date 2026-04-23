import requests
import os
from dotenv import load_dotenv

load_dotenv()

API_KEY = os.getenv("GROQ_API_KEY")

def get_ai_response(user_input):
    url = "https://api.groq.com/openai/v1/chat/completions"

    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json"
    }

    data = {
        "model": "llama-3.3-70b-versatile",
        "messages": [
            {
                "role": "user",
                "content": user_input
            }
        ],
        "temperature": 0.2
    }

    response = requests.post(url, headers=headers, json=data)

    try:
        res_json = response.json()
    except:
        return {"error": "Invalid response from API"}

    if "error" in res_json:
        return {"error": res_json["error"]["message"]}

    try:
        answer = res_json["choices"][0]["message"]["content"]
        return {"response": answer}
    except:
        return {
            "error": "Unexpected API response",
            "full_response": res_json
        }