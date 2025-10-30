export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/products',
  // Fallback endpoints for development
  fallbackUrls: [
    'http://127.0.0.1:8080/api/products',
    'http://localhost:3000/api/products'
  ]
};
