# Application Alphabet Arabe pour Enfants

Application Android simple pour apprendre l'alphabet arabe, destinée aux débutants en programmation.

## Fonctionnalités

- **Liste des lettres** : Affichage de toutes les lettres arabes dans une grille
- **Son des lettres** : Lecture audio quand l'enfant clique sur une lettre
- **Tracé interactif** : Zone de dessin pour tracer les lettres avec le doigt
- **Interface enfant** : Design coloré avec de gros boutons adaptés

## Structure du Code

### Classes Principales

- `MainActivity.kt` : Écran principal avec la liste des lettres
- `DrawingActivity.kt` : Écran de tracé d'une lettre
- `ArabicLetter.kt` : Modèle de données pour une lettre
- `ArabicAlphabet.kt` : Liste complète des 28 lettres arabes
- `LetterAdapter.kt` : Adaptateur RecyclerView pour afficher les lettres
- `DrawingView.kt` : Vue Canvas personnalisée pour le tracé

### Concepts Android Utilisés

- **Activities** : Navigation entre écrans
- **RecyclerView** : Liste scrollable des lettres
- **Canvas** : Dessin tactile personnalisé
- **MediaPlayer** : Lecture de sons
- **Intents** : Passage de données entre activités

## Installation

1. Ouvrir le projet dans Android Studio
2. Synchroniser les dépendances Gradle
3. Lancer sur un appareil ou émulateur Android

## Améliorations Possibles

- Ajouter de vrais fichiers audio pour chaque lettre
- Système de progression et récompenses
- Mode d'apprentissage guidé
- Sauvegarde des dessins