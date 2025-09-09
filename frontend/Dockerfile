# frontend/Dockerfile
FROM node:20 AS build
WORKDIR /app

ARG REACT_APP_API_URL
ARG REACT_APP_USER_MS
ARG REACT_APP_NOTE_MS
ARG REACT_APP_EVALUATION_MS

COPY package*.json ./
RUN npm install
COPY . .

ENV REACT_APP_API_URL=$REACT_APP_API_URL
ENV REACT_APP_USER_MS=$REACT_APP_USER_MS
ENV REACT_APP_NOTE_MS=$REACT_APP_NOTE_MS
ENV REACT_APP_EVALUATION_MS=$REACT_APP_EVALUATION_MS

RUN npm run build

# Serve build
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
