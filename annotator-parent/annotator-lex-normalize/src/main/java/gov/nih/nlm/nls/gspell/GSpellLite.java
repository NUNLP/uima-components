package gov.nih.nlm.nls.gspell;

import gov.nih.nlm.nls.attributions.Attributions;
import gov.nih.nlm.nls.dbm.JDbmFactory;
import gov.nih.nlm.nls.gspell.Benchmark;
import gov.nih.nlm.nls.gspell.Candidate;
import gov.nih.nlm.nls.gspell.Candidates;
import gov.nih.nlm.nls.gspell.CommonMisspellings;
import gov.nih.nlm.nls.gspell.FindOptions;
import gov.nih.nlm.nls.gspell.GSpell;
import gov.nih.nlm.nls.gspell.GSpellException;
import gov.nih.nlm.nls.gspell.Homophones;
import gov.nih.nlm.nls.gspell.Options;
import gov.nih.nlm.nls.simpleCorpusStatistics.SimpleCorpusStatistics;
import gov.nih.nlm.nls.utils.Debug;
import gov.nih.nlm.nls.utils.Fields;
import gov.nih.nlm.nls.utils.GlobalBehavior;
import gov.nih.nlm.nls.utils.Stats;
import gov.nih.nlm.nls.utils.U;
import gov.nih.nlm.nls.utils.Use;
import gov.nih.nlm.nls.utils.Version;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


/* ================================================|GSpell.java |==== */
/**
* GSpell is a package that suggests close neighbors to an input
* term using a variety of methods including character based nGrams,
* metaphone'd terms, common mispellings. Soon to be added will
* be right truncation, and incorporating constrained stemming to
* select the anchor grams for the nGram approach.
*
* Gspell instances can be embedded into single threaded applications
* with no problem. It can be embedded into threaded applications with 
* the following advice:
*<p>
*   Embedding read-only instances of GSpell objects is fine as long
*   as there are no read/write instances present.
*<p>
*   It is recommended that in cases where there are updates or 
*   read/writes, this object gets implemented as a singleton instance
*   per dictionary, that is shared across threads and processes.  
*<p>
*   There is <font color=RED>NO </font> writer lock and unlock around the 
*   update/index method, nor around the cleanup or flush methods to insure 
*   that only one writer is alive at any given time, and that there
*   are no readers alive. It is up to the 
*   application developer to insure that access is controlled around 
*   GSpell Object instances that do writes or updates.
* <p> 
* 
* @author <a href="mailto:umlslex@nlm.nih.gov">umlslex</a>
* @version $Id: GSpell.java,v 1.48 2006/08/18 14:45:53 divita Exp $
*/
/* ================================================|GSpell.java |==== */
/* ---------------------------------|License Endowment|----
     
      The use and distribution of this material is subject to the terms and conditions 
      included in the file SPECIALIST_NLP_TOOLS_TERMS_AND_CONDITIONS.TXT, 
      located in the root directory of the distribution. 

       
   ---------------------------------|License Endowment|---- */

public final class GSpellLite 
{
  // ====================
  // CONSTRUCTORS
  // ====================

// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell. This constructor assumes 
 * that you are using this for lookup only. 
 *
 * The dictionaryName should reflect the name of a directory from
 * the ___GSPELL_ROOT/dictionaries directory, or it should be
 * the full path to the dictionary to be opened.
 * 
 * The ___GSPELL_ROOT/config path must be on the classpath 
 * 
 *
 * @param String pDictionaryName
 * @exception GSpellException
 *
*/
// ================================================|Constructor  |====
  public GSpellLite(String pDictionaryName) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:1");
    Debug.denter(DT14354);

    GlobalBehavior settings = new GlobalBehavior(APPLICATION_NAME,    //Application Name
						 "GSpellRegistry.cfg",//Generic Registry file
						 "gspell.cfg",        //Overriding setting file
						 null);               //Commandline args
    
    settings.set("--dictionaryName=" + pDictionaryName );
    settings.set("--find"); 
    
    init ( settings );
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell

// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell
 * 
 * The dictionaryName should reflect the name of a directory from
 * the ___GSPELL_ROOT/dictionaries directory, or it should be
 * the full path to the dictionary to be opened.
 * 
 * The ___GSPELL_ROOT/config path must be on the classpath 
 *
 * @param pDictionaryName
 * @param  pMode GSpell.WRITE_ONLY|GSpell.READ_WRITE|GSpell.READ_ONLY
 * @exception GSpellException
 *
*/
// ================================================|Constructor  |====
  public GSpellLite(String pDictionaryName, int pMode) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:2");
    Debug.denter(DT14354);


    GlobalBehavior settings = new GlobalBehavior("GSpell",            //Application Name
						 "GSpellRegistry.cfg",//Generic Registry file
						 "gspell.cfg",        //Overriding setting file
						 null);               //Commandline args
    
    settings.set("--dictionaryName=" + pDictionaryName );
    
    switch ( pMode ) {
    case GSpell.WRITE_ONLY:  settings.set("--index");  break;
    case GSpell.READ_WRITE:  settings.set("--update"); break;
    case GSpell.READ_ONLY:   settings.set("--find");   break;
    default:                 settings.set("--find");   break;
    }
  
    init ( settings );
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell

// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell for those that cannot set the classpath
 * 
 * The dictionaryName should reflect the name of a directory from
 * the ___GSPELL_ROOT/dictionaries/[dictionaryName] directory, or it should be
 * the full path to the dictionary to be opened.
 * 
 * @param pPathToConfig   The ___GSPELL_ROOT/config path
 * @param pDictionaryName
 * @param  pMode GSpell.WRITE_ONLY|GSpell.READ_WRITE|GSpell.READ_ONLY
 * @exception GSpellException
 *
*/
// ================================================|Constructor  |====
  public GSpellLite(String pConfigPath, String pDictionaryName, int pMode) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:2");
    Debug.denter(DT14354);


    GlobalBehavior settings = new GlobalBehavior("GSpell",            //Application Name
			    pConfigPath + U.FS + "GSpellRegistry.cfg",//Generic Registry file
						 "gspell.cfg",        //Overriding setting file
						 null);               //Commandline args
    
    settings.set("--dictionaryName=" + pDictionaryName );
    settings.set("--dictionaryDir=NULL");

    switch ( pMode ) {
    case GSpell.WRITE_ONLY:  settings.set("--index");  break;
    case GSpell.READ_WRITE:  settings.set("--update"); break;
    case GSpell.READ_ONLY:   settings.set("--find");   break;
    default:                 settings.set("--find");   break;
    }
  
    init ( settings );
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell


// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell
 * 
 *
 * @param pDictionary
 * @param  pMode GSpell.WRITE_ONLY|GSpell.READ_WRITE|GSpell.READ_ONLY
 * @exception GSpellException
 *
*/
// ================================================|Constructor  |====
  public GSpellLite(File pDictionary, int pMode) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:pDictionary:pMode");
    Debug.denter(DT14354);

    GlobalBehavior _settings = new GlobalBehavior("GSpell",            //Application Name
						  "GSpellRegistry.cfg",//Generic Registry file
						  "gspell.cfg",       //Overriding setting file
						  null);               //Commandline args
    
    _settings.set("--dictionaryName=" + pDictionary.getAbsolutePath() );
        
    switch ( pMode ) {
    case GSpell.WRITE_ONLY:  _settings.set("--index"); break;
    case GSpell.READ_ONLY:   _settings.set("--find"); break;
    case GSpell.READ_WRITE:  _settings.set("--update"); break;
    }

    init ( _settings );
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell

// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell
 *
 * @param pDictionaryDir
 * @param pDictionaryName
 * @param  pMode  --index|--find|--update 
 * @exception GSpellException
 *
*/
// ================================================|Constructor  |====
  public GSpellLite(String pDictionaryDir, String pDictionaryName, String pMode) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:pDictionary:pMode");
    Debug.denter(DT14354);

    GlobalBehavior _settings = new GlobalBehavior("GSpell",            //Application Name
						  "GSpellRegistry.cfg",//Generic Registry file
						  "gspell.cfg",       //Overriding setting file
						  null);               //Commandline args
    
    _settings.set("--dictionaryDir=" + pDictionaryDir);
    _settings.set("--dictionaryName=" + pDictionaryName );
    _settings.set( pMode );

