from flask import Blueprint, request, jsonify
import threading
import time
import uuid
import requests

report_bp = Blueprint("report", __name__)

jobs = {}

def process_report(job_id, data):
    time.sleep(5)   # simulate report generation

    report_result = {
        "status": "completed",
        "report": f"Generated report for {data.get('name', 'audit')}"
    }

    jobs[job_id] = report_result

    webhook_url = data.get("webhook_url")

    if webhook_url:
        try:
            requests.post(webhook_url, json={
                "job_id": job_id,
                "status": "completed",
                "report": report_result["report"]
            })
        except Exception as e:
            print("Webhook failed:", e)

@report_bp.route("/generate-report", methods=["POST"])
def generate_report():
    data = request.get_json()

    job_id = str(uuid.uuid4())

    jobs[job_id] = {"status": "processing"}

    thread = threading.Thread(
        target=process_report,
        args=(job_id, data)
    )
    thread.start()

    return jsonify({
        "job_id": job_id,
        "status": "processing"
    }), 202


@report_bp.route("/job-status/<job_id>", methods=["GET"])
def job_status(job_id):
    job = jobs.get(job_id)

    if not job:
        return jsonify({"error": "Job not found"}), 404

    return jsonify(job)