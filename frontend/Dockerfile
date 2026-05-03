# ══════════════════════════════════════════════
#  Frontend Dockerfile — React 18 / Vite / Nginx
#  Multi-stage: build static files, serve via Nginx
# ══════════════════════════════════════════════

# ── Stage 1: Build ─────────────────────────────
FROM node:18-alpine AS builder
WORKDIR /app

# Cache dependencies
COPY package.json package-lock.json* ./
RUN npm install

# Build static files
COPY . .
RUN npm run build

# ── Stage 2: Serve ─────────────────────────────
FROM nginx:alpine
# Copy built files
COPY --from=builder /app/dist /usr/share/nginx/html
# Copy custom Nginx configuration for React Router
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
