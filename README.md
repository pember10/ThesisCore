# 🧠 ThesisCore

> A framework for symbolic systems, knowledge interpretation, and emergent gameplay mechanics.

---

## 📖 Overview

**ThesisCore** is a modular framework designed to support systems where knowledge is:

- **Discovered, not given**
- **Interpreted, not guaranteed**
- **Potentially incorrect**

It provides foundational building blocks for:
- Symbolic registries with custom registry systems
- Fragment-based knowledge discovery
- Player belief tracking and evaluation
- Instability accumulation and consequence resolution

ThesisCore is designed to power systems like those in **Grimarcana**, but is intentionally built to be reusable and system-agnostic.

---

## 🎯 Design Goals

- Enable **symbol-driven gameplay systems**
- Support **imperfect player knowledge** with entrenchment mechanics
- Separate **system truth** from **player belief** clearly
- Encourage **emergent consequences** through instability
- Remain **data-driven and extensible** via registries and codecs

---

## 🏗️ Architecture

### Module Structure

**ThesisCore** is organized around four core systems:

#### 1. **Symbol System** (`symbol/`)
- `Symbol` — core interface for symbolic concepts
- `SimpleSymbol` — concrete implementation with ID and tags
- Symbols are registered via NeoForge's `DeferredRegister`

#### 2. **Knowledge System** (`knowledge/`)
- `KnowledgeFragment` — discoverable hints about symbols (can be misleading)
- `SymbolBelief` — player's mental model of a symbol with confidence and entrenchment
- `PlayerKnowledgeState` — belief store and evaluation logic
- `InterpretationResult` — severity enum (CORRECT → CATASTROPHIC_FAILURE)

#### 3. **Instability System** (`instability/`)
- `PlayerInstability` — tracks accumulated magical instability (0–100)
- `InstabilityOutcome` — consequence severity (PERFECT → CATASTROPHIC)
- `InstabilitySystem` — stateless logic for gain calculation and outcome resolution
- XP-based stabilization (pre-cast and reactive)

#### 4. **Registry System** (`registry/`)
- `SymbolRegistry` — custom registry for Symbol objects
- `ThesisCoreRegistries` — registry manager
- `ThesisCoreAttachments` — NeoForge attachments for player knowledge and instability

---

## 🧠 Core Concepts

### 🔣 Symbol

A symbolic concept (e.g., Heat, Stability, Chaos).

- Symbols are **abstract** and **data-driven**
- Each symbol has a **unique ID** and a **set of tags** describing its nature
- Tags inform interpretation logic and player learning

```java
Symbol heat = new SimpleSymbol(
    ResourceLocation.of("grimarcana", "heat"),
    Set.of("energy", "destruction", "transformation")
);
```

### 📜 Knowledge Fragment

A discoverable hint about the symbolic world, intentionally **vague and potentially misleading**.

- Fragments have a **description key** for localization
- Each fragment points to one or more related symbols
- Fragments carry a **signal strength** (0.0–1.0) indicating clarity
- Fragments can be flagged as **misleading** — hidden from the player

The player interprets fragments to build beliefs. A misleading fragment points to the wrong symbols, creating false confidence.

### 🧠 Player Belief

The player's mental model of a single symbol.

- Tracks **believed tags**, **confidence**, and **entrenchment**
- **Confidence** (0.0–1.0) represents how sure the player is
- **Entrenched** beliefs resist correction and dampen new evidence by 50%
- Beliefs can be corrected if evidence is strong enough

```java
SymbolBelief belief = SymbolBelief.unknown(heatSymbolId);
belief.applyEvidence(Set.of("energy"), 0.4f); // Weak evidence
belief.entrench(); // Now resists weak corrections
```

### 📊 Interpretation

The system compares player belief to ground truth and produces an `InterpretationResult`:

- **CORRECT** — belief matches truth exactly
- **PARTIAL** — player understands some aspects
- **INCORRECT** — confident but significantly wrong (dangerous!)
- **FAILURE** — insufficient knowledge
- **CATASTROPHIC_FAILURE** — deeply confident and deeply wrong

