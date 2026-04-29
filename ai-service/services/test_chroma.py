from services.chroma_client import ChromaClient

client = ChromaClient()

# sample data
docs = [
    "Audit ensures financial accuracy",
    "Login issue in system",
    "Payment failed due to billing error"
]

# add documents
client.add_documents(docs)

# query
result = client.query("There is a billing problem")

print(result)