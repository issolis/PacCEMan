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



char* matrixToString(int matrix[15][15], int rows, int cols) {
    char* result = (char*)malloc(rows * cols * 5);  // Supongamos un tamaño máximo razonable

    int index = 0;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            int num = matrix[i][j];
            int length = sprintf(result + index, "%d ", num);
            index += length;
        }
    }

    return result;
}
