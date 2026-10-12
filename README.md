# Cache-Efficient k-NN Classifier (Inspired by HR Data Analytics)

## Overview
During my service as an HR Data Analyst in the IDF, I managed and analyzed hundreds of thousands of personnel records. While SQL and BI tools are excellent for business logic, I realized that Machine Learning algorithms could uncover hidden patterns in placement success rates and dropout risks that standard queries miss.

Inspired by this domain, I built this Java implementation of a **k-Nearest Neighbors (k-NN) classification pipeline from scratch**, coupling it with a highly optimized, cache-efficient memory representation designed to handle large-scale datasets rapidly.

This project bridges practical data analysis experience with core computer science algorithmic design, focusing heavily on runtime efficiency, memory optimization, and hardware-aware programming.

## Core Features & Optimizations

### 1. Performance & Memory Optimization (`FlatMatrix`)
When processing massive datasets, standard 2D arrays suffer from cache misses due to fragmented memory allocation. 
- Re-implemented standard 2D arrays as **contiguous 1D memory buffers** in row-major order.
- Significantly enhances spatial locality and CPU cache utilization, reducing memory access overhead for intensive linear algebra operations and distance calculations.

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
