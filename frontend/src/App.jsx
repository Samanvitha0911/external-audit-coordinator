import React from 'react';

function App() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full text-center">
        <h1 className="text-3xl font-bold text-blue-600 mb-4">External Audit Coordinator</h1>
        <p className="text-gray-600 mb-6">
          Welcome to the External Audit Coordinator tool. The frontend scaffold is ready.
        </p>
        <div className="grid gap-4">
            <div className="bg-blue-50 text-blue-800 p-3 rounded-md border border-blue-200">
                Backend API: {import.meta.env.VITE_API_BASE_URL || 'Not configured'}
            </div>
             <div className="bg-purple-50 text-purple-800 p-3 rounded-md border border-purple-200">
                AI API: {import.meta.env.VITE_AI_BASE_URL || 'Not configured'}
            </div>
        </div>
      </div>
    </div>
  );
}

export default App;