    init ( _settings );
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell

// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell.  This constructor will read in
 * the dictionary.cfg (possibly again) and in doing so, will override
 * any command line settings set.  If command line settings are to
 * be specified, use the GSpell(Settings, args) constructor.
 *
 * @param  pSettings that incude truncation, max candidates ... 
 * @exception GSpellException
*/
// ================================================|Constructor  |====
  public GSpellLite(GlobalBehavior pSettings ) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:4");
    Debug.denter(DT14354);

    init ( pSettings );
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell

// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell
 *
 * @param  args      The command line arguments that override all others
 * @exception GSpellException
*/
// ================================================|Constructor  |====
  public GSpellLite(String args[] ) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:4");
    Debug.denter(DT14354);

    GlobalBehavior _settings = new GlobalBehavior("GSpell",            //Application Name
						  "GSpellRegistry.cfg",//Generic Registry file
						  "gspell.cfg",        //Overriding setting file
						  args);               //Commandline args
    init ( _settings );
    
    Debug.dexit(DT14354);

  } // ***End Constructor GSpell



// ================================================|Constructor  |====
/**
 * This is a constructor for GSpell
 *
 * @param  Options pOptions  Options that incude truncation, max candidates ... 
 * @exception GSpellException
 * @deprecated  This method is being deprecated in favor of methods that use the GlobalBehavior instance.
*/
// ================================================|Constructor  |====
  public GSpellLite(Options pOptions) throws GSpellException
  {
    Debug.dfname("GSpell:Constructor:3");
    Debug.denter(DT14354);

    init ( pOptions);
    
    Debug.dpr(DF14355,"End of constructor");
    
    Debug.dexit(DT14354);
  } // ***End Constructor GSpell




  // ====================
  // PUBLIC METHODS
  // ====================


// ================================================|Public Method Header|====
/**
 * Method index add terms to the multiple indexes created. Rows that start
 * with # are ignored.
 * 
 *
 * @param  BufferedReader pFStream  
 * @return int the number of terms processed
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public int index( BufferedReader pFStream ) throws GSpellException
  { 

    Debug.dfname("GSpell:index");
    Debug.denter(DT14360);

    int                     ctr = 0;

    try {
      String                 line = null;
      Stats                 stats = new Stats( 10000 );
 
      // -------------------------------------------
      // Loop through each word or term of the input
      // -------------------------------------------
      
      while (( line = pFStream.readLine() ) != null ) {

	
	if ( !line.startsWith("#") ) {
	  this.index( line );
	  stats.report();
	  ctr++;
	}
	
      } 
    
    } catch (Exception e ) {
      Debug.dexit(DT14360);
      throw new GSpellException ( e.toString() );
    }

    

    Debug.dexit(DT14360);
    return ( ctr );


   } // ***End public void index( InputReader pFileStream ) throws Exception
// ================================================|Public Method Header|====
/**
 * Method index adds a term to the multiple indexes created. Call the cleanup
 * method when all terms are indexed.
 *
 *  Footnote: 
 *   Common Mistakes and homophones gets indexed separately, because
 *   it takes pairs of key|value strings
 * 
 * @param  String pTerm 
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public void index( String pTerm ) throws GSpellException
  { 

    Debug.dfname("GSpell:index:String");
    Debug.denter(DT14362);

    // ----------------------------------------------
    // Block until we can open a writer 
    //    Once in, don't let anyone else 
    //    in until we are done writing 
    // 
    // If we were going to do the locking it would be 
    // here. But we are not doing locking.
    //
    // lock.lockWrite ();
    // ----------------------------------------------

    try {

      Debug.dpr(DF14363, "Adding the term |" + pTerm + "|" );
        this.nGrams.index( pTerm );
        this.phones.index( pTerm ); 
      // justTheRight.index( pFStream );

    } catch ( Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14362);
      throw new GSpellException ( e.toString() );

    } finally {

      // -----------------------------------------------------
      // If we were doing locking, then here is where we would
      // do the unlock.
      // 
      // lock.unlock();
      // -----------------------------------------------------
    } 


    Debug.dexit(DT14362);


   } // ***End public void index( String pTerm ) throws Exception

// ================================================|Public Method Header|====
/**
 * Method index adds an array of terms to the multiple indexes created.
 * Call the cleanup() method when all the terms are indexed.
 *
 *  Footnote: 
 *   Common Mistakes and homophones gets indexed separately, because
 *   it takes pairs of key|value strings
 * 
 * @param  String pTerm 
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public void index( String pTerm[] ) throws GSpellException
  { 

    Debug.dfname("GSpell:index:ArrayOfString");
    Debug.denter(DT14362);

    if ( pTerm != null )
      for (int i = 0; i < pTerm.length; i++ )
	index( pTerm[i] );


    Debug.dexit(DT14362);


   } // ***End public void index( String pTerm ) throws Exception

// ================================================|Public Method Header|====
/**
 * Method index adds a Collection of terms to the multiple indexes created.
 * Call the cleanup() method when all the terms are indexed.
 *
 *  Footnote: 
 *   Common Mistakes and homophones gets indexed separately, because
 *   it takes pairs of key|value strings
 * 
 * @param  String pTerm 
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public void index( Collection pTerms ) throws GSpellException
  { 

    Debug.dfname("GSpell:index:CollectionOfString");
    Debug.denter(DT14362);

    if ( pTerms != null )
      for (Iterator i = pTerms.iterator(); i.hasNext(); ) 
	index( (String) i.next() );


    Debug.dexit(DT14362);


   } // ***End public void index( String pTerm ) throws Exception
// ================================================|Public Method Header|====
/**
 * Method update updates the indexes with terms from the file into 
 * the dictionary. This method is expecting that the input stream to
 * contain one term per line.
 * The difference between updating and indexing is really determined
 * by how the class GSpell is instantiated. For updates, the mode 
 * should be set to GSpell.READ_WRITE. For Indexing, the mode should
 * set to GSpell.CREATE.
 * 
 * This method should be syncronized by the developer using this
 * method. That is, only one writer should
 * be allowed to write at any time. All readers should exit the water
 * while the writer is active to avoid the readers from getting out
 * of sync with the writer.  
 *
 * 
 * @param  BufferedReader pFStream
 * @return int  the number of terms updated.
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public int update( BufferedReader pFStream ) throws GSpellException
  { 

    Debug.dfname("update");
    Debug.denter(DT14364);
    
    int returnValue = 0;


    if (( this.mode != GSpell.READ_WRITE ) && ( this.mode != GSpell.CREATE )) {
      throw new GSpellException ("This gspell object may have been created with something other than updates in mind");
    } else { 
      
      int                     ctr = 0;
      
      try {
	String                 line = null;
	Stats                 stats = new Stats( 10000 );
	
	// -------------------------------------------
	// Loop through each word or term of the input
	// -------------------------------------------
	
	while (( line = pFStream.readLine() ) != null ) {
	  
	  
	  if ( !line.startsWith("#") ) {
	    this.update( line );
	    stats.report();
	    ctr++;
	  }
	} 
      } catch (Exception e ) {
	Debug.dexit(DT14360);
	throw new GSpellException ( e.toString() );
      }
    }

    Debug.dexit(DT14364);
    
    return( returnValue );


   } // ***End public void update( InputReader pFileStream ) throws Exception
// ================================================|Public Method Header|====
/**
 * update adds a term to the index. This method is equivalent to index(pTerm) 
 * The difference between updating and indexing is really determined
 * by how the class GSpell is instantiated. For updates, the mode 
 * should be set to GSpell.READ_WRITE. For Indexing, the mode should
 * set to GSpell.CREATE.
 * 
 * This method should be syncronized by the developer using this
 * method. That is, only one writer should
 * be allowed to write at any time. All readers should exit the water
 * while the writer is active to avoid the readers from getting out
 * of sync with the writer.  
 *
 * 
 * @param  String pTerm 		
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public void update( String pTerm ) throws GSpellException
  { 

    Debug.dfname("update");
    Debug.denter(DT14366);

    if (( this.mode != GSpell.READ_WRITE ) && ( this.mode != GSpell.CREATE )) {
      throw new GSpellException ("This gspell object may have been created with something other than updates in mind");
    } else { 
      
      // -------------------------------------------
      // Update if this is not yet in the dictionary 
      // -------------------------------------------
      if ( !this.exists( pTerm )  )
	index( pTerm );
      else {
	Debug.dpr(DF14367, "This term is already in the dictionary, not updating");
      }
    }
    
    Debug.dexit(DT14366);


   } // ***End public void update( String pTerm ) throws Exception

// ================================================|Public Method Header|====
/**
 * exists will return true if the term is in the dictionary 
 * 
 * @param  String pTerm 		
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public boolean exists( String pTerm ) throws GSpellException
  { 
    Debug.dfname("exists");
    Debug.denter(DT14366);

    boolean returnValue = false;
    String        aName = null;
    // --------------------------------------------------
    // just use the metaphone find to grab the candidates
    // --------------------------------------------------
    Object[]  metaphoneCandidates = null;
    try {
      metaphoneCandidates = phones.find( pTerm );
      if ( metaphoneCandidates != null ) {
	for ( int i = 0; i < metaphoneCandidates.length; i++ ) {
	  aName = ((Candidate)metaphoneCandidates[i]).getName();
	  if (( aName != null ) && ( aName.equalsIgnoreCase( pTerm ) )){
	    returnValue = true ;
	    break;
	  }
	}
      }
    } catch (Exception e ) {}
    
    metaphoneCandidates = null;
    
    Debug.dexit(DT14366);
    
    return ( returnValue );


   } // ***End public boolean exists( String pTerm ) throws Exception


// ================================================|Public Method Header|====
/**
 * Method reorganize reorganizes the db into an optimized or balanced btree
 * and does a save as a side effect. This method is useful when you've  done
 * some substantial number of updates where the underlying tree could get
 * out of balance. 
 * 
 * @exception GSpellException
 * @deprecated This method is no longer useful. This method was only use
 *             when the w3c jdbm implementation was used. This jdbm 
 *             implementation has since been dropped in favor of Russell 
 *             Loanes' store facility. 
 * 
*/
// ================================================|Public Method Header|====
public void reorganize( ) throws GSpellException
  { 

    Debug.dfname("reorganize");
    Debug.denter(DT14366);


    Debug.dexit(DT14366);


   } // ***End public void reorganize() throws Exception
