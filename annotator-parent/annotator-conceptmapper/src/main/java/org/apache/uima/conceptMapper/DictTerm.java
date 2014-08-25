

/* First created by JCasGen Sat Dec 22 14:05:15 EST 2007 */
package org.apache.uima.conceptMapper;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.FSArray;


/** Annotation for dictionary lookup matches
 * Updated by JCasGen Mon Aug 25 12:28:12 CDT 2014
 * XML source: C:/WKT/git/uima-components/annotator-parent/annotator-conceptmapper/src/test/resources/descriptors/primitive/DictTerm.xml
 * @generated */
public class DictTerm extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(DictTerm.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected DictTerm() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DictTerm(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DictTerm(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DictTerm(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  *
   * @generated modifiable 
   */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: DictCanon

  /** getter for DictCanon - gets canonical form
   * @generated
   * @return value of the feature 
   */
  public String getDictCanon() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_DictCanon == null)
      jcasType.jcas.throwFeatMissing("DictCanon", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_DictCanon);}
    
  /** setter for DictCanon - sets canonical form 
   * @generated
   * @param v value to set into the feature 
   */
  public void setDictCanon(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_DictCanon == null)
      jcasType.jcas.throwFeatMissing("DictCanon", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_DictCanon, v);}    
   
    
  //*--------------*
  //* Feature: enclosingSpan

  /** getter for enclosingSpan - gets span that this NoTerm is contained within (i.e. its sentence)
   * @generated
   * @return value of the feature 
   */
  public Annotation getEnclosingSpan() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_enclosingSpan == null)
      jcasType.jcas.throwFeatMissing("enclosingSpan", "org.apache.uima.conceptMapper.DictTerm");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_enclosingSpan)));}
    
  /** setter for enclosingSpan - sets span that this NoTerm is contained within (i.e. its sentence) 
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnclosingSpan(Annotation v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_enclosingSpan == null)
      jcasType.jcas.throwFeatMissing("enclosingSpan", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_enclosingSpan, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: POS

  /** getter for POS - gets Part of speech
   * @generated
   * @return value of the feature 
   */
  public String getPOS() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_POS);}
    
  /** setter for POS - sets Part of speech 
   * @generated
   * @param v value to set into the feature 
   */
  public void setPOS(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_POS, v);}    
   
    
  //*--------------*
  //* Feature: matchedText

  /** getter for matchedText - gets 
   * @generated
   * @return value of the feature 
   */
  public String getMatchedText() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedText == null)
      jcasType.jcas.throwFeatMissing("matchedText", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedText);}
    
  /** setter for matchedText - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMatchedText(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedText == null)
      jcasType.jcas.throwFeatMissing("matchedText", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedText, v);}    
   
    
  //*--------------*
  //* Feature: key

  /** getter for key - gets 
   * @generated
   * @return value of the feature 
   */
  public String getKey() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_key);}
    
  /** setter for key - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKey(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_key, v);}    
   
    
  //*--------------*
  //* Feature: parent

  /** getter for parent - gets 
   * @generated
   * @return value of the feature 
   */
  public String getParent() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_parent == null)
      jcasType.jcas.throwFeatMissing("parent", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_parent);}
    
  /** setter for parent - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParent(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_parent == null)
      jcasType.jcas.throwFeatMissing("parent", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_parent, v);}    
   
    
  //*--------------*
  //* Feature: matchedTokens

  /** getter for matchedTokens - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getMatchedTokens() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens)));}
    
  /** setter for matchedTokens - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMatchedTokens(FSArray v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for matchedTokens - gets an indexed value - 
   * @generated */
  public TOP getMatchedTokens(int i) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i);
    return (TOP)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i)));}

  /** indexed setter for matchedTokens - sets an indexed value - 
   * @generated */
  public void setMatchedTokens(int i, TOP v) { 
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_matchedTokens == null)
      jcasType.jcas.throwFeatMissing("matchedTokens", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_matchedTokens), i, jcasType.ll_cas.ll_getFSRef(v));}
    //*--------------*
  //* Feature: AttributeType

  /** getter for AttributeType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAttributeType() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_AttributeType == null)
      jcasType.jcas.throwFeatMissing("AttributeType", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_AttributeType);}
    
  /** setter for AttributeType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAttributeType(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_AttributeType == null)
      jcasType.jcas.throwFeatMissing("AttributeType", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_AttributeType, v);}    
   
    
  //*--------------*
  //* Feature: AttributeValue

  /** getter for AttributeValue - gets idco code, stage level, etc.
   * @generated
   * @return value of the feature 
   */
  public String getAttributeValue() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_AttributeValue == null)
      jcasType.jcas.throwFeatMissing("AttributeValue", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_AttributeValue);}
    
  /** setter for AttributeValue - sets idco code, stage level, etc. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setAttributeValue(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_AttributeValue == null)
      jcasType.jcas.throwFeatMissing("AttributeValue", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_AttributeValue, v);}    
   
    
  //*--------------*
  //* Feature: SemClass

  /** getter for SemClass - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSemClass() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_SemClass == null)
      jcasType.jcas.throwFeatMissing("SemClass", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_SemClass);}
    
  /** setter for SemClass - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSemClass(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_SemClass == null)
      jcasType.jcas.throwFeatMissing("SemClass", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_SemClass, v);}    
   
    
  //*--------------*
  //* Feature: marked

  /** getter for marked - gets annotation to be marked based on some criteria
   * @generated
   * @return value of the feature 
   */
  public int getMarked() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_marked == null)
      jcasType.jcas.throwFeatMissing("marked", "org.apache.uima.conceptMapper.DictTerm");
    return jcasType.ll_cas.ll_getIntValue(addr, ((DictTerm_Type)jcasType).casFeatCode_marked);}
    
  /** setter for marked - sets annotation to be marked based on some criteria 
   * @generated
   * @param v value to set into the feature 
   */
  public void setMarked(int v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_marked == null)
      jcasType.jcas.throwFeatMissing("marked", "org.apache.uima.conceptMapper.DictTerm");
    jcasType.ll_cas.ll_setIntValue(addr, ((DictTerm_Type)jcasType).casFeatCode_marked, v);}    
   
    
}

    