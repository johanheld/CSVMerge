import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by johan on 2018-04-23.
 */
public class Main
{
    private static final String TELEMETRY_CSV_FILE_PATH = "C://users/johan/desktop/data/telemetry.csv";
    private static final String FAILURES_CSV_FILE_PATH = "C://users/johan/desktop/data/failures.csv";
    private static final String NEW_CSV = "C://users/johan/desktop/data/new_csv.csv";

    public static void main(String[] args)
    {

        ArrayList<String[]> telementaries = new ArrayList<String[]>();
        ArrayList<String[]> failures = new ArrayList<String[]>();
        try
        {
            Reader reader2 = Files.newBufferedReader(Paths.get(FAILURES_CSV_FILE_PATH));
            Reader reader = Files.newBufferedReader(Paths.get(TELEMETRY_CSV_FILE_PATH));


            CSVParser csvParser2 = new CSVParser(reader2, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());



            BufferedWriter writer = Files.newBufferedWriter(Paths.get(NEW_CSV));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("datetime", "machineID", "volt", "rotate", "pressure", "vibration", "failure"));

            System.out.println("Loading telementaries... ");
            for (CSVRecord csvRecord : csvParser)
            {
                // Accessing Values by Column Index
                String timestamp = csvRecord.get(0);
                String machineID = csvRecord.get(1);
                String volt = csvRecord.get(2);
                String rotate = csvRecord.get(3);
                String pressure = csvRecord.get(4);
                String vibration = csvRecord.get(5);
                String[] tuple = {timestamp,machineID,volt,rotate,pressure,vibration,"0"};
                telementaries.add(tuple);



            }

            System.out.println(telementaries.size());

            System.out.println("Loading failures...");
            for(CSVRecord csvRecord2 : csvParser2)
            {

                String timestamp2 = csvRecord2.get(0);
                String machineID2 = csvRecord2.get(1);
                String[] tuple = {timestamp2, machineID2};
                failures.add(tuple);
            }

            System.out.println("Merging csv");

            for(String [] tupleTelementary : telementaries)
            {
                System.out.println("telementary: "+tupleTelementary[0]+"::"+tupleTelementary[1]);

                for(String[] tupleFailure : failures)
                {
                    System.out.println("failure: "+tupleFailure[0]+"::"+tupleFailure[1]);
                    if(tupleFailure[0].equalsIgnoreCase(tupleTelementary[0]) && tupleFailure[1].equalsIgnoreCase(tupleTelementary[1])){
                        System.out.println("Failure found");
                        tupleTelementary[6]="1";
                        break;
                    }
                }
            }

            //skriva ny csv
            for(String[] tuple : telementaries){
                csvPrinter.printRecord(tuple[0],tuple[1],tuple[2],tuple[3],tuple[4],tuple[5],tuple[6]);
            }
            csvPrinter.flush();


        }catch (Exception e)
        {
            e.printStackTrace();
        }

        for (String [] tele : telementaries)
        {
            System.out.print(tele[0]);
            System.out.print(",");
            System.out.print(tele[1]);
            System.out.print(",");
            System.out.print(tele[2]);
            System.out.print(",");
            System.out.print(tele[3]);
            System.out.print(",");
            System.out.print(tele[4]);
            System.out.print(",");
            System.out.print(tele[5]);
            System.out.print(",");
            System.out.print(tele[6]);
            System.out.println();
        }


        System.out.println("Done!");
    }
}