// ================================================|Public Method Header|====
/**
 * Method save. This method calls the flush method. The flush method
 * should have a locking mechanism if one were to implement a locking method.
 * 
 * @exception GSpellException
 * 
*/
// ================================================|Public Method Header|====
public void save( ) throws GSpellException
  { 
    this.flush();

   } // ***End public void save() throws Exception
// ================================================|Public Method Header|====
/**
 * Method find 
 * This method finds documents from the ngram approach, phonetic
 * approach and soon to be others, then combines these, 
 * re-ranks them, and returns them as candidates. 
 *
 * This method writes to the output the following:
 *   inputTerm|candidate|rank|FromMethod|Messages
 * if there are candidates
 * Otherwise, this method writes the following:
 *   inputTerm||-9999||No Suggestions
 * 
 * Where messages could include "Correct" if the rank = 0;
 * or "No Suggestions" there are no close suggestions.
 *
 * @param  BufferedReader pFStream 
 * @param  PrintWriter pPrintStream
 * 
 * @return int  the number of terms updated.
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public int find( BufferedReader pFStream, PrintWriter pPrintStream ) throws GSpellException
  { 

    Debug.dfname("find");
    Debug.denter(DT14368);

    Candidate      candidates[] = null;
    String                 line = null;
    Stats                 stats = new Stats( 100 );
    int                     ctr = 0;
    String[]               cols = null;
    String              badTerm = null;
    String          correctTerm = null;
    
    boolean         fieldedText = this.settings.getBoolean("--fieldedText");
    int               termField = this.settings.getInt("--termField");
    int            correctField = this.settings.getInt("--correctField"); 
    Benchmark         benchmark = null;
    this.findOptions = new FindOptions( this.settings ); 

    try {

      if ( correctField > -1 ) 
	benchmark = new Benchmark( this.settings );
      
      
      while (( line = pFStream.readLine() ) != null ) {
	
	if (!(U.isPunctuation(line)) && ( line.trim().length() > 0 )) {
	
	  if ( fieldedText  ) {
	    cols = Fields.getFields(line, "|" );
	    badTerm = cols[ termField ];
	    
	    if ( correctField > -1 )
	      correctTerm = cols[ correctField ];
	    Debug.dpr(DF14369, line + "|" + termField + "|" + badTerm );
	  } else {
	    badTerm = line;
	  }
	  
	  candidates = this.find( findOptions, badTerm );
	
	  if ( correctTerm != null ) 
	    benchmark.add( badTerm, correctTerm, candidates );
	  
	  if (( candidates != null ) && (candidates.length > 0)) {
	  
	    for ( int i = 0; i < candidates.length && i < findOptions.getTruncateSize() ; i++ ) {
	      pPrintStream.println( line + "|" + candidates[i].toString() );
	    }
	  } else {
	    pPrintStream.println( line + 
				  "|" + 
				  "|" + 
				  -9999 +
				  "|" + 
				  "|" + 
				  "No Suggestions" 
				  );
	  } 
	  pPrintStream.flush();

	  if ( settings.getBoolean("--reportTime") )
	    stats.report();
	  ctr++;
	} 
	// -------------------------------------------------------
	// free up the candidates used 
	// -------------------------------------------------------
	this.freeCandidates();
	
      } // End while 
      if ( correctField > -1 ) 
	benchmark.reportStats();
    } catch ( Exception e) {
      e.printStackTrace();
      Debug.dexit(DT14368);
      throw new GSpellException ( e.toString() );
    }
      
    Debug.dexit(DT14368);
      
    return( ctr );
    
  } // ***End public Candidate[]  find( InputReader pFileStream )  throws Exception
// ================================================|Public Method Header|====
/**
 * Method find 
 * This method finds documents from the ngram approach, phonetic
 * approach and soon to be others, then combines these, 
 * re-ranks them, and returns them as candidates. 
 * 
 * This method returns null if there are no suggestions.
 *
 * Note: This method uses a hashtable and arrayList that gets re-used within 
 * the GSpell instance. Therefore, if this is called within a static or
 * singleton thread model, within a multi-thread environment, take care to
 * put a syncronize around it. 
 *
 * The freeCandidates() method should be called after you are finished 
 * using/extracting information from the array of Candidate. This 
 * will allow those Candidate objects to be re-used, which is faster
 * and takes up less memory than to have new Candidates instantiated
 * for each query.
 * 
 * @param  String term 
 * @return Candidate[]		
 * 
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public Candidate[]  find( String pTerm ) throws GSpellException
  { 

    Debug.dfname("find");
    Debug.denter(DT14370);

    if ( this.findOptions == null ) 
      this.findOptions = new FindOptions( this.settings ); 

    Candidate[] returnValue = find( this.findOptions , pTerm );

    Debug.dexit(DT14370);
    
    return( returnValue );

   } // ***End public Candidate[]  find( term )  throws Exception
// ================================================|Public Method Header|====
/**
 * Method find 
 * This method finds documents from the ngram approach, phonetic
 * approach and soon to be others, then combines these, 
 * re-ranks them, and returns them as candidates. 
 * 
 * This method returns null if there are no suggestions.
 *
 * The freeCandidates() method should be called after you are finished 
 * using/extracting information from the array of Candidate. This 
 * will allow those Candidate objects to be re-used, which is faster
 * and takes up less memory than to have new Candidates instantiated
 * for each query.
 * 
 * @param  pOptions 
 * @param  term 
 * @return Candidate[]		
 * 
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public Candidate[]  find( FindOptions fOptions, String pTerm ) throws GSpellException
  { 
    Candidate[] returnValue = null;

    Debug.dfname("find:FOptions");
    Debug.denter(DT14370);

    try {

    Object[]      leftMostCandidates = null;
    Object[]     rightMostCandidates = null;
    Object[]         nGramCandidates = null;
    Object[]     metaphoneCandidates = null;
    Object[]        commonCandidates = null;
    Object[]     homophoneCandidates = null;
    Candidate             aCandidate = null;


    SimpleCorpusStatistics corpusFrequencyLookupInstance = null; 

    try {
      Debug.dpr(DF14371,"Looking for the corpusName " + this.corpusName );
      corpusFrequencyLookupInstance = new SimpleCorpusStatistics( this.settings ); 
								  
    } catch (Exception e ) {
      Debug.dpr(DF14371,"Not going to use the corpus stats" );
    }

    if (this.combinedCandidates == null )
      this.combinedCandidates = new Candidates( fOptions.getMaxCandidatesToConsider(),
						corpusFrequencyLookupInstance );
    else
      this.combinedCandidates.clear();
    
    if ( this.someCandidates == null ) 
      this.someCandidates = new Hashtable(  fOptions.getMaxCandidatesToConsider());
    else
      this.someCandidates.clear();
    

      // ----------------------------------------------
      // If we were doing locking ... 
      // Block until we can open a reader 
      //   Once in, we can let other readers in
      //
      // lock.lockRead();
      //
      // ----------------------------------------------

      Debug.dpr(DF14371,"The term is = " + pTerm );
      
      if (( pTerm.trim().length() > 0 )  && (!U.isPunctuation(pTerm)) ){
	

	metaphoneCandidates = phones.find( fOptions, pTerm );

	if (( metaphoneCandidates == null ) ||     
	    ((metaphoneCandidates !=null ) &&       
	     (metaphoneCandidates.length > 0) &&  
	     !((Candidate)metaphoneCandidates[0]).isCorrect()) || 
	    !this.stopWhenFound ) {
	  
	  leftMostCandidates  = this.nGrams.findLeftMost( fOptions, pTerm );
	  

	  if (( leftMostCandidates == null ) || 
	      ((leftMostCandidates != null ) && (leftMostCandidates.length > 0) && !((Candidate)leftMostCandidates[0]).isCorrect())  || 
	      !this.stopWhenFound ) {
	    
	    if ( this.truncateLeft == true ) 
	      rightMostCandidates = this.nGrams.findRightMost( fOptions, pTerm );
	    
	    if (( rightMostCandidates == null ) ||
		((rightMostCandidates != null) && (rightMostCandidates.length > 0) && !((Candidate)rightMostCandidates[0]).isCorrect()) || 
		!this.stopWhenFound ) {

	      nGramCandidates     = this.nGrams.find( fOptions, pTerm );

	      if (( nGramCandidates == null ) ||
		  ((nGramCandidates != null)&& (nGramCandidates.length > 0) && !((Candidate)nGramCandidates[0]).isCorrect()) || 
		  !this.stopWhenFound ) {
		
		if ( mistakes != null ) commonCandidates = mistakes.find( pTerm );
		
		if ( homophones != null ) homophoneCandidates = homophones.find( pTerm );
	      } // end look for common misspellings and homophones 
	    } // end look for metaphones 
	  } // end look for nGrams
	} // end look for right most
	
	// -------------------------------------------------------
	// Cull for homophones 
	// -------------------------------------------------------
	if ( homophoneCandidates != null ) {
	  Debug.dpr(DF14371, "--> There are " + homophoneCandidates.length + " homophone candidates suggested"); 
	  for ( int i = 0; i < homophoneCandidates.length; i++ ) {
	    aCandidate = (Candidate) homophoneCandidates[i];
	    this.someCandidates.put( aCandidate.getName(), " ");
	    this.combinedCandidates.add( aCandidate.getName(), 
					 aCandidate.getEditDistance1000(),
					 aCandidate.getFromMethod());
	  }
	}
	// -------------------------------------------------------
	// Cull for common misspellings
	// -------------------------------------------------------
	if ( commonCandidates != null ) {
	  Debug.dpr(DF14371, "--> There are " + commonCandidates.length + " common misspelling candidates suggested"); 
	  for ( int i = 0; i < commonCandidates.length; i++ ) {
	    aCandidate = (Candidate) commonCandidates[i];
	    if ( !this.someCandidates.containsKey( aCandidate.getName()) ) {
	      this.someCandidates.put( aCandidate.getName(), " ");
	      this.combinedCandidates.add( aCandidate.getName(), 
				      aCandidate.getEditDistance1000(),
				      aCandidate.getFromMethod());
	    }
	  }
	}
	
	// -------------------------------------------------------
	// Cull for the leftmost candidates
	// -------------------------------------------------------
	if ( leftMostCandidates != null ) {
	  Debug.dpr(DF14371, "--> There are " + leftMostCandidates.length  + "leftMost candidates suggested"); 

	  for ( int i = 0; i < leftMostCandidates.length; i++ ) {
	    aCandidate = (Candidate) leftMostCandidates[i];
	    Debug.dpr(DF14371, "  --> Looking to add " + aCandidate.getName() );
	    
	    if ( !this.someCandidates.containsKey( aCandidate.getName()) ) {
	      Debug.dpr(DF14371,  aCandidate.getName() + " is not yet on the list " );
	      this.someCandidates.put( aCandidate.getName(), " ");
	      this.combinedCandidates.add( aCandidate.getName(), 
					   aCandidate.getEditDistance1000(),
					   aCandidate.getFromMethod());

	      if (( this.stopWhenFound ) &&
		  ((Candidate)leftMostCandidates[0]).isCorrect())
		break;
	    }
	  }
	}
	
	// -------------------------------------------------------
	// Cull for the rightMost candidates
	// -------------------------------------------------------
	if ( rightMostCandidates != null ) {
	  Debug.dpr(DF14371, "--> There are " + rightMostCandidates.length  + "rightMost candidates suggested"); 
	  for ( int i = 0; i < rightMostCandidates.length; i++ ) {
	    aCandidate = (Candidate) rightMostCandidates[i];
	    Debug.dpr(DF14371, "  --> Looking to add " + aCandidate.getName() );
	    
	    if ( !this.someCandidates.containsKey( aCandidate.getName()) ) {
	      Debug.dpr(DF14371,  aCandidate.getName() + " is not yet on the list " );
	      this.someCandidates.put( aCandidate.getName(), " ");
	      this.combinedCandidates.add( aCandidate.getName(), 
				      aCandidate.getEditDistance1000(),
				      aCandidate.getFromMethod());

	      if (( this.stopWhenFound ) &&
		  ((Candidate)rightMostCandidates[0]).isCorrect())
		break;
	    }
	  }
	}
	
	
	// -------------------------------------------------------
	// Cull for the ngram candidates
	// -------------------------------------------------------
	if ( nGramCandidates != null ) {
	  Debug.dpr(DF14371, "--> There are " + nGramCandidates.length  + "nGrams candidates suggested"); 
	  for ( int i = 0; i < nGramCandidates.length; i++ ) {
	    aCandidate = (Candidate) nGramCandidates[i];
	    Debug.dpr(DF14371, "  --> Looking to add " + aCandidate.getName() );
	    
	    if ( !this.someCandidates.containsKey( aCandidate.getName()) ) {
	      Debug.dpr(DF14371,  aCandidate.getName() + " is not yet on the list " );
	      this.someCandidates.put( aCandidate.getName(), " ");
	      this.combinedCandidates.add( aCandidate.getName(), 
				      aCandidate.getEditDistance1000(),
				      aCandidate.getFromMethod());

	      if (( this.stopWhenFound ) &&
		  ((Candidate)nGramCandidates[0]).isCorrect())
		break;
	    }
	  }
	}
	
	// -------------------------------------------------------
	// Cull for the metaphoned candidates 
	// -------------------------------------------------------
	if ( metaphoneCandidates != null ) {
	  Debug.dpr(DF14371, "--> There are " + metaphoneCandidates.length + " metaphone candidates suggested"); 
	  for ( int i = 0; i < metaphoneCandidates.length; i++ ) {
	    aCandidate = (Candidate)  metaphoneCandidates[i];
	    Debug.dpr(DF14371, "--> here 3"); 
	    
	    Debug.dpr(DF14371, "-looking for-> " + aCandidate.getName()); 
	    if ( !this.someCandidates.containsKey( aCandidate.getName()) ) {
	      Debug.dpr(DF14371, "- this is added-> " + aCandidate.getName()); 
	      this.someCandidates.put( aCandidate.getName(), " ");
	      this.combinedCandidates.add( aCandidate.getName(), 
					   aCandidate.getEditDistance1000(),
					   aCandidate.getFromMethod());
	      if (( this.stopWhenFound )  &&
		  ((Candidate)metaphoneCandidates[0]).isCorrect())
		break;

	    }
	  }
	}
	
	// -------------------------------------------------------
	// return the combined sorted candidate list 
	// -------------------------------------------------------
	if (this.combinedCandidates.size() > 0 ) {

	  // -------------------------------------------------------
	  // retrieve corpus frequency on the candidates 
	  // -------------------------------------------------------
	  this.combinedCandidates.calculateCorpusFrequencies(); 
	  returnValue = this.combinedCandidates.get( fOptions.getTruncateSize() );
	  Debug.dpr(DF14371, "Found a combined " + returnValue.length );
	} else {
	}


	// this.combinedCandidates.clear(); 
	// this.someCandidates.clear();
	
	nGramCandidates     = null; 
	metaphoneCandidates = null; 
	commonCandidates    = null; 
	homophoneCandidates = null; 
	
      } //End if the term has any length

      if ( this.termsProcessed % 1000 == 0 )
	// System.gc();
      this.termsProcessed++;
      
    } catch ( Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14370);
      throw new GSpellException ( e.toString() ) ;

    } finally {
      // -----------------------------------------------------
      // If we were doing locking, then here is where we would
      // do the unlock.
      // lock.unlock();
      // -----------------------------------------------------
    }
    
      
    Debug.dexit(DT14370);
    
    return( returnValue );

   } // ***End public Candidate[]  find( term )  throws Exception

// ================================================|Public Method Header|====
/**
 * isSpelledCorrect takes a term and returns true if this term is found in 
 * the indexes. whitespace, punctuation, numbers are considered correct.
 * 
 * This method does not need to have candidates free'd after its use. 
 *
 * @param pTerm
 * @return boolean
 * @exception Exception
*/
// ================================================|Public Method Header|====
  public boolean isSpelledCorrect(String pTerm) throws Exception
  {
    
    boolean found = true;

    if (( pTerm.trim().length() > 0 )  && (!U.isPunctuation(pTerm)) && (!U.isNumber(pTerm)) ) { 

      Candidate[] metaphoneCandidates = this.phones.find( pTerm );

      if (( metaphoneCandidates == null ) ||     
	  ((metaphoneCandidates !=null ) &&       
	   (metaphoneCandidates.length > 0) &&  
	   !((Candidate)metaphoneCandidates[0]).isCorrect()) ) {
	found = false;
      }
      this.phones.freeCandidates();
	  
    }

    return ( found );
	  

  } // ***End boolean isSpelledCorrect()  throws Exception

