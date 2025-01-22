// Author: Omar Essa
// Date Created: 2/29/2024
// Editors: Boris Alarcon, Omar Essa
// Date Last edited: 4/15/24, 2/29/2024
// Changes: Removed the getFileString from multiple other servlets and added it to the parent servlet
// Purpose: basic servlet display

package NolaBytes.front_end.servlets;

// Import required java libraries

import NolaBytes.Globals;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");//setting the content type
        PrintWriter pw = res.getWriter();//get the stream to write the data

        //writing html in the stream
        pw.println("<html><body>");
        pw.println("Welcome to servlet");
        pw.println("</body></html>");

        pw.close();//closing the stream
    }

    protected Globals getGlobals(String inputPath) {
        ServletContext application = getServletConfig().getServletContext();
        try {
            return new Globals(new FileInputStream((application.getRealPath("/WEB-INF/" + inputPath))));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getFileString(String inputPath) {
        String baseString = "";
        ServletContext application = getServletConfig().getServletContext();
        String path = application.getRealPath(inputPath);
        File basePageFile = new File(path);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(basePageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while (in.ready()) {
                baseString += in.readLine();
            }
            return baseString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "something went wrong";
    }
}