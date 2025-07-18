# TourGuide

## Contexte

*Ce projet est le 8ème de la formation Développement d'applications Java d'OpenClassrooms.*
TourGuide est une application Spring Boot pour faciliter la planification de voyages.
Elle permet de voir quelles sont les attractions touristiques à proximité et d’obtenir des réductions sur les hôtels et les spectacles.

Le projet fourni, entre autre, une base de code sur laquelle il faut :
- corriger des bugs,
- résoudre des problèmes de performances associées à une montée en charge
- mettre en place un pipeline d'intégration continue.

## Technologies utilisées

- Back-end : Java 17, Spring Boot, Junit
- Gestion des dépendances : Maven

## Configuration
Le projet simule l'utilisation d'API externe avec des dépendances locales que vous devez installer pour pouvoir compiler 
**1. Compilation et lancement**

> Run : 
- mvn install:install-file -Dfile=/libs/gpsUtil.jar -DgroupId=gpsUtil -DartifactId=gpsUtil -Dversion=1.0.0 -Dpackaging=jar  
- mvn install:install-file -Dfile=/libs/RewardCentral.jar -DgroupId=rewardCentral -DartifactId=rewardCentral -Dversion=1.0.0 -Dpackaging=jar  
- mvn install:install-file -Dfile=/libs/TripPricer.jar -DgroupId=tripPricer -DartifactId=tripPricer -Dversion=1.0.0 -Dpackaging=jar

## CI/CD avec GitHub Actions

### 1️⃣ Intégration Continue (CI)
- **Déclenchement** : à chaque `push` ou `pull request` sur `master`
- **Étapes réalisées** :
    - Compilation avec Maven
    - Tests unitaires (sauf tests de performance)
    - Génération de l'artefact `.jar`

### 2️⃣ Tests de Performance
- **Déclenchement** : manuel via l'onglet **Actions** de GitHub (workflow_dispatch)
- **Tests lancés avec différentes charges** grâce à la matrice GitHub Actions :
    - 100 utilisateurs
    - 500 utilisateurs
    - 1 000 utilisateurs
    - 100 000 utilisateurs

## author
Astri Brugeilles - 2025
Formation Développeur d'Application Java - OpenClassrooms