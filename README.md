# PayMyBuddy

## 📜 Description
PayMyBuddy est une application qui permet d'effectuer des virements d'argent entre amis ou de recevoir des paiements en quelques clics, à l'image de services comme PayPal ou WERO. Elle répond au besoin de simplifier le remboursement entre particuliers sans nécessiter de coordonnées bancaires complexes, contrairement aux services bancaires traditionnels.

## 🚀 Fonctionnalités
- **Inscription** : Créez un compte avec une adresse e-mail et un mot de passe.
- **Connexion** : Accédez à votre compte sécurisé.
- **Dépôt d'argent** : Ajoutez des fonds à votre compte PayMyBuddy.
- **Retrait d'argent** : Transférez vos fonds vers votre compte bancaire.
- **Ajout de relations** : Enregistrez vos amis via leur adresse e-mail.
- **Transfert d'argent** : Envoyez de l'argent à vos amis enregistrés.
- **Page profil** : Consultez et modifiez vos informations personnelles.

## 🛠️ Technologies Utilisées
- **Java 21**
- **Spring Boot**
- **Hibernate JPA**
- **MySQL 8**
- **Thymeleaf**
- **Spring Security 6**

### Prérequis
- **Java 21**
- **Maven**
- **MySQL 8**
- **Spring Boot**

## ⚙️ Installation
1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/KentinTL/PayMyBuddy.git
   cd PayMyBuddy
   ```
2. **Configurer la base de données** :
    - Créez une base de données MySQL nommée `paymybuddy`.
    - Mettez à jour le fichier `application.properties` avec vos informations MySQL.

3. **Compiler et lancer l'application** :
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Accéder à l'application** :
    - URL : `http://localhost:8080`

## 🔒 Sécurité
- Utilisation de **Spring Security 6** pour l'authentification et l'autorisation.
- Encodage des mots de passe avec **BCrypt**.

## 🧪 Tests
Aucun test unitaire ou d'intégration n'a été effectué pour cette application.

## 🤝 Contribuer
Ce projet a été réalisé dans le cadre d'une formation. Les contributions externes ne sont pas nécessaires.

## 🏅 Licence
Aucune licence spécifique n'est appliquée à ce projet.