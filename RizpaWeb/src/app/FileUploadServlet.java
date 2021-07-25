package app;

import app.constant.Constants;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        Collection<Part> parts = request.getParts();
        ServletUtils.getUsernameFromSession(request.getSession(true));
        String username = ServletUtils.getUsernameFromSession(request.getSession());
        InputStream file = getFileAsInputStream(parts);
        RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
        try {
            rizpaFacade.loadNewData(username, file);
        } catch (Exception e) {
            response.setStatus(401);
            response.getOutputStream().println(e.getMessage());
        }
    }

    private InputStream getFileAsInputStream(Collection<Part> fileParts) {
        Collection<InputStream> collect = fileParts.stream().map(part -> {
            try {
                return part.getInputStream();
            }
            catch (IOException ignored) {}

            return null;
        }).collect(Collectors.toList());

        return new SequenceInputStream(Collections.enumeration(collect));
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(Collection<Part> fileParts) {
        try {
            StringBuilder fileContent = new StringBuilder();

            for (Part part : fileParts) {
                //to write the content of the file to a string
                fileContent.append("New Part content:").append("\n");
                fileContent.append(readFromInputStream(part.getInputStream())).append("\n");
            }
            System.out.println("Total parts : " + fileParts.size() + "\n");
            System.out.println(fileContent);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}