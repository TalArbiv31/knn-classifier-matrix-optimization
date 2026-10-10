# k-NN Classifier & High-Performance Matrix Optimization

A Java implementation of a **k-Nearest Neighbors (k-NN)** classification pipeline from scratch, coupled with a cache-efficient **Flat Matrix** data representation.

## Overview
This project focuses on two core components:
1. **Machine Learning Pipeline (k-NN Classifier):**
   - Implemented an end-to-end k-NN classifier from scratch.
   - Computes vectorized squared distance matrices ($D^2$) and normalizes multi-dimensional feature data (Z-score normalization).
   - Utilizes custom comparators (`IndexComparator`) and selection sorting to identify nearest neighbors and perform majority voting.
   
2. **Performance & Memory Optimization (`FlatMatrix`):**
   - Re-implemented standard 2D arrays as contiguous 1D memory buffers in row-major order.
   - Significantly enhances **spatial locality** and **CPU cache utilization**, reducing memory access overhead for intensive linear algebra operations.

## Technologies & Principles
- **Language:** Java
- **Core Concepts:** Object-Oriented Programming (OOP), Custom Interfaces, Dynamic Array Mapping, Matrix Operations, Algorithm Efficiency, Cache Locality.

## Project Structure
- `Matrix.java` – Core matrix interface defining arithmetic and manipulation methods.
- `FlatMatrix.java` – High-performance 1D array-based matrix implementation.
- `MatrixOperations.java` – Linear algebra computations (addition, multiplication, squared distances, normalizations).
- `IndexComparator.java` – Custom comparator for sorting training instances by feature distances.
- `MatrixUtils.java` – Helper utility functions.
