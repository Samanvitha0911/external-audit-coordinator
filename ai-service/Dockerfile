# ══════════════════════════════════════════════
#  AI Service Dockerfile — Python 3.11 / Flask
#  Serves via Gunicorn in production
# ══════════════════════════════════════════════
FROM python:3.11-slim

WORKDIR /app

# System deps for sentence-transformers / chromadb
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Install Python dependencies (cached layer)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy application source
COPY . .

# Persist ChromaDB data
VOLUME ["/app/chroma_store"]

EXPOSE 5000

# Gunicorn: 2 sync workers, 120s timeout (model loading can be slow)
CMD ["gunicorn", "--bind", "0.0.0.0:5000", "--workers", "2", "--timeout", "120", "app:create_app()"]
