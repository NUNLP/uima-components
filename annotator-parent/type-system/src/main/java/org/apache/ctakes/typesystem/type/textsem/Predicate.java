

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** Predicates are typically verbs and may participate in
				SemanticRoleRelations. Follows PropBank standards with a few
				clinical additions.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class Predicate extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Predicate.class);
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
  protected Predicate() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Predicate(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Predicate(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Predicate(JCas jcas, int begin, int end) {
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
  //* Feature: relations

  /** getter for relations - gets 
   * @generated */
  public FSList getRelations() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_relations == null)
      jcasType.jcas.throwFeatMissing("relations", "org.apache.ctakes.typesystem.type.textsem.Predicate");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_relations)));}
    
  /** setter for relations - sets  
   * @generated */
  public void setRelations(FSList v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_relations == null)
      jcasType.jcas.throwFeatMissing("relations", "org.apache.ctakes.typesystem.type.textsem.Predicate");
    jcasType.ll_cas.ll_setRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_relations, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: frameSet

  /** getter for frameSet - gets 
   * @generated */
  public String getFrameSet() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_frameSet == null)
      jcasType.jcas.throwFeatMissing("frameSet", "org.apache.ctakes.typesystem.type.textsem.Predicate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_frameSet);}
    
  /** setter for frameSet - sets  
   * @generated */
  public void setFrameSet(String v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_frameSet == null)
      jcasType.jcas.throwFeatMissing("frameSet", "org.apache.ctakes.typesystem.type.textsem.Predicate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_frameSet, v);}    
  }

    