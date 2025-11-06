# 📚 API Documentation - Authentification

## Base URL
```
http://localhost:8080
```

## Endpoints disponibles

### 1. Inscription (Register)

**POST** `/api/auth/register`

**Description :** Créer un nouveau compte utilisateur

**Headers :**
```
Content-Type: application/json
```

**Request Body :**
```json
{
  "firstname": "Jean",
  "lastname": "Dupont",
  "email": "jean.dupont@example.com",
  "password": "motdepasse123",
  "address": "123 Rue de Paris, Nantes"
}
```

**Response Success (200 OK) :**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "jean.dupont@example.com",
  "role": "USER"
}
```

**Erreurs possibles :**
- **400 Bad Request** : Données manquantes ou invalides
- **500 Internal Server Error** : "Cet email est déjà utilisé"

---

### 2. Connexion (Login)

**POST** `/api/auth/login`

**Description :** Se connecter avec un compte existant

**Headers :**
```
Content-Type: application/json
```

**Request Body :**
```json
{
  "email": "jean.dupont@example.com",
  "password": "motdepasse123"
}
```

**Response Success (200 OK) :**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "jean.dupont@example.com",
  "role": "USER"
}
```

**Erreurs possibles :**
- **400 Bad Request** : Données manquantes
- **500 Internal Server Error** : "Email ou mot de passe incorrect"

---

## 🔐 Utilisation du Token JWT

Pour les futures requêtes authentifiées, ajoutez le token dans le header :
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

## 🧪 Tests avec Postman

### Scénario 1 : Inscription puis connexion
1. POST `/api/auth/register` avec les données d'un nouvel utilisateur
2. Copier le token reçu
3. POST `/api/auth/login` avec le même email/password
4. Vérifier qu'on reçoit un nouveau token

### Scénario 2 : Tentative de double inscription
1. POST `/api/auth/register` avec un email
2. Re-POST `/api/auth/register` avec le même email
3. Doit retourner une erreur "Cet email est déjà utilisé"

### Scénario 3 : Mauvais mot de passe
1. POST `/api/auth/login` avec un mauvais password
2. Doit retourner "Email ou mot de passe incorrect"