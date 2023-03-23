FROM node:16.17.1-alpine

WORKDIR '/usr/src/app/frontend'

COPY package*.json ./

RUN npm install

EXPOSE 3050

CMD ["npm run dev"]
