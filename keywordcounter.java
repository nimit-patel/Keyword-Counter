import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Keywordcounter program finds top K word.
 *
 *  @author Nimitbhai Patel
 *          UFID: (7092-9185)
 *          npatel1@ufl.edu
 */
public class keywordcounter {

    public static void main(String[] args){

        if(args.length == 1){

            // file to read
            File inputFile = new File(args[0]);
            // file to ouptut
            File outputFile = new File("output_file.txt");

            BufferedReader fileReader = null;
            BufferedWriter fileWriter = null;

            if(inputFile.exists()){

                try {
                    String inputFileLine = null;
                    String word = null;
                    int spaceIndex = -1;
                    int frequency = 0;
                    MaxFibonacciHeap heap = new MaxFibonacciHeap();
                    Map<String, Node> wordToNode = new HashMap<>();

                    fileReader = new BufferedReader(new FileReader(inputFile));
                    fileWriter = new BufferedWriter(new FileWriter(outputFile));

                    while((inputFileLine = fileReader.readLine()) != null){

                        // parsing the inputFileLine
                        if(inputFileLine.charAt(0) == '$'){
                            // insertion/increaseKey operation is done

                            // parse word and frequency
                            spaceIndex = inputFileLine.indexOf(' ');
                            word = inputFileLine.substring(1, spaceIndex);
                            frequency = Integer.parseInt(inputFileLine.substring(spaceIndex + 1));

                            // check if the word exist already
                            if(wordToNode.containsKey(word)){
                                // increment word count by frequency
                                heap.increaseKey(wordToNode.get(word), frequency);
                            }else{
                                // normal insert at the top level
                                Node node = heap.insert(word, frequency);
                                wordToNode.put(word, node);
                            }

                        }else if(Character.isDigit(inputFileLine.charAt(0))){
                            // stores the top K nodes
                            List<Node> topK = new ArrayList<>();
                            int K = Integer.parseInt(inputFileLine);

                            for(int i = 0; i < K; ++i){
                                Node max = heap.removeMax();
                                if(max != null){
                                    topK.add(max);
                                }else{
                                    break;
                                }
                            }

                            for(Node node : topK){
                                heap.insert(node);
                            }

                            // create output file if it does not exist
                            if(!outputFile.exists())
                                outputFile.createNewFile();

                            // writing to the output file
                            writeOutput(topK, fileWriter);

                        }else{
                            // close read
                            fileWriter.close();
                            break;
                        }
                    }
                    fileReader.close();
                }catch (IOException ioe){
                    System.out.println(ioe.getMessage());
                }
            }else{
                System.out.println(args[0] + " file doesn't exist!");
            }
        }else{
            System.out.print("Keyword counter takes exactly one argument!");
        }
    }


    public static void writeOutput(List<Node> topK, BufferedWriter fileWriter) throws IOException{
        char separator = ',';

        StringBuilder topKWords = new StringBuilder();

        for(Node node: topK){
            topKWords.append(node.getWord());
            topKWords.append(separator);
        }

        if(topKWords.length() > 0)
            topKWords.deleteCharAt(topKWords.length() - 1);

        topKWords.append('\n');

        System.out.println(topKWords.toString());

        fileWriter.write(topKWords.toString());
    }

}
