import static org.northshore.cbri.dsl.UIMAUtil.*

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.textsem.EntityMention
import org.apache.ctakes.typesystem.type.textspan.Segment

testConcepts = [
    (~/(?i)(pneumonia|fever|cough|sepsis|weakness)/):[group:0, type:UmlsConcept, codingScheme:"SNOMED", code:"1", cui:"C01", tui:"T01"]
    ]
createMentions(
    patterns:testConcepts, 
    searchSet:select(type:Segment), 
    mentionType:EntityMention
    )
