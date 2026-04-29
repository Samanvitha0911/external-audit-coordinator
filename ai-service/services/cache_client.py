import redis
import hashlib
import json

class CacheClient:
    def __init__(self):
        self.hits = 0
        self.misses = 0
        self.memory_cache = {}

        try:
            self.client = redis.Redis(
                host="localhost",
                port=6379,
                decode_responses=True
            )
            self.client.ping()
            self.redis_available = True
            print("✅ Redis connected")
        except Exception:
            self.redis_available = False
            print("⚠️ Redis not running, using memory cache")

    def generate_key(self, text):
        return hashlib.sha256(text.encode()).hexdigest()

    def get(self, text):
        key = self.generate_key(text)

        if self.redis_available:
            data = self.client.get(key)
            if data:
                self.hits += 1
                return json.loads(data)
        else:
            if key in self.memory_cache:
                self.hits += 1
                return self.memory_cache[key]

        self.misses += 1
        return None

    def set(self, text, value):
        key = self.generate_key(text)

        if self.redis_available:
            self.client.setex(key, 900, json.dumps(value))
        else:
            self.memory_cache[key] = value

    def stats(self):
        return {
            "hits": self.hits,
            "misses": self.misses
        }