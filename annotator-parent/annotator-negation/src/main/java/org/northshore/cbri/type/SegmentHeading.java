

/* First created by JCasGen Fri Jan 03 13:41:43 CST 2014 */
package org.northshore.cbri.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jan 03 13:41:43 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/annotator-negation/src/main/resources/descriptors/NegExTypeSystem.xml
 * @generated */
public class SegmentHeading extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SegmentHeading.class);
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
  protected SegmentHeading() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SegmentHeading(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SegmentHeading(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SegmentHeading(JCas jcas, int begin, int end) {
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
  public String getId() {
    if (SegmentHeading_Type.featOkTst && ((SegmentHeading_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "org.northshore.cbri.type.SegmentHeading");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SegmentHeading_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(String v) {
    if (SegmentHeading_Type.featOkTst && ((SegmentHeading_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "org.northshore.cbri.type.SegmentHeading");
    jcasType.ll_cas.ll_setStringValue(addr, ((SegmentHeading_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: preferredText

  /** getter for preferredText - gets preferredText is the normalized/resolved section name.  Normally, this is populated by the Sectionizer and would contain the HL7/CCDA section name.
   * @generated */
  public String getPreferredText() {
    if (SegmentHeading_Type.featOkTst && ((SegmentHeading_Type)jcasType).casFeat_preferredText == null)
      jcasType.jcas.throwFeatMissing("preferredText", "org.northshore.cbri.type.SegmentHeading");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SegmentHeading_Type)jcasType).casFeatCode_preferredText);}
    
  /** setter for preferredText - sets preferredText is the normalized/resolved section name.  Normally, this is populated by the Sectionizer and would contain the HL7/CCDA section name. 
   * @generated */
  public void setPreferredText(String v) {
    if (SegmentHeading_Type.featOkTst && ((SegmentHeading_Type)jcasType).casFeat_preferredText == null)
      jcasType.jcas.throwFeatMissing("preferredText", "org.northshore.cbri.type.SegmentHeading");
    jcasType.ll_cas.ll_setStringValue(addr, ((SegmentHeading_Type)jcasType).casFeatCode_preferredText, v);}    
  }

    