// ================================================|Public Method Header|====
/**
 * freeCandidates returns the candidates in the candidatePool to the free list.
 * This should be called after every find() method, once the candidates returned
 * are no longer needed.
 *
*/
// ================================================|Public Method Header|====
  public void freeCandidates() 
  {
    this.nGrams.freeCandidates();
    this.phones.freeCandidates();

  } // ***End void freeCandidates()  throws Exception

// ================================================|Public Method Header|====
/**
 * Method cleanup writes out remaining indexes, closes files ... 
 * 
 * This method should have a write lock around it, if one is implementing 
 * a locking mechanism. This method writes out the internal caches to
 * the jdbm when in update or write mode.
 * This method then closes the jdbm files.
 *   
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public void cleanup() throws GSpellException
  { 

    Debug.dfname("GSpell:cleanup");
    Debug.denter(DT14372);

    try {
      
      this.busyFlag = true;
      this.settings = nGrams.cleanup();
      phones.cleanup();
      if ( this.mistakes != null )this.mistakes.cleanup();
      if ( this.homophones != null )homophones.cleanup();
      // justTheRight.cleanup();
    
      
      if ( this.mode != GSpell.READ_ONLY ) {
	GSpell.storeConfigSettings(this.settings );
      }
      this.busyFlag = false;
      
    } catch ( Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14372);
      throw new GSpellException ( e.toString() );
    }

    Debug.dexit(DT14372);


   } // ***End public void cleanup()  throws Exception

// ================================================|Public Method Header|====
/**
 * Method close closes the open db's
 * 
 * @exception GSpellException
 * 
*/
// ================================================|Public Method Header|====
public void close() throws GSpellException
  { 

    Debug.dfname("GSpell:close");
    Debug.denter(DT14372);

    try {
      
      this.nGrams.close();
      this.phones.close();
      if ( this.mistakes != null )this.mistakes.close();
      if ( this.homophones != null )homophones.close();
      // justTheRight.close();

      this.nGrams = null; 
      this.phones = null; 
      this.mistakes = null; 
      this.homophones = null; 
      // this.justTheRight = null;
      
    } catch ( Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14372);
      throw new GSpellException ( e.toString() );
    }

    Debug.dexit(DT14372);


   } // ***End public void close()  throws Exception

