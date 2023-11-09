#include <stdio.h>
#include <stdlib.h>
#include <string.h>


int extractNumber(const char *str, int pos) {
    int number = 0;
    int currentPos = 0;
    int len = strlen(str);
    int startIndex = -1;

    for (int i = 0; i < len; i++) {
        if (isdigit(str[i])) {
            if (startIndex == -1) {
                startIndex = i;
            }
        } else {
            if (startIndex != -1) {
                currentPos++;
                if (currentPos == pos) {
                    char numberStr[20]; // Tamaño suficiente para números razonables
                    strncpy(numberStr, str + startIndex, i - startIndex);
                    numberStr[i - startIndex] = '\0';
                    number = atoi(numberStr);
                    break;
                }
                startIndex = -1;
            }
        }
    }

    return number;
}



// Función para convertir una matriz en una cadena con formato de matriz
char* matrixToString(int matrix[][15], int rows, int cols) {
    char* result = (char*)malloc(1000); // Supongamos un tamaño máximo razonable

    // Inicializa la cadena vacía
    strcpy(result, "");

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            char element[10]; // Suponemos que cada elemento de la matriz puede tener hasta 10 caracteres
            sprintf(element, "%d", matrix[i][j]);
            strcat(result, element);
            if (j < cols - 1) {
                strcat(result, " "); // Separador entre elementos de la fila
            }
        }
        if (i < rows - 1) {
            strcat(result, "\n"); // Salto de línea entre filas
        }
    }

    return result;
}
