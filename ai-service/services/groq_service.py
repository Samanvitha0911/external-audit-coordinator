import os
from groq import Groq

# Initialize the Groq client
# Ensure GROQ_API_KEY is set in your .env file
client = Groq(
    api_key=os.environ.get("GROQ_API_KEY"),
)

def get_groq_response(prompt: str, context: str = "") -> str:
    """
    Calls the Groq API using LLaMA-3.3-70b.
    """
    system_prompt = "You are a helpful and professional AI assistant for an external audit coordinator system."
    
    messages = [
        {"role": "system", "content": system_prompt}
    ]
    
    if context:
        messages.append({"role": "user", "content": f"Context:\n{context}"})
        
    messages.append({"role": "user", "content": prompt})
    
    try:
        chat_completion = client.chat.completions.create(
            messages=messages,
            model="llama-3.3-70b-versatile",
            temperature=0.3,
            max_tokens=1024,
        )
        return chat_completion.choices[0].message.content
    except Exception as e:
        print(f"Error calling Groq API: {str(e)}")
        import json
        return json.dumps({
            "is_fallback": True,
            "message": "The AI service is currently unavailable. Please try again later.",
            "error": str(e)
        })
