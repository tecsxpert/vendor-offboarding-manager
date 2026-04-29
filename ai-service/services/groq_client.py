import os
from groq import Groq
from dotenv import load_dotenv
import time

load_dotenv()

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

def get_ai_response(user_input):
    retries = 3

    # Load prompt template
    try:
        with open("prompts/describe_prompt.txt", "r") as file:
            template = file.read()
    except Exception as e:
        print("Error loading prompt file:", e)
        template = "{input}"

    # Insert user input into template
    prompt = template.replace("{input}", user_input)

    for attempt in range(retries):
        try:
            response = client.chat.completions.create(
                model="llama-3.3-70b-versatile",
                messages=[
                    {"role": "user", "content": prompt}
                ]
            )

            return {
                "response": response.choices[0].message.content,
                "is_fallback": False
            }

        except Exception as e:
            print(f"Error: {e}, retrying...")
            time.sleep(2)

    # Fallback response
    return {
        "response": "AI service temporarily unavailable",
        "is_fallback": True
    }