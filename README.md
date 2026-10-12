# Cache-Efficient k-NN Classifier (Inspired by HR Data Analytics)

## Overview
During my service as an HR Data Analyst in the IDF, I managed and analyzed hundreds of thousands of personnel records. While SQL and BI tools are excellent for business logic, I realized that Machine Learning algorithms could uncover hidden patterns in placement success rates and dropout risks that standard queries miss.

Inspired by this domain, I built this Java implementation of a **k-Nearest Neighbors (k-NN) classification pipeline from scratch**, coupling it with a highly optimized, cache-efficient memory representation designed to handle large-scale datasets rapidly.

This project bridges practical data analysis experience with core computer science algorithmic design, focusing heavily on runtime efficiency, memory optimization, and hardware-aware programming.

---

## Core Features & Optimizations

### 1. Performance & Memory Optimization (`FlatMatrix`)
When processing massive datasets, standard 2D arrays suffer from cache misses due to fragmented pointer-based memory allocation. 

* **Contiguous Memory Buffers:** Re-implemented standard 2D arrays as contiguous 1D memory buffers in row-major order.
* **Spatial Locality:** Significantly enhances CPU cache utilization, reducing memory access overhead for intensive linear algebra operations and distance calculations.

> ** Benchmark Result:** Running a matrix multiplication test ($800 \times 800$ elements) proves that the contiguous `FlatMatrix` implementation is **~2.5x faster** than standard Java 2D arrays (`double[][]`) purely due to CPU spatial cache locality. *(See `PerformanceBenchmark.java`)*

```mermaid
graph LR
    subgraph Sub1 [Standard 2D Array - Cache Misses]
        R0[Row 0] -.-> M1[Memory Addr 100]
        R1[Row 1] -.-> M2[Memory Addr 500]
        R2[Row 2] -.-> M3[Memory Addr 250]
    end

    subgraph Sub2 [FlatMatrix - Cache Friendly]
        F1[Row 0, Row 1, Row 2] ===> M4[Contiguous Memory Block]
    end
```

### 2. Machine Learning Pipeline (k-NN Classifier)
* **Vectorized Distance Computation:** Computes squared distance matrices efficiently across sample batches.
* **Data Normalization:** Features a built-in Z-score normalization mechanism to standardize multi-dimensional features (e.g., psychometric scores vs. physical distances) so no single scale dominates the model.
* **Optimized Neighbor Selection:** Utilizes a custom `IndexComparator` and sorting routines to rapidly identify nearest neighbors and perform majority voting without losing track of original sample indices.

```mermaid
graph TD
    A[(mock_hr_data.csv)] -->|File I/O| B[Load Features and Labels]
    B -->|Initialization| C[FlatMatrix]
    C -->|MatrixOperations| D[Z-Score Normalization]
    D -->|Linear Algebra| E[Calculate Squared Distances]
    E -->|Custom Sorting| F[IndexComparator]
    F -->|k=3| G[Majority Voting]
    G --> H{Prediction Result}
    H -->|High Risk| I[Recommend Secondary Placement]
    H -->|Low Risk| J[Proceed to Regular Placement]
```

---

## Technologies & Principles

* **Language:** Java
* **Core Concepts:** 
  * Cache Locality & Hardware-Aware Design
  * Dynamic 1D-to-2D Index Mapping
  * Matrix Operations & Linear Algebra
  * Custom Sorting & Comparators
  * Object-Oriented Design (Clean Interfaces & Polymorphism)

---

## Project Structure

```text
├── Matrix.java              # Core matrix interface defining arithmetic and manipulation methods
├── FlatMatrix.java          # High-performance 1D array-based matrix implementation
├── MatrixOperations.java    # Linear algebra computations (Z-score normalization, squared distances)
├── IndexComparator.java     # Custom comparator for sorting instances by distance indices
├── DemoHRClassifier.java    # End-to-end classification demo using simulated HR data
└── PerformanceBenchmark.java # CPU cache locality benchmark for matrix multiplication

```
