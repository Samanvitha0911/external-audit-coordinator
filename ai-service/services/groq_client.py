import os
import time
import logging
from groq import Groq
from dotenv import load_dotenv

load_dotenv()

logging.basicConfig(level=logging.ERROR)

class GroqClient:
    def __init__(self):
        self.api_key = os.getenv("GROQ_API_KEY")
        print("API KEY LOADED:", self.api_key)
        self.client = Groq(api_key=self.api_key)

    def generate_response(self, prompt):
        retries = 3
        delay = 2

        for attempt in range(retries):
            try:
                response = self.client.chat.completions.create(
                    model="llama-3.1-8b-instant",
                    messages=[
                        {"role": "user", "content": prompt}
                    ]
                )

                return response.choices[0].message.content

            except Exception as e:
                print("FULL ERROR:", e) 
                logging.error(f"Attempt {attempt+1} failed: {e}")

                if attempt < retries - 1:
                    time.sleep(delay)
                    delay *= 2
                else:
                    return "Error: Unable to get response"