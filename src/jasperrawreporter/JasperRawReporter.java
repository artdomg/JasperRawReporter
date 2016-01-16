
package jasperrawreporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

import net.sf.jasperreports.engine.JasperRunManager;
/**
 *
 * @author Arturo Dominguez
 */
public class JasperRawReporter {

    public void generateReport(String reportPath, Map params, String format, Connection connection) {
        try {
            File file = new File(reportPath);
            InputStream is = new FileInputStream(file);
            
            if(format.equals("html")) {
                String destPath = Math.random() + ".html";
                JasperRunManager.runReportToHtmlFile(reportPath, destPath, params, connection);
                File reportFile = new File(destPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(reportFile)));
                String line;
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                
                br.close();
                reportFile.delete();
            }
            else if(format.equals("pdf")) {
                System.out.write(JasperRunManager.runReportToPdf(is, params, connection));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Map createParameters(String paramString) {
        Map parameters = new HashMap<String, Object>();
        
        JSONObject params = new JSONObject(paramString);
        Iterator<String> iterator = params.keys();
        String key;
        
        while(iterator.hasNext()) {
            key = iterator.next();
            parameters.put(key, params.get(key));
        }
    
        return parameters;
    }
    
    public static Connection createConnection(String paramString) throws ClassNotFoundException, SQLException{
        JSONObject params = new JSONObject(paramString);
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        
        if(params.getString("vendor").equals("mysql")) {
            connection=DriverManager.getConnection("jdbc:mysql://" + params.getString("host")+ "/" + params.getString("db"), params.getString("user"), params.getString("password"));
        }
        
        return connection;
    }
    
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }
        
        Map parameters = null;
        Connection connection = null;
        
        if(args.length == 3) {
            parameters = createParameters(args[2]);
        }
        
        if(args.length == 4) {
            try {
                connection = createConnection(args[3]);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        JasperRawReporter report = new JasperRawReporter();
        report.generateReport(args[0], parameters, args[1], connection);
    }
    
}
