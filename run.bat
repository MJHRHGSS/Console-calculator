@echo off
cd /d %~dp0
javac -d out src/com/mjhrhgss/strilculate/*.java
java -cp out com.mjhrhgss.strilculate.Main
