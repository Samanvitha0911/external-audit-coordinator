import chromadb


class ChromaClient:
    def __init__(self):
        # Persistent storage folder
        self.client = chromadb.PersistentClient(path="./chroma_db")

        self.collection = self.client.get_or_create_collection(
            name="audit_collection"
        )

    def add_documents(self, docs):
        self.collection.add(
            documents=docs,
            ids=[str(i) for i in range(len(docs))]
        )

    def query(self, text, n_results=3):
        results = self.collection.query(
            query_texts=[text],
            n_results=n_results
        )

        documents = results.get("documents", [[]])

        if documents and len(documents[0]) > 0:
            return documents[0]

        return []