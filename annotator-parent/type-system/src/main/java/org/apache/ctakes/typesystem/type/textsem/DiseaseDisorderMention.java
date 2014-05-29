

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.ctakes.typesystem.type.relation.ManifestationOfTextRelation;
import org.apache.ctakes.typesystem.type.relation.ManagesTreatsTextRelation;
import org.apache.ctakes.typesystem.type.relation.LocationOfTextRelation;
import org.apache.ctakes.typesystem.type.relation.ComplicatesDisruptsTextRelation;
import org.apache.ctakes.typesystem.type.relation.DegreeOfTextRelation;
import org.apache.ctakes.typesystem.type.relation.TemporalTextRelation;


/** A text string that refers to a (DiseaseDisorder) Event.
				This is from the UMLS semantic group of Disorders (except that Sign
				and Symptom types are separate). Based on generic Clinical Element
				Models (CEMs)
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class DiseaseDisorderMention extends EventMention {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DiseaseDisorderMention.class);
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
  protected DiseaseDisorderMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DiseaseDisorderMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DiseaseDisorderMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DiseaseDisorderMention(JCas jcas, int begin, int end) {
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
  //* Feature: alleviatingFactor

  /** getter for alleviatingFactor - gets 
   * @generated */
  public ManagesTreatsTextRelation getAlleviatingFactor() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_alleviatingFactor == null)
      jcasType.jcas.throwFeatMissing("alleviatingFactor", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (ManagesTreatsTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_alleviatingFactor)));}
    
  /** setter for alleviatingFactor - sets  
   * @generated */
  public void setAlleviatingFactor(ManagesTreatsTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_alleviatingFactor == null)
      jcasType.jcas.throwFeatMissing("alleviatingFactor", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_alleviatingFactor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: associatedSignSymptom

  /** getter for associatedSignSymptom - gets 
   * @generated */
  public ManifestationOfTextRelation getAssociatedSignSymptom() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_associatedSignSymptom == null)
      jcasType.jcas.throwFeatMissing("associatedSignSymptom", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (ManifestationOfTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_associatedSignSymptom)));}
    
  /** setter for associatedSignSymptom - sets  
   * @generated */
  public void setAssociatedSignSymptom(ManifestationOfTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_associatedSignSymptom == null)
      jcasType.jcas.throwFeatMissing("associatedSignSymptom", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_associatedSignSymptom, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: bodyLaterality

  /** getter for bodyLaterality - gets 
   * @generated */
  public BodyLateralityModifier getBodyLaterality() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_bodyLaterality == null)
      jcasType.jcas.throwFeatMissing("bodyLaterality", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (BodyLateralityModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_bodyLaterality)));}
    
  /** setter for bodyLaterality - sets  
   * @generated */
  public void setBodyLaterality(BodyLateralityModifier v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_bodyLaterality == null)
      jcasType.jcas.throwFeatMissing("bodyLaterality", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_bodyLaterality, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: bodySide

  /** getter for bodySide - gets 
   * @generated */
  public BodySideModifier getBodySide() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_bodySide == null)
      jcasType.jcas.throwFeatMissing("bodySide", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (BodySideModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_bodySide)));}
    
  /** setter for bodySide - sets  
   * @generated */
  public void setBodySide(BodySideModifier v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_bodySide == null)
      jcasType.jcas.throwFeatMissing("bodySide", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_bodySide, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: bodyLocation

  /** getter for bodyLocation - gets 
   * @generated */
  public LocationOfTextRelation getBodyLocation() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_bodyLocation == null)
      jcasType.jcas.throwFeatMissing("bodyLocation", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (LocationOfTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_bodyLocation)));}
    
  /** setter for bodyLocation - sets  
   * @generated */
  public void setBodyLocation(LocationOfTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_bodyLocation == null)
      jcasType.jcas.throwFeatMissing("bodyLocation", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_bodyLocation, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: course

  /** getter for course - gets 
   * @generated */
  public DegreeOfTextRelation getCourse() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_course == null)
      jcasType.jcas.throwFeatMissing("course", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (DegreeOfTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_course)));}
    
  /** setter for course - sets  
   * @generated */
  public void setCourse(DegreeOfTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_course == null)
      jcasType.jcas.throwFeatMissing("course", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_course, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: duration

  /** getter for duration - gets 
   * @generated */
  public TemporalTextRelation getDuration() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_duration == null)
      jcasType.jcas.throwFeatMissing("duration", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (TemporalTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_duration)));}
    
  /** setter for duration - sets  
   * @generated */
  public void setDuration(TemporalTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_duration == null)
      jcasType.jcas.throwFeatMissing("duration", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_duration, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: endTime

  /** getter for endTime - gets 
   * @generated */
  public TimeMention getEndTime() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_endTime == null)
      jcasType.jcas.throwFeatMissing("endTime", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (TimeMention)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_endTime)));}
    
  /** setter for endTime - sets  
   * @generated */
  public void setEndTime(TimeMention v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_endTime == null)
      jcasType.jcas.throwFeatMissing("endTime", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_endTime, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: exacerbatingFactor

  /** getter for exacerbatingFactor - gets 
   * @generated */
  public ComplicatesDisruptsTextRelation getExacerbatingFactor() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_exacerbatingFactor == null)
      jcasType.jcas.throwFeatMissing("exacerbatingFactor", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (ComplicatesDisruptsTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_exacerbatingFactor)));}
    
  /** setter for exacerbatingFactor - sets  
   * @generated */
  public void setExacerbatingFactor(ComplicatesDisruptsTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_exacerbatingFactor == null)
      jcasType.jcas.throwFeatMissing("exacerbatingFactor", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_exacerbatingFactor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: startTime

  /** getter for startTime - gets 
   * @generated */
  public TimeMention getStartTime() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (TimeMention)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_startTime)));}
    
  /** setter for startTime - sets  
   * @generated */
  public void setStartTime(TimeMention v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_startTime, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: relativeTemporalContext

  /** getter for relativeTemporalContext - gets 
   * @generated */
  public TemporalTextRelation getRelativeTemporalContext() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_relativeTemporalContext == null)
      jcasType.jcas.throwFeatMissing("relativeTemporalContext", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (TemporalTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_relativeTemporalContext)));}
    
  /** setter for relativeTemporalContext - sets  
   * @generated */
  public void setRelativeTemporalContext(TemporalTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_relativeTemporalContext == null)
      jcasType.jcas.throwFeatMissing("relativeTemporalContext", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_relativeTemporalContext, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: severity

  /** getter for severity - gets 
   * @generated */
  public DegreeOfTextRelation getSeverity() {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_severity == null)
      jcasType.jcas.throwFeatMissing("severity", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    return (DegreeOfTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_severity)));}
    
  /** setter for severity - sets  
   * @generated */
  public void setSeverity(DegreeOfTextRelation v) {
    if (DiseaseDisorderMention_Type.featOkTst && ((DiseaseDisorderMention_Type)jcasType).casFeat_severity == null)
      jcasType.jcas.throwFeatMissing("severity", "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((DiseaseDisorderMention_Type)jcasType).casFeatCode_severity, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    