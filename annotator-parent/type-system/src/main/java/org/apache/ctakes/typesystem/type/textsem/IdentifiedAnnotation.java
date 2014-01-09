

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.ctakes.typesystem.type.refsem.OntologyConcept;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.TOP;


/** Any span of text that has been discovered or flagged for
				some reason, such as a Named Entity. Allows for mapping to an
				ontology. Generalized from cTAKES:
				org.apache.ctakes.typesystem.type.IdentifiedAnnotation.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class IdentifiedAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(IdentifiedAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected IdentifiedAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public IdentifiedAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public IdentifiedAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public IdentifiedAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated */
  public int getId() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(int v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: ontologyConceptArr

  /** getter for ontologyConceptArr - gets 
   * @generated */
  public FSArray getOntologyConceptArr() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_ontologyConceptArr == null)
      jcasType.jcas.throwFeatMissing("ontologyConceptArr", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_ontologyConceptArr)));}
    
  /** setter for ontologyConceptArr - sets  
   * @generated */
  public void setOntologyConceptArr(FSArray v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_ontologyConceptArr == null)
      jcasType.jcas.throwFeatMissing("ontologyConceptArr", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_ontologyConceptArr, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for ontologyConceptArr - gets an indexed value - 
   * @generated */
  public OntologyConcept getOntologyConceptArr(int i) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_ontologyConceptArr == null)
      jcasType.jcas.throwFeatMissing("ontologyConceptArr", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_ontologyConceptArr), i);
    return (OntologyConcept)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_ontologyConceptArr), i)));}

  /** indexed setter for ontologyConceptArr - sets an indexed value - 
   * @generated */
  public void setOntologyConceptArr(int i, OntologyConcept v) { 
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_ontologyConceptArr == null)
      jcasType.jcas.throwFeatMissing("ontologyConceptArr", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_ontologyConceptArr), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_ontologyConceptArr), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: typeID

  /** getter for typeID - gets The type of named entity (e.g. drug, disorder, ...)
   * @generated */
  public int getTypeID() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_typeID == null)
      jcasType.jcas.throwFeatMissing("typeID", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_typeID);}
    
  /** setter for typeID - sets The type of named entity (e.g. drug, disorder, ...) 
   * @generated */
  public void setTypeID(int v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_typeID == null)
      jcasType.jcas.throwFeatMissing("typeID", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_typeID, v);}    
   
    
  //*--------------*
  //* Feature: segmentID

  /** getter for segmentID - gets 
   * @generated */
  public String getSegmentID() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_segmentID == null)
      jcasType.jcas.throwFeatMissing("segmentID", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_segmentID);}
    
  /** setter for segmentID - sets  
   * @generated */
  public void setSegmentID(String v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_segmentID == null)
      jcasType.jcas.throwFeatMissing("segmentID", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_segmentID, v);}    
   
    
  //*--------------*
  //* Feature: sentenceID

  /** getter for sentenceID - gets contains the sentence id of the sentence that contains
						the NE's text span
   * @generated */
  public String getSentenceID() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_sentenceID == null)
      jcasType.jcas.throwFeatMissing("sentenceID", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_sentenceID);}
    
  /** setter for sentenceID - sets contains the sentence id of the sentence that contains
						the NE's text span 
   * @generated */
  public void setSentenceID(String v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_sentenceID == null)
      jcasType.jcas.throwFeatMissing("sentenceID", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_sentenceID, v);}    
   
    
  //*--------------*
  //* Feature: discoveryTechnique

  /** getter for discoveryTechnique - gets 
   * @generated */
  public int getDiscoveryTechnique() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_discoveryTechnique == null)
      jcasType.jcas.throwFeatMissing("discoveryTechnique", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_discoveryTechnique);}
    
  /** setter for discoveryTechnique - sets  
   * @generated */
  public void setDiscoveryTechnique(int v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_discoveryTechnique == null)
      jcasType.jcas.throwFeatMissing("discoveryTechnique", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_discoveryTechnique, v);}    
   
    
  //*--------------*
  //* Feature: confidence

  /** getter for confidence - gets The confidence of the annotation.
   * @generated */
  public float getConfidence() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_confidence == null)
      jcasType.jcas.throwFeatMissing("confidence", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getFloatValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_confidence);}
    
  /** setter for confidence - sets The confidence of the annotation. 
   * @generated */
  public void setConfidence(float v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_confidence == null)
      jcasType.jcas.throwFeatMissing("confidence", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setFloatValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_confidence, v);}    
   
    
  //*--------------*
  //* Feature: polarity

  /** getter for polarity - gets 
   * @generated */
  public int getPolarity() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_polarity == null)
      jcasType.jcas.throwFeatMissing("polarity", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_polarity);}
    
  /** setter for polarity - sets  
   * @generated */
  public void setPolarity(int v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_polarity == null)
      jcasType.jcas.throwFeatMissing("polarity", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_polarity, v);}    
   
    
  //*--------------*
  //* Feature: uncertainty

  /** getter for uncertainty - gets 
   * @generated */
  public int getUncertainty() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_uncertainty == null)
      jcasType.jcas.throwFeatMissing("uncertainty", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_uncertainty);}
    
  /** setter for uncertainty - sets  
   * @generated */
  public void setUncertainty(int v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_uncertainty == null)
      jcasType.jcas.throwFeatMissing("uncertainty", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_uncertainty, v);}    
   
    
  //*--------------*
  //* Feature: conditional

  /** getter for conditional - gets 
   * @generated */
  public boolean getConditional() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_conditional == null)
      jcasType.jcas.throwFeatMissing("conditional", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_conditional);}
    
  /** setter for conditional - sets  
   * @generated */
  public void setConditional(boolean v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_conditional == null)
      jcasType.jcas.throwFeatMissing("conditional", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_conditional, v);}    
   
    
  //*--------------*
  //* Feature: generic

  /** getter for generic - gets 
   * @generated */
  public boolean getGeneric() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_generic == null)
      jcasType.jcas.throwFeatMissing("generic", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_generic);}
    
  /** setter for generic - sets  
   * @generated */
  public void setGeneric(boolean v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_generic == null)
      jcasType.jcas.throwFeatMissing("generic", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_generic, v);}    
   
    
  //*--------------*
  //* Feature: subject

  /** getter for subject - gets 
   * @generated */
  public String getSubject() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_subject == null)
      jcasType.jcas.throwFeatMissing("subject", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_subject);}
    
  /** setter for subject - sets  
   * @generated */
  public void setSubject(String v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_subject == null)
      jcasType.jcas.throwFeatMissing("subject", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_subject, v);}    
   
    
  //*--------------*
  //* Feature: historyOf

  /** getter for historyOf - gets 
   * @generated */
  public int getHistoryOf() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_historyOf == null)
      jcasType.jcas.throwFeatMissing("historyOf", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_historyOf);}
    
  /** setter for historyOf - sets  
   * @generated */
  public void setHistoryOf(int v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_historyOf == null)
      jcasType.jcas.throwFeatMissing("historyOf", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setIntValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_historyOf, v);}    
   
    
  //*--------------*
  //* Feature: originalText

  /** getter for originalText - gets The covered text of the span or the disjoint spans that resulted in the creation of this IdentifiedAnnotation. If the covered text is from disjoint spans, they are separated by a delimeter.
   * @generated */
  public FSArray getOriginalText() {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_originalText)));}
    
  /** setter for originalText - sets The covered text of the span or the disjoint spans that resulted in the creation of this IdentifiedAnnotation. If the covered text is from disjoint spans, they are separated by a delimeter. 
   * @generated */
  public void setOriginalText(FSArray v) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_originalText, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for originalText - gets an indexed value - The covered text of the span or the disjoint spans that resulted in the creation of this IdentifiedAnnotation. If the covered text is from disjoint spans, they are separated by a delimeter.
   * @generated */
  public TOP getOriginalText(int i) {
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_originalText), i);
    return (TOP)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_originalText), i)));}

  /** indexed setter for originalText - sets an indexed value - The covered text of the span or the disjoint spans that resulted in the creation of this IdentifiedAnnotation. If the covered text is from disjoint spans, they are separated by a delimeter.
   * @generated */
  public void setOriginalText(int i, TOP v) { 
    if (IdentifiedAnnotation_Type.featOkTst && ((IdentifiedAnnotation_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_originalText), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((IdentifiedAnnotation_Type)jcasType).casFeatCode_originalText), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    