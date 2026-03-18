# Pacman Game (Java)

## Overview
This project is a Java-based implementation of the classic Pacman game, designed with a strong focus on object-oriented design and software engineering principles.

The system leverages multiple design patterns to create a modular, extensible, and maintainable architecture. This makes it easy to extend gameplay features such as ghost behaviour, collectibles, and overall game logic.

---

## How to Run

Ensure you have Java and Gradle installed.

```bash
gradle clean build run

## Design Patterns

### Decorator Pattern
**Location:** `java/pacman/model/entity/staticentity/collectable`

The Decorator pattern is used to dynamically extend the behaviour of collectable entities without modifying their base implementation.

- Allows adding new functionality (e.g. score boosts, power-ups) at runtime  
- Promotes flexibility by composing behaviour instead of using inheritance  
- Supports the Open/Closed Principle  

---

### State Pattern
**Location:** `java/pacman/model/GhostState`

The State pattern is used to manage ghost behaviour based on their current state.

- Encapsulates behaviour for different states such as:
  - Chase
  - Scatter
  - Frightened  
- Eliminates large conditional statements  
- Enables clean and scalable state transitions  

---

### Strategy Pattern
**Location:** `java/pacman/model/Strategy`

The Strategy pattern is used to define different movement and decision-making algorithms for ghosts.

- Encapsulates different AI behaviours as interchangeable strategies  
- Allows switching behaviour dynamically at runtime  
- Improves modularity and testability of ghost logic  
