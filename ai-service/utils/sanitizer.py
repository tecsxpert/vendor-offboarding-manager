import re

def sanitize_input(text):
    if not text:
        return ""
    return re.sub(r"<[^>]*>", "", text).strip()

def is_prompt_injection(text):
    if not text:
        return False

    suspicious_patterns = [
        r"ignore previous instructions",
        r"forget previous instructions",
        r"system prompt",
        r"developer message",
        r"bypass",
        r"jailbreak",
        r"act as"
    ]

    lowered = text.lower()

    for pattern in suspicious_patterns:
        if re.search(pattern, lowered):
            return True

    return False