```java
Symbol truth = heat;
InterpretationResult result = playerKnowledge.evaluate(truth);
// If player believed heat was "cold", result = INCORRECT
```

### ⚡ Instability

Magical actions generate instability based on interpretation quality.

- **Good interpretation** (CORRECT) → no instability
- **Poor interpretation** (INCORRECT/CATASTROPHIC_FAILURE) → high instability
- Instability accumulates up to a hard cap (100)
- High instability makes all future actions produce worse outcomes
- Players can spend XP to stabilize (pre-cast more efficient than reactive)

```java
InstabilityOutcome outcome = InstabilitySystem.applyAction(
    player, 
    InterpretationResult.INCORRECT, 
    1.0f /* action power */
);
// Outcome ranges from PERFECT (low instability) to CATASTROPHIC (very high)
```

---

## 🧪 Testing

ThesisCore includes **35+ unit tests** covering:

- Symbol and belief mechanics
- Interpretation evaluation logic
- Instability gain and outcome resolution
- Edge cases (entrenchment, correction resistance, clamping)

Run tests locally:
```bash
./gradlew :thesiscore:test
```

All tests use JUnit 5. ResourceLocation construction is handled via reflection to work around private constructors in the Minecraft version.

---

## 🚀 Getting Started (for Mod Developers)

### 1. Register Symbols

Create symbols using `SimpleSymbol` and register via `DeferredRegister`:

```java
public static final DeferredRegister<Symbol> SYMBOLS = DeferredRegister.create(
    ThesisCoreRegistries.SYMBOL_REGISTRY_KEY, 
    "yourmodid"
);

public static final Supplier<Symbol> HEAT = SYMBOLS.register(
    "heat", 
    () -> new SimpleSymbol(/* id */, Set.of("energy", "destruction"))
);
```

### 2. Create Knowledge Fragments

Build discoverable hints that point to symbols:

```java
KnowledgeFragment fragment = new KnowledgeFragment(
    ResourceLocation.of("yourmodid", "flame_hint"),
    "fragment.flame_hint", // translation key
    Set.of(HEAT.getId()),
    0.7f, // signal strength
    false // not misleading
);
```

### 3. Award Fragments to Players

When players discover something, apply evidence to their beliefs:

```java
PlayerKnowledgeState knowledge = player.getData(ThesisCoreAttachments.PLAYER_KNOWLEDGE);
knowledge.applyEvidence(
    HEAT.getId(), 
    Set.of("energy", "destruction"), 
    0.5f // confidence
);
```

### 4. Evaluate Actions

Check interpretation before resolving an action:

```java
InterpretationResult result = knowledge.evaluate(HEAT);
InstabilityOutcome outcome = InstabilitySystem.resolveOutcome(
    player.getData(ThesisCoreAttachments.PLAYER_INSTABILITY),
    result
);
```

---

## 📦 Dependency

Add ThesisCore as a dependency in your mod's `build.gradle`:

```gradle
dependencies {
    implementation project(':thesiscore')
}
```

When released, mods will depend on the published JAR artifact, and players must install ThesisCore alongside.

---

## 🔧 Project Structure

```
ThesisCore/
├── thesiscore/              # Core framework
│   ├── src/main/java/       # Symbol, knowledge, instability, registry systems
│   ├── src/test/java/       # 35+ unit tests
│   └── build.gradle
├── grimarcana/              # Example mod using ThesisCore
│   ├── src/main/java/       # Spell system, potion mechanics, dimension
│   └── build.gradle
├── .github/workflows/build.yml  # CI/CD: build + test on push/PR
└── README.md                # This file
```

---

## 📋 License

See [TEMPLATE_LICENSE.txt](TEMPLATE_LICENSE.txt) for details.

---

## 🤝 Contributing

Contributions are welcome! Please ensure:
- New code passes the existing test suite
- New tests are added for logic changes
- The framework remains system-agnostic

---

## 🔮 Future Directions

- Symbol interaction rules (e.g., Heat + Water → Steam)
- Multi-stage knowledge discovery chains
- Player-to-player belief sharing and conflict
- Instability consequences (visual effects, temporary debuffs)
- Integration tests with GameTest framework
