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
### ✅ Day 9 – Response Metadata Object
- Added `meta` object to all API responses
- Included confidence score (0.0–1.0)
- Added model name used for response generation
- Included total token usage from Groq response
- Added response time in milliseconds
- Added dynamic cache status as boolean
- Tested cached and non-cached responses successfully in Postman

---
### ✅ Day 10 – Week 2 AI Quality Review
- Tested 10 fresh inputs for each active endpoint
- Evaluated response accuracy and output format
- Calculated average accuracy score
- Ensured target average >= 4/5
- Rewrote weak prompts for low-scoring cases
- Re-tested failing inputs after prompt tuning
- Saved evaluation report in `reports/Week2_AI_Quality_Review_Day10.xlsx`

---
### ✅ Day 11 – Async Job Processing for /generate-report
- Built POST `/generate-report` endpoint
- Returned `job_id` immediately with processing status
- Implemented background thread using Python threading
- Added GET `/job-status/<job_id>` endpoint
- Integrated webhook callback on completion
- Verified callback delivery using webhook.site

---
### ✅ Day 12 – Performance Benchmarking
- Benchmarked all endpoints with 50 requests each
- Measured p50, p95, p99, and average response time
- Identified `/categorise` as performance bottleneck
- Added cache-based optimization for repeated inputs
- Documented benchmark results in `reports/Day12_Performance_Benchmark_Report.xlsx`

---
### ✅ Day 13 – AI Fallback Handling
- Added fallback mechanism for Groq timeout/error
- Prevented API crashes on AI failure
- Returned safe template response
- Added `meta.is_fallback` flag
- Applied fallback across query and categorisation endpoints

---
### ✅ Day 14 – Final Prompt QA
- Tested all prompts against 30 seeded demo records
- Verified prompt quality for categorisation and query endpoints
- Checked formatting, correctness, and demo readiness
- Documented results in `reports/Day14_Final_Prompt_QA_Report.xlsx`

---
### ✅ Day 15 – AI Packaging & Docker Deployment Completion
- Containerized the AI backend service using Docker using Python 3.11 slim base image  
- Fixed dependency management by correctly configuring `requirements.txt` for Flask, Groq, Redis, and supporting libraries  
- Resolved multiple Docker build and runtime issues including missing modules and incorrect environment configuration  
- Configured `.env` support for secure handling of `GROQ_API_KEY` inside Docker container  
- Successfully built Docker image with clean installation of all dependencies without errors  
- Executed and validated the containerized application using `docker run` command  
- Verified all REST API endpoints (`/`, `/health`, `/query`, `/categorise`, `/generate-report`) inside the running container  
- Confirmed successful integration of AI service (Groq API) with backend workflow  
- Ensured the application is fully portable and ready for deployment in any environment using Docker  

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