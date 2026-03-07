# Proyecto PPI - Visualizador y Analizador de Redes de Interacción Proteína-Proteína

**Una herramienta gráfica para modelar, visualizar y analizar redes biológicas de proteínas utilizando teoría de grafos.**

Descripción del Proyecto

Este proyecto implementa una aplicación de escritorio con interfaz gráfica (GUI) que modela una Red de Interacción Proteína-Proteína (PPIN) como un **grafo no dirigido con pesos**. El sistema permite cargar archivos CSV con datos de interacciones, visualizar la red, y aplicar algoritmos fundamentales para el análisis biológico:

*   **Detección de complejos proteicos** (componentes conexos) mediante **BFS**.
*   **Identificación de proteínas esenciales (hubs)** a través del cálculo de **centralidad de grado**.
*   **Búsqueda de la ruta metabólica más corta** (de menor resistencia) usando el algoritmo de **Dijkstra**.
*   **Modificación del grafo** (agregar/eliminar proteínas e interacciones) para simular efectos farmacológicos.
*   **Persistencia** de los cambios en archivos CSV.

Integrantes del Equipo

*   **Samuel Djekki**
*   **Juan Ferreira**
*   **Valeria Torres**

Repositorio en GitHub

[https://github.com/samu1243/proyecto-1-PPI](https://github.com/samu1243/proyecto-1-PPI)

Estructura de la Interfaz y Localización de Funcionalidades

La ventana principal se divide en tres áreas:

*   **Barra de menú superior** (Archivo, Edición, Análisis).
*   **Panel lateral izquierdo** con botones de acceso rápido.
*   **Panel central** donde se visualiza el grafo.
*   **Panel inferior** con área de resultados y un campo de comandos opcional.


