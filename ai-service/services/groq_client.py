import time
import os
from groq import Groq
from dotenv import load_dotenv

load_dotenv()


class GroqClient:
    def __init__(self):
        self.model_name = "llama-3.1-8b-instant"
        self.client = Groq(api_key=os.getenv("GROQ_API_KEY"))

    def generate_response(self, prompt):
        start = time.time()

        response = self.client.chat.completions.create(
            model=self.model_name,
            messages=[
                {"role": "user", "content": prompt}
            ]
        )

        end = time.time()

        answer = response.choices[0].message.content

        tokens_used = response.usage.total_tokens

        response_time_ms = round((end - start) * 1000, 2)

        return {
            "answer": answer,
            "model_used": self.model_name,
            "tokens_used": tokens_used,
            "response_time_ms": response_time_ms
        }