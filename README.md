## JSweet407
JSweet407 is a Java-to-JS transpiler implementation that builds upon Jsweet, adding support for Java concurrency features. 

### Key Components
JSweet407 consists of two main components:
1. **Transpiler Extension**
2. **JS Multithreading Runtime**

### Supported Features
- `Thread` class  
- `Runnable` interface  
- `synchronized` keyword  
- Thread methods from the standard library, such as:
  - `join`
  - `sleep`

### Lock Classes
The current design architecture also allows for the easy implementation of various lock classes from the Java standard library. Examples implemented in the project include:
- `ReentrantLock`
- `ReadWriteLock`
- `StampedLock`


## Documentation
The file `technical-report.pdf` in the root directory provides a detailed explanation of the project's design, architecture, implementation, and limitations, along with relevant data visualizations.

## Demo Video

The `example.mkv` video file in the root directory demonstrates how to run this project using the compiled JAR package.

## System Requirements
- JDK 16+
- TSC (TypeScript Compiler)

## Usage Instructions

We provide a bash script `run.sh` to compile files and view the results.  
- The script uses the precompiled JAR package in the `artifacts` directory to compile files from the `example` folder.  
- **Note:** Only one file should be placed in the `example` folder at a time to avoid potential conflicts.  
- During execution, `run.sh` stores intermediate compilation results in the `tsout` directory and copies the final compiled JavaScript code to `runtime/src/compiled.js`.  

Users can view the execution results by accessing `runtime/src/test.html` through an HTTP server. For detailed steps, refer to the demo video `example.mkv`.

## Testing Instructions
All test cases are located in the `benchmark` folder in the project root directory.

## Authors
- Wenzhang Yang
- Junjie Zhong
- Lianzuo Wang
- Shiping Zhang
- Yuhang Ye
- Ao Chen
