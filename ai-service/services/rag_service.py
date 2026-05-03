import os
import chromadb
from chromadb.config import Settings
from sentence_transformers import SentenceTransformer
from services.groq_service import get_groq_response

# Initialize ChromaDB client (persistent storage)
persist_directory = "/app/chroma_store" if os.getenv("FLASK_PORT") else "./chroma_store"
chroma_client = chromadb.PersistentClient(path=persist_directory)

# Create or get a collection for audit documents
collection = chroma_client.get_or_create_collection(name="audit_documents")

# Load embedding model
embedder = SentenceTransformer('all-MiniLM-L6-v2')

def ingest_document(doc_id: str, text: str, metadata: dict = None):
    """
    Embeds text and stores it in ChromaDB.
    Note: In a real app, you'd chunk the text first if it's long.
    """
    embedding = embedder.encode(text).tolist()
    
    collection.add(
        ids=[doc_id],
        embeddings=[embedding],
        documents=[text],
        metadatas=[metadata or {}]
    )

def query_rag(question: str, top_k: int = 5):
    """
    Retrieves relevant chunks and generates an answer using Groq.
    """
    # 1. Embed the question
    query_embedding = embedder.encode(question).tolist()
    
    # 2. Retrieve top-k chunks from ChromaDB
    results = collection.query(
        query_embeddings=[query_embedding],
        n_results=top_k
    )
    
    documents = results.get('documents', [[]])[0]
    sources = results.get('metadatas', [[]])[0]
    
    if not documents:
        return "I couldn't find any relevant information in the uploaded documents to answer your question.", []
        
    # 3. Construct context
    context = "\n\n---\n\n".join(documents)
    
    # 4. Generate answer with Groq
    prompt = f"Based ONLY on the provided context, answer this question: {question}"
    answer = get_groq_response(prompt, context)
    
    return answer, sources
