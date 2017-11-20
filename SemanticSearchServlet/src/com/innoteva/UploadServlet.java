package com.innoteva;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.innoteva.dao.ImageDao;
import com.innoteva.model.Image;
import com.innoteva.util.DbUtil;
import com.innoteva.IndexBuilder;

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {
    
	public Connection connection;
	public ImageDao hData;
	public File dataDir = new File("/home/samuel/eclipseWorkspace/SemanticSearchServlet/lucene/");
	private IndexBuilder builder;
	
	private final static Logger LOGGER = Logger.getLogger(UploadServlet.class.getCanonicalName());
	
	public UploadServlet() {
		super();
		hData = new ImageDao();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4164897894467893805L;
	/**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "/home/samuel/eclipseWorkspace/SemanticSearchServlet";
     
    /**
     * handles file upload
     *
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // gets absolute path of the web application
        //String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        //String savePath = appPath + File.separator + SAVE_DIR;
    	connection = DbUtil.getConnection();
    	Image image = new Image();
    	
    	String savePath = SAVE_DIR;
    	image.setDescription(request.getParameter("paragraph_text"));
    	
        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        
        //this is the step to save on file folder
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            image.setName(fileName);
            
            //part.write(savePath + File.separator + fileName);
            part.write(fileName);
            image.setPath(savePath + File.separator + fileName);
        }
        
        hData.addImage(image);
        
        request.setAttribute("message", "Upload has been done successfully! The " + hData.getImageById(hData.getAllImages().size() -1).getName());
        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
    }
    */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	 processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
    	response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("latin1");
        
        File[] files = dataDir.listFiles();
        
        if(files.length > 0) {
        
        	for (File aFile : files) {
        		aFile.delete();
            }
        }
        
	        Image image = new Image();
	        connection = DbUtil.getConnection();
	        
	        // Create path components to save the file
	        //final String path = request.getParameter("destination");
	        final String path = SAVE_DIR;
	        final Part filePart = request.getPart("file");
	        final String fileName = getFileName(filePart);
	        
	        OutputStream out = null;
	        InputStream filecontent = null;
	
	        try {
	        	image.setDescription(request.getParameter("paragraph_text"));
	        	image.setName(fileName);
	        	
	        	String pathComplete = path + File.separator + fileName;
	        	
	            out = new FileOutputStream(new File(pathComplete));
	            filecontent = filePart.getInputStream();
	            image.setPath(pathComplete);
	            hData.addImage(image);
	            
	            int read = 0;
	            final byte[] bytes = new byte[1024];
	
	            while ((read = filecontent.read(bytes)) != -1) {
	                out.write(bytes, 0, read);
	            }
	            //writer.println("New file " + fileName + " created at " + pathComplete);
	            
	            //Here I am trying to write the index inside the lucene folder
	            // I guide from here http://www.jpgtutorials.com/lucene-full-text-search-in-java
	            builder = new IndexBuilder();
	        	builder.buildIndex();
	            
	            request.setAttribute("message", "Upload has been done successfully! The " 
	            		+ hData.getImageById(hData.getAllImages().size()).getName());
	            
	            LOGGER.log(Level.INFO, "File{0} being uploaded to {1}", new Object[]{fileName, path});
	        
	        } catch (FileNotFoundException fne) {
	        	request.setAttribute("message", "You either did not specify a file to upload or are "
	                    + "trying to upload a file to a protected or nonexistent "
	                    + "location." + "<br/> ERROR: " + fne.getMessage());
	
	            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
	                    new Object[]{fne.getMessage()});
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	            if (filecontent != null) {
	                filecontent.close();
	            }
	        }
        
        
        
        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
    /**
     * Extracts file name from HTTP header content-disposition
     *
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
    */
}

