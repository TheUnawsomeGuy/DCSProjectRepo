# 🎯 Distributed Systems Consistency Models Demo

# AI Generated Readme
---
Due to lack of time. Will be replaced later at some point

---

This project demonstrates **three major consistency models** in distributed systems. It's as challenging as Sans's final boss fight, but way more educational! 💀

## 🗂️ File Structure & What Each Code Does

### 📋 `Operation.java` - The Building Block
**What it does:** Creates a "recipe card" for every action in our system
- Stores information about each operation (PUT/DELETE)
- Gives each operation a unique sequence number
- Tracks who did it and when
- Think of it as a "transaction receipt" that shows exactly what happened

### 🏢 `CentralCoordinator.java` - The Boss
**What it does:** Acts like a traffic controller for all operations
- Maintains a central log of ALL operations
- Ensures everyone follows the same order (Sequential Consistency)
- Gives out sequence numbers to keep things organized
- Sends operations to all replica nodes in the exact same order

### 🖥️ `ReplicaNode.java` - The Workers
**What it does:** Individual computers/servers that store copies of data
- Each node has its own data storage (like a mini-database)
- Can apply operations sent by the coordinator
- Can sync up with the coordinator when they fall behind
- Simulates network delays to make it realistic

### 👤 `ConsistencyClient.java` - The Smart User
**What it does:** A client that remembers what it has done (Client-Centric Consistency)
- Tracks versions of data it has seen
- Ensures you can always read what you just wrote
- Finds the right replica that has up-to-date information
- Makes sure you never see "old" data after seeing "new" data

### ⚡ `EventualConsistencySimulator.java` - The Chaos Creator
**What it does:** Simulates real-world network problems
- Creates background sync processes
- Simulates network partitions (when nodes can't talk to each other)
- Shows how nodes eventually become consistent after problems
- Makes the demo more realistic with random delays

### 🎮 `ConsistencyDemo.java` - The Main Show
**What it does:** Runs demonstrations of all three consistency models
- Sets up the entire system with coordinator and replicas
- Demonstrates Sequential Consistency with ordered operations
- Shows Eventual Consistency with network partitions
- Proves Client-Centric Consistency with version tracking

---

## 🚀 How to Run

```bash
javac Consistency/*.java
java Consistency.ConsistencyDemo
```

The demo will show you:
1. **Sequential Consistency** - Everyone sees the same order
2. **Eventual Consistency** - Things sync up eventually (even with network issues)
3. **Client-Centric Consistency** - Each client gets a consistent personal view

---

## 🎯 What You'll Learn

- **Sequential Consistency**: Like everyone watching the same movie in the same order
- **Eventual Consistency**: Like gossip that eventually reaches everyone, even if some people hear it late
- **Client-Centric Consistency**: Like having a personal diary that never forgets what you wrote

---

## 🧒 ELI5 (Explain Like I'm 5)

### What is this about?
Imagine you have 3 toy boxes (replicas) and you want to keep the same toys in all of them!

### Sequential Consistency 🎯
**Like a teacher giving instructions to the class:**
- Teacher says "Put the red ball in box 1, then put the blue car in box 2"
- ALL kids do these actions in the EXACT same order
- Every toy box ends up exactly the same!

### Eventual Consistency ⏰
**Like cleaning up toys after playtime:**
- Some kids clean up fast, some kids clean up slow
- Sometimes a kid goes to the bathroom and misses some instructions
- But EVENTUALLY, all toy boxes have the same toys (even if it takes a while!)

### Client-Centric Consistency 📝
**Like remembering what you put in your own toy box:**
- You put a teddy bear in your box
- You can ALWAYS find that teddy bear when you look
- You never forget what toys you put in yourself
- But you might not see what other kids put in their boxes right away

---

## 🤓 Simpler Technical Explanation

Think of it like a **Google Docs** shared document:

### Sequential Consistency
Everyone sees edits in the exact same order. If Alice types "Hello" and Bob types "World", everyone sees either "HelloWorld" or "WorldHello", but never a mix.

### Eventual Consistency
Sometimes your internet is slow, so you see changes later than others. But eventually, everyone's document looks the same.

### Client-Centric Consistency
YOU always see the changes YOU made, even if the internet is slow. You never lose your own work, even if you can't see what others did immediately.

---

## 💡 Real-World Examples

- **Banks** use Sequential Consistency (money transfers must be in order!)
- **Social Media** uses Eventual Consistency (your posts appear everywhere eventually)
- **Email** uses Client-Centric Consistency (you always see emails you sent)

---

## 🏆 Achievement Unlocked
If you understand this code, you've mastered one of the hardest concepts in computer science! You're now ready to build the next Facebook, Google, or Amazon! 🌟

*"Congratulations, kid. You beat Sans's distributed systems course."* 💀⚡ 

