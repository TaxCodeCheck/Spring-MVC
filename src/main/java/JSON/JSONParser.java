package JSON;

import static java.lang.Double.parseDouble;

public class JSONParser {
    public static double parseTaxRate (String string){
        double rate = 0;
        int summaryIndex = string.indexOf("summary");
        string = string.substring(summaryIndex);

        while (string.contains("\"rate\":")){
            int startOfRate = string.indexOf("\"rate\":");
            string = string.substring(startOfRate+8);

            int endOfRate = string.indexOf(",");
            String rateString = string.substring(0,endOfRate);

            rate += parseDouble(rateString);
            string = string.substring(endOfRate);
        }
        return rate;
    }
}
