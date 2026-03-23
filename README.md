# 🧠 ThesisCore

> A framework for symbolic systems, knowledge interpretation, and emergent gameplay mechanics.

---

## 📖 Overview

**ThesisCore** is a modular framework designed to support systems where knowledge is:

- **Discovered, not given**
- **Interpreted, not guaranteed**
- **Potentially incorrect**

It provides foundational building blocks for:
- Symbolic interaction systems
- Fragment-based knowledge models
- Interpretation engines
- Instability and consequence systems

ThesisCore is designed to power systems like those in **Grimarcana**, but is intentionally built to be reusable and system-agnostic.

---

## 🎯 Design Goals

- Enable **symbol-driven gameplay systems**
- Support **imperfect player knowledge**
- Separate **system truth** from **player belief**
- Encourage **emergent behavior**
- Remain **data-driven and extensible**

---

## 🧠 Core Concepts

---

### 🔣 Symbol

Represents a core concept (e.g., Heat, Stability, Chaos).

- Symbols are **abstract**, not tied to items
- They interact with other symbols via rules
- They are the foundation of all interpretation logic

```java
public interface Symbol {
    String getId();
    Set<String> getTags();
}
