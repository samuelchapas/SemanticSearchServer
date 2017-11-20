package com.innoteva;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LegacyIntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import com.innoteva.dao.ImageDao;
import com.innoteva.model.Image;
 
 
@SuppressWarnings("deprecation")
public class IndexBuilder {
	
	private final static Logger LOGGER = Logger.getLogger(IndexBuilder.class.getCanonicalName());
 
	static final String LUCENE_INDEX_DIRECTORY = "/home/samuel/eclipseWorkspace/SemanticSearchServlet/lucene";
	//static final String DB_HOST_NAME = "localhost";
	//static final String DB_USER_NAME = "root";
	//static final String DB_PASSWORD = "jinoy";
	//public ImageDao hData;
 
	//public IndexBuilder() {
	//	super();
		//LOGGER.log(Level.INFO, "File{0} being uploaded to {1}", new Object[]{fileName, path});
		
		
	//}
	//method for indexing
	public void buildIndex(){
		
		LOGGER.log(Level.INFO, "Here we are present on IndexBuider");
		ImageDao hData = new ImageDao();
		
		List<Image> images = new ArrayList<Image>(); 
		
		IndexWriter writer=null;
		SpanishAnalyzer analyzer = null;		
		File file = null;
		
		try {
			images = hData.getAllImages();
			
			//System.out.println("Start indexing");
			//get a reference to index directory file
			file = new File(LUCENE_INDEX_DIRECTORY);
			analyzer = new SpanishAnalyzer();
			Directory index = FSDirectory.open(file.toPath());
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			
			//This was the code extracted from http://www.jpgtutorials.com/lucene-full-text-search-in-java
			//writer = new IndexWriter(
			//		FSDirectory.open(file.toPath()),
			//		analyzer,
			//		true,
			//		IndexWriter.MaxFieldLength.LIMITED
			//		);
			
			writer = new IndexWriter(index, config);
 
			//initialize the driver class
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			//get connection object
			//con = DriverManager.getConnection(
			//		"jdbc:mysql://"+DB_HOST_NAME+"/mydb",
			//		DB_USER_NAME, DB_PASSWORD);
			//create statement object
			//stmt = con.createStatement();
			//execute query
			//rs = stmt.executeQuery("SELECT * FROM employee");
			//iterate through result set
			for(Image image : images){
				int id = image.getImageId();
				String name = image.getName();
				String description = image.getDescription();
				//String designation = rs.getString("designation");
				//create a full text field which contains name,
				//age and designation
				//String fulltext = name + ": " + description;
 
				//create document object
				Document document = new Document();
				//create field objects and add to document				
				//Field idField = new Field("employeeid", 
				//		id, Field.Store.YES,
				//		Field.Index.NO);
				//document.add(idField);
				document.add(new LegacyIntField("id", id, Field.Store.YES));
				//Field nameField = new Field("name",
				//		name, Field.Store.YES,
				//		Field.Index.ANALYZED);
				document.add(new TextField("name", name , Field.Store.YES));
				//Field ageField = new Field("age",
				//		age, Field.Store.YES,
				//		Field.Index.NOT_ANALYZED);
				document.add(new TextField("description", description , Field.Store.YES));
				//Field designationField = new Field("designation",
				//		designation, Field.Store.YES,
				//		Field.Index.ANALYZED);
				//document.add(designationField);
				//Field fulltextField = new Field("fulltext",
				//		fulltext, Field.Store.NO,
				//		Field.Index.ANALYZED);
				//document.add(fulltextField);
				//add the document to writer
				writer.addDocument(document);
			}
			//optimize the index
			//System.out.println("Optimizing index");
			//Commits all pending changes (added and deleted documents, segment merges, added indexes, etc.) 
			//to the index, and syncs all referenced index files, such that a reader will see the changes and the index 
			//updates will survive an OS or machine crash or power loss.
			writer.commit();
 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(writer!=null)
					writer.close();
 
			}catch(Exception ex){
				ex.printStackTrace();
			}
 
		}
 
 
	}
 
	public static void main(String[] args) throws Exception {
 
		IndexBuilder builder = new IndexBuilder();
		builder.buildIndex();
	}
 
}