// ================================================|Public Method Header|====
/**
 * Method flush updates the physical indexes
 * This method should have a write lock around it, if one is implementing 
 * a locking mechanism. This method writes out the internal caches to
 * the jdbm when in update or write mode.
 * 
 * @exception GSpellException
 * 
 * 
*/
// ================================================|Public Method Header|====
public void flush() throws GSpellException
  { 

    Debug.dfname("flush");
    Debug.denter(DT14372);

    try {
      this.setBusy(true);
      this.nGrams.flush();
      this.phones.flush();
      if ( mistakes != null ) 
	mistakes.flush();
      // justTheRight.flush();
    
      if ( this.mode != GSpell.READ_ONLY ) {
	GSpell.storeConfigSettings( this.settings );
      }
      this.setBusy(false);
      
    } catch ( Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14372);
      throw new GSpellException ( e.toString() );
    }

    Debug.dexit(DT14372);


   } // ***End public void cleanup()  throws Exception

// ================================================|Public Method Header|====
/**
 * Method reload closes and re-opens all the files to pick up changes
 * that may have been made by an external process. 
 * 
 * @exception GSpellException
 * 
*/
// ================================================|Public Method Header|====
public void reload() throws GSpellException
  { 

    Debug.dfname("reload");
    Debug.denter(DT14372);

    try {

      Debug.dpr(DF14373,"Closing the dbs");
      this.close();
      
      Debug.dpr(DF14373,"Opening the dbs");
      this.init( this.settings );
      Debug.dpr(DF14373,"done");
      
    } catch ( Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14372);
      throw new GSpellException ( e.toString() );
    }

    Debug.dexit(DT14372);


   } // ***End public void re-load()  throws Exception
