package com.innoteva;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.innoteva.dao.ImageDao;

// Here I am using the db cloned

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	static final String LUCENE_INDEX_DIRECTORY = "/home/samuel/eclipseWorkspace/SemanticSearchServlet/lucene";
	private static IndexReader reader = null;
	private FileInputStream fis = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request,
	 *  HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException,IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,
	 *  HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setContentType("text/html");// this is the other configuration
		response.setContentType("application/json");
		//boolean emptyImages = false;
		//IndexReader reader = null;
		//StandardAnalyzer analyzer = null;
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		
		SpanishAnalyzer analyzer = null;    
		IndexSearcher searcher = null;
		TopScoreDocCollector collector = null;
		QueryParser parser = null;
		Query query = null;
		ScoreDoc[] hits = null;
		//ArrayList<Image> Images = null;
		
		//This is the most recent tutorial as reference https://howtodoinjava.com/lucene/lucene-index-search-examples/
		
		try{
			// Assume default encoding.
			//File file = new File("/home/samuel/eclipseWorkspace/SemanticSearchServlet/temp.txt");
            //FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            
            // Always wrap FileWriter in BufferedWriter.
            //BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
			// put some value pairs into the JSON object as into a Map.
			json.put("status", 200);
			json.put("msg", "OK");
			
			//store the parameter value in query variable
			String userQuery = request.getParameter("query");
			//create standard analyzer object
			analyzer = new SpanishAnalyzer();
			//create File object of our index directory
			//File file = new File(LUCENE_INDEX_DIRECTORY);
			//create index reader object
			//reader = IndexReader.open(FSDirectory.open(file.toPath()),true);
			//create index searcher object
			//searcher = new IndexSearcher(reader);
			searcher = createSearcher();
			//create topscore document collector
			collector = TopScoreDocCollector.create(1000);
			//create query parser object
			parser = new QueryParser("description", analyzer);
			//parse the query and get reference to Query object
			query = parser.parse(userQuery);
			//search the query
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			//check whether the search returns any result
			if(hits.length>0){
				
				json.put("foundImages", "Yes, we could find the next.");
				
				JSONArray list = new JSONArray();
				
				//print heading
				//pw.println("<P><TABLE BORDER=\1\">");
				//pw.println("<TR><TD>Name</TD><TD>Age</TD><TD>" +
				//		"Designation</TD></TR>");	
				ImageDao hData = new ImageDao();
				//Images = new ArrayList<Image>();
				//iterate through the collection and display result
				
				for(int i=0; i<hits.length; i++){
					int scoreId = hits[i].doc;
					//Image imageSearch = new Image(); //I was using this way to send the array to show it over jsp
					
					//now get reference to document
					Document document = searcher.doc(scoreId);
					int imageId = (int) document.getField("id").numericValue();
					String imageFilePath = hData.getImageById(imageId).getPath();
					
					//imageSearch.setImageId(imageId);
					//imageSearch.setName(document.getField("name").stringValue());
					//imageSearch.setDescription(document.getField("description").stringValue());
					//imageSearch.setPath(imageFilePath);
					
					JSONObject map = new JSONObject();
					map.put("id", imageId);
					map.put("name", document.getField("name").stringValue());
					map.put("description", document.getField("description").stringValue());
					map.put("path", imageFilePath);
					map.put("image", getImageEncoded(imageFilePath));
					list.put(map);
					
					//pw.println("<TR><TD>"+document.getField(
					//"name").stringValue()+"</TD><TD>"+
					//document.getField("age").stringValue()+
					//"</TD><TD>"+document.getField(
					//"designation").stringValue()+
					//"</TR>");
					//Images.add(imageSearch);
					
					// Note that write() does not automatically
		            // append a newline character.
					//bufferedWriter.write(getImageEncoded(imageFilePath));
					//bufferedWriter.newLine();
				}
				json.put("images", list);
				//emptyImages = true;
				//pw.println("</TABLE>");
			} else {
				//emptyImages = false;
				json.put("foundImages", "No, we could not find the next.");
			}
			
			// Always close files.
            //bufferedWriter.close();
            
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null) {
				reader.close();
			}
			if(fis != null) {
				fis.close();
			}
		}
		//pw.println("</BODY>");
		//pw.println("</HTML>");
		
		// finally output the json string		
		out.print(json.toString());
		
		//if(emptyImages) {
		//	request.setAttribute("imageList", Images);
		//	getServletContext().getRequestDispatcher("/searchResults.jsp").forward(request, response);
		//} else {
		//	request.setAttribute("message", "<P>No records found");
		//	getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		//}
	}
	
	/*
	private static TopDocs searchByFirstName(String firstName, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("firstName", new StandardAnalyzer());
        Query firstNameQuery = qp.parse(firstName);
        TopDocs hits = searcher.search(firstNameQuery, 10);
        return hits;
    }
 
    private static TopDocs searchById(Integer id, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("id", new StandardAnalyzer());
        Query idQuery = qp.parse(id.toString());
        TopDocs hits = searcher.search(idQuery, 10);
        return hits;
    }
	*/
	private static IndexSearcher createSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_DIRECTORY));
        reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

	// I found this on http://www.thejavaprogrammer.com/convert-image-to-base64-string-or-base64-string-to-image-in-java/
	private String getImageEncoded(String Path) throws IOException {
		File f = new File( Path );		//change path of image according to you
		
		byte byteArray[] = null;
		
		try {
			fis = new FileInputStream(f);
			byteArray = new byte[(int)f.length()];
			fis.read(byteArray);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String imageString = Base64.encodeBase64String(byteArray);
		
		return imageString;
	}
 
}