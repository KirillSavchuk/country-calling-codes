FROM node:alpine
ENV NODE_OPTIONS="--openssl-legacy-provider"
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
EXPOSE 8080
CMD ["npm", "run", "start"]