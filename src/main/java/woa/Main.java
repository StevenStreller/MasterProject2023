package woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private ArrayList<Evaluable> resultSet = new ArrayList<>();
    private static Dataset dataset;

    /**
     *
     * @param args first argument is path to file.tsp
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please set the path to the *.tsp");
        }
        dataset = Parser.read(args[0]);


        Fitness fitness = new Fitness(dataset);

        WOA woa = new WOA(10, dataset);
    }
}
