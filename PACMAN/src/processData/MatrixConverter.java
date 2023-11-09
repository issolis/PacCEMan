package processData; 

public class MatrixConverter {
    public static int[][] stringToMatrix(String matrixString) {
        String[] rows = matrixString.split("\n");

        int numRows = rows.length;
        int numCols = rows[0].split(" ").length;

        int[][] matrix = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            String[] elements = rows[i].split(" ");
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
        }

        return matrix;
    }  
}