// ================================================|Public Method Header|====
/**
 * Method run 
 * 
 * @param String args[]		
 * @return int
 * 
 * 
 * 
*/
// ================================================|Public Method Header|====
public static int run(String args[])
  { 

    Debug.dfname("run");
    Debug.denter(DT210);

      
    int         returnValue = 0;
    String          charset = "ASCII";
    String    inputFileName = null;
    String   outputFileName = null;
    BufferedReader    input = null;
    PrintWriter      output = null;
    GSpell           gspell = null; 
    Stats             stats = new Stats(0);
    int      totalProcessed = 0;
    
      // ----------------------
      // Retrieve these options
      //  --help
      //  --index    
      //  --update   
      //  --find     
      //  --version
      //  --reportTime
      //  --dictionaryName=XXXXX
      //  --dictionaryDir=/UU/KK
      //  --inputFile=YYYYY
      //  --outputFile=ZZZZZ
      //  --truncate=N <------ TRUNCATE_SIZE
      //  --considerNCandidates=N <----- MAX_CANDIDATES
      //  --maxEditDistance=N   <---- MAX_THRESHOLD 
      //  --fieldedText
      //  --termField=X   [ The first field is 1 not 0 ] 
      // ----------------------
      
      // ----------------------------------------------------------
      // If you know the full path to the dictionary/dictionary.cfg,
      // use it instead of the gspell.cfg, and use the GSpell(Settings)
      // constructor instead.
      // ----------------------------------------------------------

    String configPath = "GSpellRegistry.cfg";
    if ( args != null ) {
      for (int i = 0; i < args.length; i++ ) {
	if ( args[i].startsWith("--configPath") == true ) {
	  configPath = args[i].substring(13);
	  System.err.println("The passed in configPath = " + configPath );
	}
      }
    }

    try {

      GlobalBehavior settings = new GlobalBehavior("GSpell",     //Application Name
						   configPath ,  //Generic Registry file
						   "gspell.cfg", //Overriding setting file
						   args);        //Commandline args

      if (settings.getBoolean("--attributions")) {
	Attributions.displayToStdErr( "store.txt");
	Attributions.displayToStdErr( "junit.txt");
      }

      // ---------------------------------------------
      // Turn on utf encoding if called for
      // ---------------------------------------------

      if (settings.getBoolean("--unicode") ) {
	charset="UTF-8";
	System.err.println("Setting the output to utf8"); 
      }
	
      // -----------------------------------------
      // Figure out where the input is coming from 
      // -----------------------------------------
      if (( (inputFileName = settings.getString("--inputFile")) != null ) &&
	  ( !(inputFileName.equals("null") ) )) {
	    input = new BufferedReader(new InputStreamReader( 
		  		     new FileInputStream(inputFileName),
				   charset));

      } else 
	input = new BufferedReader(new InputStreamReader( System.in, charset));
      
      // -----------------------------------------
      // Figure out where the output is going to
      // -----------------------------------------
      if (( (outputFileName = settings.getString("--outputFile" )) != null ) &&
	  ( !(outputFileName.equals("null") ) )) {
	settings.setOutputFile( outputFileName );
      } else {
	settings.setOutputFile( System.out );
      }
      output = settings.getOutputPrintWriter();
      
      if  ( settings.getBoolean("--help") )
	_usage("gspell.hlp" );  

      
	
      else if ( settings.getBoolean("--version") ) 
	System.out.println( "GSPell Version : " + 
			    Version.getVersion("gov/nih/nlm/nls/gspell/history.txt"));
      
      else if ( settings.getBoolean("--cvsVersion") ) 
	System.out.println( "GSPell CVS Version : " + 
			    Version.getCVSVersion("gov/nih/nlm/nls/gspell/history.txt"));

      else if ( settings.getBoolean("--compiledTime") ) 
	System.out.println( "GSPell CompiledTime : " + 
			    Version.getCompiledTime("gov/nih/nlm/nls/gspell/history.txt"));

      else if ( settings.getBoolean("--history") ) 
	System.out.println( "GSPell History: " + 
			    Version.getHistory("gov/nih/nlm/nls/gspell/history.txt"));
	
	
      else if ( settings.getBoolean("--index") ) {
	gspell = new GSpell( settings);

	Debug.dpr(DF211,"The dictionaryname = " + settings.getString("--dictionaryName") );
	totalProcessed = gspell.index( input ); 
	gspell.cleanup();

	try {
	  // -----------------------------------------------------
	  // If you can get to the current lexicon dictionary,
	  // copy over the homophone and common mispelling dictionaries
	  // to make available for this use.
	  // -----------------------------------------------------
	  // -------------------------
	  // Index the homophone data
	  // -------------------------
	  
	  String argsq[] = new String[3];
	  argsq[0] = new String( "--index");
	  argsq[1] = new String( "--dictionaryName=" + settings.getString("--dictionaryName"));
	  argsq[2] = new String("--inputFile=" + settings.getString("--homophonesData"));
	  Homophones.run( argsq );
	  
	  // ---------------------------------
	  // Index the common mispellings data
	  // ---------------------------------
	  argsq[2] = new String("--inputFile=" + settings.getString("--commonData"));
	  CommonMisspellings.run( argsq );
	} catch (Exception e ) {
	  ;;
	}
	  
      }	
      
      else if ( settings.getBoolean("--update") ) {
	Debug.dpr(DF211,"The dictionaryname = " + settings.getString("--dictionaryName") );
	gspell = new GSpell( settings);
	totalProcessed = gspell.update( input ); 
	gspell.cleanup();

      }
	
      else { // if ( settings.getBoolean("--find") ) 
	Debug.dpr(DF211,"The dictionaryname = " + settings.getString("--dictionaryName") );
	gspell = new GSpell( settings);
	totalProcessed = gspell.find( input, output ); 
	gspell.cleanup();
      } 

      if ( settings.getBoolean("--reportTime") )
	stats.finalReport( totalProcessed );
      
    } catch ( Exception e ) {
      e.printStackTrace();
      System.err.println(e.toString() );
      returnValue = -1;
    }

    Debug.dexit(DT210);

    return( returnValue );

   } // ***End public int run(String argv[])

// ================================================|Public Method Header|====
/**
 * Method getVersion 
 * 
 * @return String 
 * 
 * 
*/
// ================================================|Public Method Header|====
public static String  getVersion()
  { 

    String version = Version.getVersion("gov/nih/nlm/nls/gspell/history.txt");

    return( version );

   } // ***End public int run(String argv[])

// ================================================|Public Method Header|====
/**
 * Method isBusy returns whether or not gspell is currently writing out
 *        an index to disk. This method is useful when you are in a singleton
 *        threaded environment, that is, where there is one gspell thread, 
 *        but many threads accessing the gspell thread. When someone pushes
 *        the button to save, and it takes more than a few seconds, you want
 *        to grey out the button until the indexing is done. 
 *        This flag is specific to each instance of GSpell.
 * 
 * @return boolean 
 * 
 * 
*/
// ================================================|Public Method Header|====
public boolean isBusy()
  { 
    return( this.busyFlag );

   } // ***End public int run(String argv[])

// ================================================|Public Method Header|====
/**
 * Method setBusy sets whether or not gspell is currently writing out
 *        an index to disk. This method is useful when you are in a singleton
 *        threaded environment, that is, where there is one gspell thread, 
 *        but many threads accessing the gspell thread. When someone pushes
 *        the button to save, and it takes more than a few seconds, you want
 *        to grey out the button until the indexing is done. 
 *        This flag is specific to each instance of GSpell.
 * 
 * @return boolean 
 * 
 * 
*/
// ================================================|Public Method Header|====
public void setBusy(boolean isBusy)
  { 
    this.busyFlag = isBusy;

   } // ***End public int setBusy()



  // ====================
  // PROTECTED METHODS
  // ====================

/* ================================================|Private Method Header|====
 * Method getHeader reads in the header to be put on each dictionary file 
 * The header file is going to be with the help file in the class directory.
 * 
 * @param pSettings
 * @param pFileName
 * @return 			String
 * @exception
 * 
 * 
 * ================================================|Private Method Header|==== */
  static String getHeader(GlobalBehavior pSettings, String pFileName) throws Exception
  { 

    Debug.dfname("getHeader");
    Debug.denter(DT14580);

    StringTokenizer      st = null;
    boolean           found = false;
    String         fullPath = null;
    File         headerFile = null;
    String            aPath = null;
    StringBuffer buff = new StringBuffer();
    String line = null;

    // -------------------------------------------------------------
    // Determine the path to the classes/gspell/gspell.hdr file
    // -------------------------------------------------------------
    
    InputStream is = null; 
    try {
      is = pSettings.getClassLoader().getResourceAsStream("gov/nih/nlm/nls/gspell/gspell.hdr");
    } catch ( Exception e ) {
      Debug.warning("Not able to get the header template"); 
      Debug.warning("This is only a slight issue because it means that");
      Debug.warning("the SystemClassLoader has been swapped out and this instance");
      Debug.warning("is being run from a different class loader than the System class loader");
    }
      
	
    
    // ----------------------------------------------------
    // We are going to assume that right level is just that.
    // ----------------------------------------------------
    try {
      if ( is != null ) {
	BufferedReader fStream = new BufferedReader(new InputStreamReader( is ));

      
	buff.append("# -----------------------------------------------------------------------------");
	buff.append(U.NL );
	buff.append("#              ");
	buff.append( pFileName ); 
	buff.append(U.NL );
	buff.append("# -----------------------------------------------------------------------------");
	buff.append(U.NL );
	
	while (( line = fStream.readLine() ) != null ) {
	  buff.append( line );
	  buff.append( U.NL );
	}
      
	fStream.close();
      }
    } catch (Exception e ) {
      Debug.warning( "Not able to open the header file: " + e.toString());
      e.printStackTrace();
    }
    
    Debug.dexit(DT14580);
    
    return( buff.toString() );

   } // ***End private String getHeader()

// ================================================|Public Method Header|====
/**
 * Method storeConfigSettings prints the configuration setting from all the
 * methods to a dictionary/dictionary.cfg file.  This method is a static method
 * because each individual algorithm (ngrams, metaphone, common misspellings ...)
 * can be used by itself, and can call this method to create such a file.
 *  
 * @exception Exception
 */
// ================================================|Public Method Header|====
  static void storeConfigSettings( GlobalBehavior pSettings ) throws Exception
  { 
    
    Debug.dfname("storeConfigSettings");
    Debug.denter(DT14526);
    
    // --------------------------------------
    // Open the dictionaryName/dictionary.cfg 
    // --------------------------------------
    
    String aFileName = U.concat( pSettings.getString("--dictionaryDir"), U.FS , 
				 pSettings.getString("--dictionaryName") , U.FS , 
				 pSettings.getString("--dictionaryName") , ".cfg" ); 
    
    PrintWriter _output = new PrintWriter(new BufferedWriter(new FileWriter( aFileName ))); 
    
    String header = GSpell.getHeader( pSettings, aFileName );
    
    _output.println( header );

    // -------------------------------------
    // Print the dictionary specific options
    // -------------------------------------
    _output.println("##### Dictionary Specifig Options"); 
    _output.println("##### (These should only appear in the ");
    _output.println("##### " + aFileName + " config file" );
    _output.println("##### ");
    _output.println("--maxReferences=" + pSettings.getInt("--maxReferences"));
    _output.println("--gdatabaseType=" + pSettings.getInt("--gdatabaseType"));
    _output.println();
    
    _output.println("##### Dictionary Stats - not used after initial indexing ... "); 
    _output.println("--totalDocIdsReferenced=" + pSettings.getInt("--totalDocIdsReferenced"));
    _output.println("--avgReferenced=" + pSettings.getInt("--avgReferenced"));
    _output.println("--totalNumberOfDocuments=" + pSettings.getInt("--totalNumberOfDocuments"));

    Debug.dpr(DF14527,"--maxReferences=" + pSettings.getInt("--maxReferences"));
    Debug.dpr(DF14527,"--totalDocIdsReferenced=" + pSettings.getInt("--totalDocIdsReferenced"));
    Debug.dpr(DF14527,"--avgReferenced=" + pSettings.getInt("--avgReferenced"));
    Debug.dpr(DF14527,"--totalNumberOfDocuments=" + pSettings.getInt("--totalNumberOfDocuments"));
    
    
    _output.close();
    
    Debug.dexit(DT14526);


  } // ***End static void storeConfigSettings()  throws Exception

  // ====================
  // PACKAGE METHODS
  // ====================




  // ====================
  // PRIVATE METHODS
  // ====================

