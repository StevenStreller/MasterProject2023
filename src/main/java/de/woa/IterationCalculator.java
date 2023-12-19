package de.woa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class IterationCalculator {

    public static int[] findClosest(int target) {
        String csvFilePath = "/heuristic_result.csv";
        char csvDelimiter = ';';
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(IterationCalculator.class.getResourceAsStream(csvFilePath))))) {
            String line;
            int[] arrayFirstColumn = new int[0];
            int[] arraySecondColumn = new int[0];
            int[] arrayThirdColumn = new int[0];


            while ((line = br.readLine()) != null) {
                String[] values = line.split(String.valueOf(csvDelimiter));
                int valueFirstColumn = Integer.parseInt(values[0].trim());
                int valueSecondColumn = Integer.parseInt(values[1].trim());
                int valueThirdColumn = Integer.parseInt(values[2].trim());


                int[] newArrayFirstColumn = new int[arrayFirstColumn.length + 1];
                int[] newArraySecondColumn = new int[arraySecondColumn.length + 1];
                int[] newArrayThirdColumn = new int[arrayThirdColumn.length + 1];

                System.arraycopy(arrayFirstColumn, 0, newArrayFirstColumn, 0, arrayFirstColumn.length);
                System.arraycopy(arraySecondColumn, 0, newArraySecondColumn, 0, arraySecondColumn.length);
                System.arraycopy(arrayThirdColumn, 0, newArrayThirdColumn, 0, arrayThirdColumn.length);

                newArrayFirstColumn[arrayFirstColumn.length] = valueFirstColumn;
                newArraySecondColumn[arraySecondColumn.length] = valueSecondColumn;
                newArrayThirdColumn[arrayThirdColumn.length] = valueThirdColumn;

                arrayFirstColumn = newArrayFirstColumn;
                arraySecondColumn = newArraySecondColumn;
                arrayThirdColumn = newArrayThirdColumn;
            }

            int low = 0;
            int high = arrayFirstColumn.length - 1;

            while (low <= high) {
                int mid = low + (high - low) / 2;

                if (arrayFirstColumn[mid] == target) {
                    return new int[]{arrayFirstColumn[mid], arraySecondColumn[mid], arrayThirdColumn[mid]};
                }

                if (arrayFirstColumn[mid] < target) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            int diffLow = Math.abs(arrayFirstColumn[low] - target);
            int diffHigh = Math.abs(arrayFirstColumn[high] - target);

            int closestIndex = (diffLow <= diffHigh) ? low : high;

            return new int[]{arrayFirstColumn[closestIndex], arraySecondColumn[closestIndex], arrayThirdColumn[closestIndex]};
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
