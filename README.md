# Space Invaders Clone & Custom 2D Java Engine

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge)

> **Nota:** Este proyecto fue desarrollado como trabajo colaborativo universitario. Se expone aquí la arquitectura del motor 2D y la implementación técnica.

## Descripción del Proyecto
Desarrollo desde cero de un clon del clásico Space Invaders. En lugar de utilizar librerías gráficas externas o motores preconstruidos, el equipo programó un motor 2D propio en Java, gestionando de forma manual el renderizado por fotogramas, la memoria y las físicas básicas.

El desarrollo se centró en la aplicación rigurosa de los principios de la Programación Orientada a Objetos (POO) y el uso de un flujo de trabajo estructurado con control de versiones en equipo.

## Características Técnicas y Arquitectura

* **Custom Game Loop:** Implementación de un bucle de juego estructurado (Update/Render) que garantiza la independencia entre la lógica del juego y el dibujado de los gráficos, bloqueado a 60 FPS estables.
* **Sistema de Hitboxes Dinámicas:** Motor de detección de colisiones 2D programado desde cero para gestionar los impactos entre proyectiles, barreras y naves mediante el cálculo de intersección de polígonos.
* **Arquitectura de Clases (POO):** 
  * Uso de Herencia y Polimorfismo para gestionar las diferentes entidades del juego (Jugador, Enemigos Básicos, Nave Nodriza).
  * Patrones de diseño para la instanciación eficiente de múltiples entidades en pantalla (manejo de memoria de los proyectiles).
* **Renderizado de Sprites:** Carga y dibujado eficiente de recursos gráficos en pantalla.

## Cómo ejecutar el proyecto
1. Clona este repositorio: `git clone https://github.com/alexhgar/Space-Invaders-2D-Engine.git`
2. Abre el proyecto en tu entorno de desarrollo compatible con Java (Eclipse, IntelliJ, VS Code).
3. Compila y ejecuta la clase principal `Main.java`.

## Equipo de Desarrollo
Proyecto desarrollado de forma colaborativa mediante Git:
* Alex Hinojar - Desarrollo e Implementación
* Julen - Desarrollo e Implementación
* Erik - Desarrollo e Implementación
