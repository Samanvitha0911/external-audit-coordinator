import chromadb

class ChromaClient:
    def __init__(self):
        self.client = chromadb.Client()

        self.collection = self.client.get_or_create_collection(
            name="audit_collection"
        )

    def add_documents(self, docs):
        self.collection.add(
            documents=docs,
            ids=[str(i) for i in range(len(docs))]
        )

    def query(self, text):
        results = self.collection.query(
            query_texts=[text],
            n_results=1
        )
        return results["documents"]