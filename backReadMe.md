# 🚀 Backend - Guide de démarrage

## 📦 Ce qui a été fait (Sprint 1)

### ✅ Authentification JWT complète
- Inscription (`POST /api/auth/register`)
- Connexion (`POST /api/auth/login`)
- Gestion d'erreurs avec codes HTTP appropriés
- Hashage des mots de passe avec BCrypt

## 🏗️ Architecture
```
src/main/java/com/meubles/
├── Controller/
│   └── AuthController.java
├── Service/
│   ├── AuthService.java
│   └── CustomUserDetailsService.java
├── Repository/
│   └── UserRepository.java
├── Entity/
│   └── UserEntity.java
├── DTO/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   └── ErrorResponse.java
├── Security/
│   ├── JwtUtil.java
│   └── SecurityConfig.java
├── exception/
│   ├── EmailAlreadyExistsException.java
│   ├── InvalidCredentialsException.java
│   └── GlobalExceptionHandler.java
└── Model/
    └── Role.java (enum)
```

## ⚙️ Configuration

### Base de données (PostgreSQL)
Fichier `application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nom_bdd
spring.datasource.username=votre_user
spring.datasource.password=votre_password

jwt.secret=VotreCleSecreteSuperLongue...
jwt.expiration=86400000
```

## 🧪 Comment tester

Voir le fichier `API_DOCUMENTATION.md`

## 📝 Pour le Sprint 2 (Produits)

À créer :
- Entity Product, Category, Couleur, Matiere
- Repositories correspondants
- ProductService
- ProductController
- Endpoints GET /api/products et GET /api/products/{id}

## ⚠️ Points d'attention

- Spring Security configuré en mode permissif (`.anyRequest().permitAll()`)
  → À revoir au Sprint 3 pour protéger les routes
- Pas de validation des données (@Valid) pour l'instant
- Pas de gestion de refresh token

