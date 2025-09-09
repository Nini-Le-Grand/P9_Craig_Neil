# Medilabo Application

Ce projet est une architecture **microservices** pour l’application Medilabo, comprenant :  

- **Frontend React** servi par Nginx  
- **Gateway** pour l’API  
- **Microservices backend** : user-ms, note-ms, evaluation-ms  
- **Bases de données** : MySQL et MongoDB  

L’ensemble est orchestré via **Docker Compose**.


## Se connecter à l'application
Placez-vous à la racine du projet

docker-compose up --build -d

Si vous n'avez pas modifié le .env, l'application est disponible à l'url http://localhost:3000/

### Utilisteur :
- email : user1@test.com
- mdp : Valid123!

### Admin :
- email : user2@test.com
- mdp : Valid123!


## Prérequis

- [Docker](https://docs.docker.com/get-docker/) >= 20.10  
- [Docker Compose](https://docs.docker.com/compose/install/) >= 1.29  


## Structure du projet
medilabo/
├─ backend/
│ ├─ gateway/
│ └─ services/
│   ├─ user-ms/
│   ├─ note-ms/
│   └─ evaluation-ms/
├─ frontend/
│ ├─ Dockerfile
│ └─ src/
├─ docker-compose.yml
└─ README.md


## Configuration

Créer un fichier `.env` à la racine du projet pour les secrets et ports :
