from services.groq_client import GroqClient

client = GroqClient()

response = client.generate_response("Explain audit in one line")

print(response)