

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.ctakes.typesystem.type.relation.Relation;


/** Predicate-argument structure used for semantic role
				labeling output.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class SemanticRoleRelation extends Relation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SemanticRoleRelation.class);
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
  protected SemanticRoleRelation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SemanticRoleRelation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SemanticRoleRelation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: predicate

  /** getter for predicate - gets 
   * @generated */
  public Predicate getPredicate() {
    if (SemanticRoleRelation_Type.featOkTst && ((SemanticRoleRelation_Type)jcasType).casFeat_predicate == null)
      jcasType.jcas.throwFeatMissing("predicate", "org.apache.ctakes.typesystem.type.textsem.SemanticRoleRelation");
    return (Predicate)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((SemanticRoleRelation_Type)jcasType).casFeatCode_predicate)));}
    
  /** setter for predicate - sets  
   * @generated */
  public void setPredicate(Predicate v) {
    if (SemanticRoleRelation_Type.featOkTst && ((SemanticRoleRelation_Type)jcasType).casFeat_predicate == null)
      jcasType.jcas.throwFeatMissing("predicate", "org.apache.ctakes.typesystem.type.textsem.SemanticRoleRelation");
    jcasType.ll_cas.ll_setRefValue(addr, ((SemanticRoleRelation_Type)jcasType).casFeatCode_predicate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: argument

  /** getter for argument - gets 
   * @generated */
  public SemanticArgument getArgument() {
    if (SemanticRoleRelation_Type.featOkTst && ((SemanticRoleRelation_Type)jcasType).casFeat_argument == null)
      jcasType.jcas.throwFeatMissing("argument", "org.apache.ctakes.typesystem.type.textsem.SemanticRoleRelation");
    return (SemanticArgument)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((SemanticRoleRelation_Type)jcasType).casFeatCode_argument)));}
    
  /** setter for argument - sets  
   * @generated */
  public void setArgument(SemanticArgument v) {
    if (SemanticRoleRelation_Type.featOkTst && ((SemanticRoleRelation_Type)jcasType).casFeat_argument == null)
      jcasType.jcas.throwFeatMissing("argument", "org.apache.ctakes.typesystem.type.textsem.SemanticRoleRelation");
    jcasType.ll_cas.ll_setRefValue(addr, ((SemanticRoleRelation_Type)jcasType).casFeatCode_argument, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    