import requests
import time
import statistics

BASE_URL = "http://127.0.0.1:5000"

results = {}

def percentile(data, p):
    data = sorted(data)
    index = int(len(data) * p / 100)
    return data[min(index, len(data)-1)]

def benchmark(endpoint, method="GET", payload=None, count=50):
    times = []

    for _ in range(count):
        start = time.time()

        if method == "GET":
            requests.get(f"{BASE_URL}{endpoint}")
        else:
            requests.post(f"{BASE_URL}{endpoint}", json=payload)

        end = time.time()
        times.append((end - start) * 1000)

    return {
        "p50": round(percentile(times, 50), 2),
        "p95": round(percentile(times, 95), 2),
        "p99": round(percentile(times, 99), 2),
        "avg": round(statistics.mean(times), 2)
    }

results["/"] = benchmark("/")
results["/health"] = benchmark("/health")
results["/categorise"] = benchmark(
    "/categorise",
    method="POST",
    payload={"text": "payment issue"}
)
results["/query"] = benchmark(
    "/query",
    method="POST",
    payload={"question": "Why did payment fail?"}
)

for endpoint, stats in results.items():
    print(f"\nEndpoint: {endpoint}")
    print(stats)