// ================================================|Private Method Header|====
/** 
 * Method init
 * 
 * @param  pSettings Options that incude truncation, max candidates ... 
 *
 * @exception GSpellException
 */
 /* ================================================|Private Method Header|==== */
private synchronized void init ( GlobalBehavior pSettings ) throws GSpellException
  { 

    Debug.dfname("GSpell:init:GlobalBehavior");
    Debug.denter(DT14376);

    if ( pSettings.getBoolean("--help") == false ) {

      // ------------------------------------------
      // If we were to do locking, we'd create a Read/Write Lock instance
      // here when the gspell object is created.
      //
      // lock = new RWLock();
      // ------------------------------------------
      
      try {
	this.settings = pSettings;
	
	// ------------------------------------------
	// figure out what mode we are in
	// ------------------------------------------
	if ( this.settings.getBoolean("--update" ) )
	  this.mode = GSpell.READ_WRITE;
	else if ( this.settings.getBoolean("--index" ) )
	  this.mode = GSpell.WRITE_ONLY;
	else 
	  this.mode = GSpell.READ_ONLY;
	
	
	
	this.dictionaryName = this.settings.getString("--dictionaryName");
	this.dictionaryDir  = this.settings.getString("--dictionaryDir");
	this.corpusName     = this.settings.getString("--corpusName");
	
	// -------------------------------------------------------------------
	// If no dictionaryDir was specified, infer it from the dictionaryName
	// -------------------------------------------------------------------
	File directory = new File ( this.dictionaryDir ); 
	if ( ! directory.exists() ) {
	  directory = new File ( this.dictionaryName); 
	  if ( !directory.exists() ) 
	    throw new GSpellException ( "The --dictionaryDir was not set");
	  else {
	    this.dictionaryDir = directory.getParent(); 
	    this.settings.set( "--dictionaryDir=" +  this.dictionaryDir ); 
	    // -----------------------------------------------------
	    // Set the dictionaryName from the name of the directory
	    // -----------------------------------------------------
	    this.dictionaryName = directory.getName();
	    this.settings.set("--dictionaryName=" +  this.dictionaryName);
	  }
	}
	
	Debug.dpr(DF14377,"The dictionaryname = " + this.dictionaryName  );
	Debug.dpr(DF14377,"The dictionaryDir = " + this.dictionaryDir );
	
      
	
	if ( this.mode == GSpell.CREATE ) {
	  if ( !this.settings.getBoolean("--bagOWordsPlusIndex")) {
	    // ---------------------------------------------
	    // Create the dictionary directory for this dict
	    // read in the config file, because it may already
	    // have been created by another spelling technique
	    // and we don't want to loose those parameters.
	    //
	    // This has prooved to be a bad idea. The berkeley
	    // btrees are not being properly removed upon 
	    // indexing, so the best thing to do here, is
	    // to wipe the directory and start over.
	    // ---------------------------------------------
	    if ( this.dictionaryName != null ) {
	      
	      File dictionaryNameHandle = new File( this.dictionaryDir + U.FS + this.dictionaryName ); 
	      
	      if (( dictionaryNameHandle.exists() ) && ( dictionaryNameHandle.canWrite()) ) {
		Debug.dpr(DF14377, "Moving the contents of the Dictionary dir " + dictionaryNameHandle.toString() ); 
		File tmpFile = new File( this.dictionaryDir + U.FS + this.dictionaryName  + ".bak"); 
		int n = 1;
		while (( tmpFile.exists() ) && ( tmpFile.canWrite()) ) {
		  Debug.dpr(DF14377, "Tmp dir exists, renaming it.");
		  tmpFile = new File( this.dictionaryDir + U.FS + this.dictionaryName  + ".bak" + n); 
		  n++;
		}
	      Debug.dpr(DF14377, "Making a new tmp dir.");
	      
	      Debug.dpr(DF14377, "Renaming the dir to .bak");
	      dictionaryNameHandle.renameTo(tmpFile);
	      Debug.dpr(DF14377, "deleting the .bak dir");
	      tmpFile.delete();
	      dictionaryNameHandle = new File( this.dictionaryDir + U.FS + this.dictionaryName ); 
	      dictionaryNameHandle.mkdirs();
	      
	      } else {
		dictionaryNameHandle.mkdirs();
		Debug.dpr(DF14377, "Making Dictionary dir " + dictionaryNameHandle.toString() );
	      }
	    
	    } else {
	      throw new GSpellException("No dictionary Name was specified");
	      
	    }
	  }

	} else { // End of if this is create mode
	  Debug.dpr(DF14377,"We are not in create mode ");
	  
	  // --------------------------------------------------------------
	  // Read in the dictionary.cfg file to get the dictionary specific
	  // configuration settings
	  // --------------------------------------------------------------
	  this.settings.readConfigFile( U.concat( this.dictionaryDir, U.FS , 
						  this.dictionaryName , U.FS , 
						  this.dictionaryName , ".cfg" )); 
	  Debug.dpr(DF14377,"done");
	}
	
	this.nGrams   = new NGrams( this.settings );
	this.phones   = new MetaphoneRetrieve( this.settings ); 
	
	Debug.dpr(DF14377,"--index = " + this.settings.getBoolean("--index"));
	
	try {
	  if ( this.mode != GSpell.CREATE )
	    this.mistakes = new CommonMisspellings( this.settings );
	} catch (Exception e ) {
	  this.mistakes = null;
	  Debug.dpr(DF14377,"-|-Not using the common misspellings");
	}
	
	try {
	  if ( this.mode != GSpell.CREATE )
	    this.homophones = new Homophones( this.settings );
	} catch (Exception e ) {
	  mistakes = null;
	  Debug.dpr(DF14377,"-|-Not using the Homophones");
	}
	
      } catch (Exception e ) {
	e.printStackTrace();
	Debug.dexit(DT14376);
	throw new GSpellException( e.toString() );
	
      }

    } // End of if help

    // -------------------------------------------------------------
    // Check to see if we should stop when the query term is correct 
    // -------------------------------------------------------------
    this.stopWhenFound =  this.settings.getBoolean("--stopWhenFound");

    // -------------------------------
    // Implement a timeout feature
    // -------------------------------
    try {
    
      if ( this.timerInitialized == false ) {
	if ( this.settings.getBoolean("--timeout") == true ) {
	  
	  System.err.println("Turning on the timeout feature ... ");
	  long hoursToMilliSeconds = 1000 * 60 * 60;
	  
	  double tm = this.settings.getReal("--timeoutInHours");
	  Debug.dpr(DF14377,"timeout (in hours) =  " + tm);

	  long timeoutPeriod = (long) (hoursToMilliSeconds * tm);
	  
	  Debug.dpr(DF14377,"Setting the timeout (in milliSeconds) to  " + timeoutPeriod);
	  this.timer = new Timer();
	  this.timer.schedule( new ReLoadTask( (Object) this ), timeoutPeriod, timeoutPeriod );
	  
	} else {
	  Debug.dpr(DF14377,"Not using the timeout feature ... ");
	}
	
	timerInitialized = true;
      } // end if initialized
    } catch (Exception e ) {
      System.err.println("Not able to kick off the timeout feature " + e.toString());
    }

    // --------------------------------------------------
    // Make the truncateLeft retrieval mechanism optional 
    // --------------------------------------------------
    this.truncateLeft = this.settings.getBoolean("--truncateLeft");

      
    Debug.dexit(DT14376);


   } // ***End private void init ( String pDictionaryName ) throws Exception

