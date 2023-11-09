package processData;

public class MatrixConverter {
    public static int[][] stringToMatrix(String numbers) {
        int rows = 15;
        int cols = 15;

        int[][] matrix = new int[rows][cols];

        String[] tokens = numbers.split(" ");
        int index = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(tokens[index]);
                index++;
            }
        }

        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}
