# External Audit AI Service

## 📌 Project Overview
This project is part of an AI-based External Audit System. The goal is to build intelligent services that can classify user queries, generate responses using LLMs, and enable semantic search using vector databases.

---

## 🚀 Tech Stack
- Python
- Flask
- Groq API
- ChromaDB

---

## 📅 Work Progress

### ✅ Day 1 – Setup
- Project environment setup
- Installed required dependencies
- Configured folder structure

---

### ✅ Day 2 – API Development
- Created Flask application
- Implemented basic API endpoints
- Tested API using Postman

---

### ✅ Day 3 – Groq Integration
- Integrated Groq API for text processing
- Built categorisation API
- Generated structured JSON responses (category, confidence, reasoning)
- Handled API errors and retries

---

### ✅ Day 4 – ChromaDB Integration
- Integrated ChromaDB for vector storage
- Created a collection to store documents
- Converted text into embeddings
- Implemented semantic search using query
- Tested retrieval using `test_chroma.py`

---
### ✅ Day 5 – Query API with RAG Pipeline
- Built POST `/query` endpoint
- Accepted user questions through API
- Retrieved top 3 relevant chunks from ChromaDB
- Injected retrieved chunks as context
- Integrated Groq LLM for answer generation
- Returned JSON response with:
  - `answer`
  - `sources`
- Fixed persistent ChromaDB storage using `PersistentClient`
- Tested end-to-end flow using Postman

---
### ✅ Day 6 – Prompt Tuning & Evaluation
- Tested prompts against 10 real user inputs
- Evaluated responses based on accuracy and output format
- Identified weak responses below 7/10
- Rewrote prompt instructions in `query.py` to enforce strict context-only answers
- Re-tested tuned prompts and compared before vs after results
- Saved evaluation report in `reports/Day6_Prompt_Tuning_Report.xlsx`

---
### ✅ Day 7 – Health Monitoring API
- Built GET `/health` endpoint
- Added system health monitoring response
- Included model name, average response time, ChromaDB document count
- Implemented uptime tracking
- Added cache hit/miss statistics
- Tested endpoint successfully using Postman

---
### ✅ Day 8 – Redis AI Cache
- Implemented AI response caching layer
- Used SHA256 hash for cache key generation
- Added 15-minute TTL for cached responses
- Implemented cache hit and miss counters
- Added support to skip cache for fresh requests
- Added graceful fallback to in-memory cache if Redis is unavailable
- Tested cache miss, cache hit, and fresh request flow successfully in Postman

---
## 🧠 Features
- Text classification into categories
- AI-generated responses using Groq LLM
- Semantic search using ChromaDB
- RAG-based query answering with sources
- Prompt tuning and response evaluation
- Health monitoring API for service status

---

## ▶️ How to Run

```bash
pip install -r requirements.txt
python app.py