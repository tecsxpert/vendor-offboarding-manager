import re

# List of suspicious keywords
SUSPICIOUS_PATTERNS = [
    "ignore previous instructions",
    "system prompt",
    "reveal secrets",
    "bypass",
    "jailbreak"
]

def sanitize_input(user_input):
    # Remove HTML tags
    clean_text = re.sub(r'<.*?>', '', user_input)
    return clean_text.strip()


def is_prompt_injection(user_input):
    lower_input = user_input.lower()

    for pattern in SUSPICIOUS_PATTERNS:
        if pattern in lower_input:
            return True

    return False