// ================================================|Private Method Header|====
/** 
 * Method init
 * 
 * @param  Options pOptions  Options that incude truncation, max candidates ... 
 *
 * @exception GSpellException
 * @deprecated  Use init (GlobalBehaviors instead)
 * 
 */
 /* ================================================|Private Method Header|==== */
private void init ( Options pOptions ) throws GSpellException
  { 

    Debug.dfname("GSpell:init");
    Debug.denter(DT14376);


    // ------------------------------------------
    // If we were to do locking, we'd create a Read/Write Lock instance
    // here when the gspell object is created.
    //
    // lock = new RWLock();
    // ------------------------------------------

    
    try {
      
      this.options = pOptions ;
      this.mode = this.options.getMode();
      this.dictionaryName = this.options.getDictionaryName();
      this.dictionaryDir  = this.options.getDictionaryDir();
      this.corpusName     = this.options.getCorpusName();
      
      
      Debug.dpr(DF14377,"The dictionaryname = " + this.dictionaryName  );
      Debug.dpr(DF14377,"The dictionaryDir = " + this.dictionaryDir );
      
      
      
      if ( this.mode == GSpell.CREATE ) {
	// ---------------------------------------------
	// Create the dictionary directory for this dict
	// Read in the config file, because it may already
	// have been created by another spelling technique
	// and we don't want to loose those parameters.
	// ---------------------------------------------
	if ( this.dictionaryDir != null ) 
	  this.options.makeDir( this.dictionaryDir );
	else
	  this.options.makeDir( this.dictionaryName );
      }
      
      GlobalBehavior settings = new GlobalBehavior("GSpell",            //Application Name
						   "GSpellRegistry.cfg",//Generic Registry file
						   "gspell.cfg",        //Overriding setting file
						   null);              //Commandline args
      
      settings.set("--dictionaryDir=" + this.dictionaryDir );
      settings.set("--dictionaryName=" + this.dictionaryName );
      
      
      this.nGrams   = new NGrams( settings );
      this.phones   = new MetaphoneRetrieve( settings ); 
      
      try {
	if ( this.mode != GSpell.CREATE )
	  this.mistakes = new CommonMisspellings( settings );
      } catch (Exception e ) {
	this.mistakes = null;
	Debug.dpr(DF14377,"-|-Not using the common misspellings");
      }

      try {
	if ( this.mode != GSpell.CREATE )
	  this.homophones = new Homophones( settings );
      } catch (Exception e ) {
	this.homophones = null;
	Debug.dpr(DF14377,"-|-Not using the Homophones");
      }
      
    } catch (Exception e ) {
      e.printStackTrace();
      Debug.dexit(DT14376);
      throw new GSpellException( e.toString() );
      
    }

    Debug.dpr(DF14377,"End of init");
    
      
    Debug.dexit(DT14376);


   } // ***End private void init ( String pDictionaryName ) throws Exception
// ================================================|Main Method|====
/**
 * main
 *
 * @param String args[] 	The command line input, tokenized
 *
 * @return int  0|-1		0 is returned if no problems, -1 is
 * 				is returned if there is a problem.
 *
 *
*/
// ================================================|Main Method|====
  public final static void main( String args[] )
  {

    int returnValue = 0;

    Debug.dfname("main");
    Debug.denter(DT14356);

    
    try {
    run( args );
    } catch ( Exception e ) {
      System.err.println(e.toString() );
    }


    Debug.dexit(DT14356);

    System.exit( returnValue );

   } // ***End main ( String argv[] )


// ================================================|Usage Header |====
/**
 * _usage 
 *
 * @param pHelpFile
*/
// ================================================|Usage Header |====
  private static final void _usage( String pHelpFile)
  {

    Debug.dfname("_usage");
    Debug.denter(DT14358);

    // -------------------------------------------------
    // Find the gspell.hlp from the classes/gspell/ path
    // -------------------------------------------------
    String aFile = "gov/nih/nlm/nls/gspell/" + pHelpFile ;

    InputStream is =  ClassLoader.getSystemClassLoader().getSystemResourceAsStream( aFile);
    
    if ( is != null ) 
      Use.usage( is );
    
    
    Debug.dexit(DT14358);

  } // ***End usage()


  // ====================
  // Public Declarations
  // ====================
  // -------------------------------------------------------------------------
  // These were verified to be the same values as those from lexUtils.dbm.JDBM
  // -------------------------------------------------------------------------
  public final static int WRITE_ONLY  = 1;
  public final static int      CREATE = GSpell.WRITE_ONLY ;
  public final static int READ_WRITE  = 2;
  public final static int READ_ONLY   = 3;

  public final static int DATABASE_TYPE = JDbmFactory.UNCOMMON_JDBM;
  // public final static int DATABASE_TYPE = JDbmFactory.STORE_JDBM;
  // public final static int DATABASE_TYPE = JDbmFactory.SOURCE_FORGE_JDBM;



  // ====================
  // Private Declarations
  // ====================
   private int                      mode = READ_ONLY;
   private String         dictionaryName = null;
   private String          dictionaryDir = null;
   private String             corpusName = null;
   private NGrams                 nGrams = null; 
   private MetaphoneRetrieve      phones = null; 
   private CommonMisspellings   mistakes = null; 
   private Homophones         homophones = null; 
   private Options               options = null;
   private GlobalBehavior       settings = null;
   private FindOptions       findOptions = null; 
   private boolean 	    truncateLeft = true; 
   private int            termsProcessed = 0;
   private boolean         stopWhenFound = false;

   private int     MAX_CANDIDATES = 2000;
   private int      TRUNCATE_SIZE = 20;
   private int     MAX_THRESHHOLD = 5;
  private static final String    APPLICATION_NAME = "GSpell";

  private boolean timerInitialized = false;
  private Timer timer = null;
  private boolean         busyFlag = false;
  // private RWLock               lock = null;

  Candidates combinedCandidates = null; 
  Hashtable      someCandidates = null; 

  // ====================
  // DEBUG FLAGS
  // ====================
     private static final int DT14354 = 14354;         // DT for Constructor()
     private static final int DF14355 = 14355;         // DF for Constructor()
     private static final int DT14356 = 14356;         // DT for main()
     private static final int DF14357 = 14357;         // DF for main()
     private static final int DT14358 = 14358;         // DT for usage()
     private static final int DF14359 = 14359;         // DF for usage()
     private static final int DT14360 = 14360;         // DT for index()
     private static final int DF14361 = 14361;         // DF for index()
     private static final int DT14362 = 14362;         // DT for index()
     private static final int DF14363 = 14363;         // DF for index()
     private static final int DT14364 = 14364;         // DT for update()
     private static final int DF14365 = 14365;         // DF for update()
     private static final int DT14366 = 14366;         // DT for update()
     private static final int DF14367 = 14367;         // DF for update()
     private static final int DT14368 = 14368;         // DT for find()
     private static final int DF14369 = 14369;         // DF for find()
     private static final int DT14370 = 14370;         // DT for find()
     private static final int DF14371 = 14371;         // DF for find()
     private static final int DT14372 = 14372;         // DT for cleanup()
     private static final int DF14373 = 14373;         // DF for cleanup()
     private static final int DT14374 = 14374;         // DT for export()
     private static final int DF14375 = 14375;         // DF for export()
     private static final int DT14376 = 14376;         // DT for init ()
     private static final int DF14377 = 14377;         // DF for init ()
     private static final int DT14580 = 14580;         // DT for getHeader()
     private static final int DF14581 = 14581;         // DF for getHeader()
     private static final int DT14526 = 14526;         // DT for storeConfigSettings()
     private static final int DF14527 = 14527;         // DF for storeConfigSettings()
     private static final int DT210 = 210;         // DT for run()
     private static final int DF211 = 211;         // DF for run()

  // ====================
  // Inner Class 
  // ====================
  public class ReLoadTask extends TimerTask {

    Object gspellInstance = null;
    // ====================
    // CONSTRUCTOR
    // ====================
    public ReLoadTask( Object pGSpellInstance ) throws Exception {
      
      this.gspellInstance = pGSpellInstance; 
      
    }

    public void run() {
      System.err.println("About to reload");
      try {
	((GSpell) this.gspellInstance).reload();
      } catch ( Exception e ) {
	System.err.println("Not able to reload the app " + e.toString());
      }
      System.err.println("Reloaded");
    } 
  } // End of inner class ReadLoadTask
    


} // End of the Class GSpell

