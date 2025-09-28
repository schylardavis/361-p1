## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


## Authors

- Schylar Davis and Daniel Aguilar

## Description

This project implements a *Deterministic Finite Automation (DFA) in Java.

DFA.java implements the DFA behavior which includes methods from "DFAInterface" and "FAInterface".
DFAState.java extends the abstract state class and represents a single state in the DFA
Unit test cases in "DFATest.java verifies the correctness of the implementation

## Features

- Add states, start state, and final states.  
- Define the DFA’s alphabet (Σ).  
- Add transitions between states.  
- Process strings to determine if they are accepted or rejected.  
- Print the DFA using toString() in a specific format.  
- Create a swapped DFA where transition labels are exchanged (swap method).  

## Compilation and Running

## Compile

On onyx: javac -cp .:/usr/share/java/junit.jar ./test/dfa/DFATest.java

## Run

On Onyx: java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar
org.junit.runner.JUnitCore test.dfa.DFATest