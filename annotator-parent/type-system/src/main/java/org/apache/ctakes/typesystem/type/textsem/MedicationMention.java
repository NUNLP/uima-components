

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.ctakes.typesystem.type.relation.TemporalTextRelation;


/** A text string that refers to a (Medication) Event. This
				is an Event from the UMLS semantic group of Chemicals and Drugs,
				pruned by RxNORM source. Based on generic Clinical Element Models
				(CEMs)
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class MedicationMention extends EventMention {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MedicationMention.class);
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
  protected MedicationMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MedicationMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MedicationMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MedicationMention(JCas jcas, int begin, int end) {
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
  //* Feature: medicationAllergy

  /** getter for medicationAllergy - gets 
   * @generated */
  public MedicationAllergyModifier getMedicationAllergy() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationAllergy == null)
      jcasType.jcas.throwFeatMissing("medicationAllergy", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationAllergyModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationAllergy)));}
    
  /** setter for medicationAllergy - sets  
   * @generated */
  public void setMedicationAllergy(MedicationAllergyModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationAllergy == null)
      jcasType.jcas.throwFeatMissing("medicationAllergy", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationAllergy, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationFrequency

  /** getter for medicationFrequency - gets 
   * @generated */
  public MedicationFrequencyModifier getMedicationFrequency() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationFrequency == null)
      jcasType.jcas.throwFeatMissing("medicationFrequency", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationFrequencyModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationFrequency)));}
    
  /** setter for medicationFrequency - sets  
   * @generated */
  public void setMedicationFrequency(MedicationFrequencyModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationFrequency == null)
      jcasType.jcas.throwFeatMissing("medicationFrequency", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationFrequency, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationDuration

  /** getter for medicationDuration - gets 
   * @generated */
  public MedicationDurationModifier getMedicationDuration() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationDuration == null)
      jcasType.jcas.throwFeatMissing("medicationDuration", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationDurationModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationDuration)));}
    
  /** setter for medicationDuration - sets  
   * @generated */
  public void setMedicationDuration(MedicationDurationModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationDuration == null)
      jcasType.jcas.throwFeatMissing("medicationDuration", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationDuration, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationRoute

  /** getter for medicationRoute - gets 
   * @generated */
  public MedicationRouteModifier getMedicationRoute() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationRoute == null)
      jcasType.jcas.throwFeatMissing("medicationRoute", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationRouteModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationRoute)));}
    
  /** setter for medicationRoute - sets  
   * @generated */
  public void setMedicationRoute(MedicationRouteModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationRoute == null)
      jcasType.jcas.throwFeatMissing("medicationRoute", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationRoute, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationStatusChange

  /** getter for medicationStatusChange - gets 
   * @generated */
  public MedicationStatusChangeModifier getMedicationStatusChange() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationStatusChange == null)
      jcasType.jcas.throwFeatMissing("medicationStatusChange", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationStatusChangeModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationStatusChange)));}
    
  /** setter for medicationStatusChange - sets  
   * @generated */
  public void setMedicationStatusChange(MedicationStatusChangeModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationStatusChange == null)
      jcasType.jcas.throwFeatMissing("medicationStatusChange", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationStatusChange, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationDosage

  /** getter for medicationDosage - gets 
   * @generated */
  public MedicationDosageModifier getMedicationDosage() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationDosage == null)
      jcasType.jcas.throwFeatMissing("medicationDosage", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationDosageModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationDosage)));}
    
  /** setter for medicationDosage - sets  
   * @generated */
  public void setMedicationDosage(MedicationDosageModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationDosage == null)
      jcasType.jcas.throwFeatMissing("medicationDosage", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationDosage, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationStrength

  /** getter for medicationStrength - gets 
   * @generated */
  public MedicationStrengthModifier getMedicationStrength() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationStrength == null)
      jcasType.jcas.throwFeatMissing("medicationStrength", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationStrengthModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationStrength)));}
    
  /** setter for medicationStrength - sets  
   * @generated */
  public void setMedicationStrength(MedicationStrengthModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationStrength == null)
      jcasType.jcas.throwFeatMissing("medicationStrength", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationStrength, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: medicationForm

  /** getter for medicationForm - gets 
   * @generated */
  public MedicationFormModifier getMedicationForm() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationForm == null)
      jcasType.jcas.throwFeatMissing("medicationForm", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (MedicationFormModifier)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationForm)));}
    
  /** setter for medicationForm - sets  
   * @generated */
  public void setMedicationForm(MedicationFormModifier v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_medicationForm == null)
      jcasType.jcas.throwFeatMissing("medicationForm", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_medicationForm, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: startDate

  /** getter for startDate - gets 
   * @generated */
  public TimeMention getStartDate() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_startDate == null)
      jcasType.jcas.throwFeatMissing("startDate", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (TimeMention)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_startDate)));}
    
  /** setter for startDate - sets  
   * @generated */
  public void setStartDate(TimeMention v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_startDate == null)
      jcasType.jcas.throwFeatMissing("startDate", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_startDate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: endDate

  /** getter for endDate - gets 
   * @generated */
  public TimeMention getEndDate() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_endDate == null)
      jcasType.jcas.throwFeatMissing("endDate", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (TimeMention)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_endDate)));}
    
  /** setter for endDate - sets  
   * @generated */
  public void setEndDate(TimeMention v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_endDate == null)
      jcasType.jcas.throwFeatMissing("endDate", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_endDate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: relativeTemporalContext

  /** getter for relativeTemporalContext - gets 
   * @generated */
  public TemporalTextRelation getRelativeTemporalContext() {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_relativeTemporalContext == null)
      jcasType.jcas.throwFeatMissing("relativeTemporalContext", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    return (TemporalTextRelation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_relativeTemporalContext)));}
    
  /** setter for relativeTemporalContext - sets  
   * @generated */
  public void setRelativeTemporalContext(TemporalTextRelation v) {
    if (MedicationMention_Type.featOkTst && ((MedicationMention_Type)jcasType).casFeat_relativeTemporalContext == null)
      jcasType.jcas.throwFeatMissing("relativeTemporalContext", "org.apache.ctakes.typesystem.type.textsem.MedicationMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((MedicationMention_Type)jcasType).casFeatCode_relativeTemporalContext, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    