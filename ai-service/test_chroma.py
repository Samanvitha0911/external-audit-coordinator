from services.chroma_client import ChromaClient

client = ChromaClient()

docs = [
    "Payment failed due to billing error",
    "Refund processed successfully",
    "User login failed due to incorrect password"
]

client.add_documents(docs)

print("✅ Documents added")