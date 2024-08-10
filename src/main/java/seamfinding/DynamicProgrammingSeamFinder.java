package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        int width = picture.width();
        int height = picture.height();

        double[][] cost = new double[width][height];
        int[][] parent = new int[width][height];

        // Initialize the leftmost column in the 2D array with the energy for each pixel
        for (int y = 0; y < height; y++) {
            cost[0][y] = f.apply(picture, 0, y);
        }

        // Fill in the cost array for each remaining column
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Determine the minimum cost to reach (x, y) from the previous column
                double minCost = cost[x - 1][y];
                parent[x][y] = y;

                // Most Vulnerable Lines of Code:
                // Check the left-up neighbor
                if (y > 0 && cost[x - 1][y - 1] < minCost) {
                    minCost = cost[x - 1][y - 1];
                    parent[x][y] = y - 1;
                }
                // Most Vulnerable Lines of Code:
                // Check the left-down neighbor
                if (y < height - 1 && cost[x - 1][y + 1] < minCost) {
                    minCost = cost[x - 1][y + 1];
                    parent[x][y] = y + 1;
                }

                cost[x][y] = f.apply(picture, x, y) + minCost;
            }
        }

        // Find the end of the minimum cost seam in the last column
        double minCost = cost[width - 1][0];
        int minIndex = 0;
        for (int y = 1; y < height; y++) {
            if (cost[width - 1][y] < minCost) {
                minCost = cost[width - 1][y];
                minIndex = y;
            }
        }

        // Backtrack to find the seam path
        List<Integer> seam = new ArrayList<>(Collections.nCopies(width, 0));
        for (int x = width - 1, y = minIndex; x >= 0; x--) {
            seam.set(x, y);
            y = parent[x][y];
        }

        return seam;
    }
}
