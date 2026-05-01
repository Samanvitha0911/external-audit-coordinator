import time
import os
import json
import re
from groq import Groq
from dotenv import load_dotenv

load_dotenv()


class GroqClient:
    def __init__(self):
        self.model_name = "llama-3.1-8b-instant"
        self.client = Groq(api_key=os.getenv("GROQ_API_KEY"))

    # ✅ NEW: safe JSON extractor
    def extract_json(self, text):
        # remove markdown wrappers
        text = re.sub(r"```json", "", text)
        text = re.sub(r"```", "", text)

        # extract only JSON part
        start = text.find("{")
        end = text.rfind("}")

        if start == -1 or end == -1:
            raise ValueError("No JSON found in response")

        clean_text = text[start:end+1]

        return json.loads(clean_text)

    def generate_response(self, prompt):
        start = time.time()

        try:
            response = self.client.chat.completions.create(
                model=self.model_name,
                messages=[
                    {"role": "user", "content": prompt}
                ]
            )

            end = time.time()

            raw_output = response.choices[0].message.content

            tokens_used = response.usage.total_tokens
            response_time_ms = round((end - start) * 1000, 2)

            # ✅ FIX: ensure clean parsing
            try:
                answer = self.extract_json(raw_output)
            except Exception:
                # fallback if AI doesn't return valid JSON
                answer = {
                    "raw_response": raw_output,
                    "parse_error": "Invalid JSON from AI"
                }

            return {
                "answer": answer,
                "model_used": self.model_name,
                "tokens_used": tokens_used,
                "response_time_ms": response_time_ms,
                "is_fallback": False
            }

        except Exception as e:
            end = time.time()

            print("⚠️ Groq timeout/error:", str(e))

            response_time_ms = round((end - start) * 1000, 2)

            return {
                "answer": "We are currently unable to process your request. Please try again later.",
                "model_used": self.model_name,
                "tokens_used": 0,
                "response_time_ms": response_time_ms,
                "is_fallback": True
            }