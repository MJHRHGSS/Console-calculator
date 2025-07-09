# Strilculate

Strilculate is a console-based calculator written in Java. It was created to learn the basics of tokenizing and parsing expressions.

If you want to check out the logic behind it or add new features, here are some instructions you might find useful:

## Requirements

- Java Development Kit (JDK) 17+

## How it works

It does things in these simple steps:

1. Takes in a `String` input
2. Tokenizes it into `Object` tokens in a list
3. Parses the tokens and calculates the outcome

## Installation

Clone the repo:

```bash
git clone https://github.com/MJHRHGSS/Console-calculator.git
```

## Run

### Pre-written scripts:

Run the script located at `~/Console-calculator/run.sh` on Linux or Mac, or `~/Console-calculator/run.bat` on Windows.

### Manual

To run manually, run this:

```bash
javac -d out src/com/mjhrhgss/strilculate/*.java # Compile
java -cp out com.mjhrhgss.strilculate.Main # Run
```

All the information you'll need to use it is available once you run it.

## Contributions

This project is mainly for learning purposes, but contributions are always welcome!

---

> **NOTE**: I made the code not thinking that I would make it public, so I never really thought about making it readable.
