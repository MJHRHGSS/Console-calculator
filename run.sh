#!/bin/bash
cd "$(dirname "$(readlink -f "$0")")"
javac -d out src/com/mjhrhgss/strilculate/*.java
java -cp out com.mjhrhgss.strilculate.Main
